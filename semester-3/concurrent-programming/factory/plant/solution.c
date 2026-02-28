#include "../common/err.h"
#include "../common/plant.h"
#include "list.c"

#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

#define ETIMEDOUT 110

#define STATUS_FINISHED 0
#define STATUS_FAILED 1
#define STATUS_PENDING 2
#define STATUS_NOT_STARTED 3

int max(int a, int b)
{
    return (a > b) ? a : b;
}

bool should_shutdown = false;
bool shutdown = false;

struct station_t {
    int capacity;
    bool is_occupied;
};
typedef struct station_t station_t;

struct worker_info_t {
    worker_t* worker;
    bool is_busy;
    pthread_t thread;
    pthread_cond_t worker_wait_cond;
    int current_task_index;
    int task_part_index;
    int station_index;
    bool should_die;
};

typedef struct worker_info_t worker_info_t;

int workers_count;
worker_info_t* workers;
int curr_worker = 0;
int ever_available_workers_count = 0;

task_list_t _tasks;
task_list_t* tasks = &_tasks;

int stations_count;
station_t* stations_list;

pthread_mutex_t plant_mutex;
pthread_cond_t inform_manager_cond;
pthread_t manager_thread;

int doable_tasks = 0;

#define WORKER_STATUS_READY 1
#define WORKER_STATUS_BUSY 2
#define WORKER_STATUS_END 3

int get_worker_status(worker_info_t* w, time_t current_time)
{
    if (w->is_busy || w->worker->start > current_time) {
        return WORKER_STATUS_BUSY;
    }
    if (w->worker->end > current_time) {
        return WORKER_STATUS_READY;
    } else {
        return WORKER_STATUS_END;
    }
}

int fail()
{
    ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));
    return ERROR;
}

int get_empty_station_index(int required_capacity)
{
    for (int i = 0; i < stations_count; i++) {
        if (stations_list[i].is_occupied == false && stations_list[i].capacity >= required_capacity) {
            return i;
        }
    }
    return -1;
}

int count_available_workers(time_t current_time)
{
    int count = 0;
    for (int i = 0; i < curr_worker; i++) {
        int status = get_worker_status(&workers[i], current_time);
        if (status == WORKER_STATUS_READY) {
            count++;
        }
    }
    return count;
}

void assign_task_to_worker(int task_index, worker_info_t* worker, int part_index)
{
    worker->is_busy = true;
    worker->current_task_index = task_index;
    worker->task_part_index = part_index;
}

bool is_task_ever_possible(task_processing_t* task_proc)
{
    if (task_proc->task->capacity > ever_available_workers_count) {
        return false;
    }

    for (int i = 0; i < stations_count; i++) {
        if (stations_list[i].capacity >= task_proc->task->capacity) {
            return true;
        }
    }
    return false;
}

void end_task(task_processing_t* task_proc, int station_index)
{
    stations_list[station_index].is_occupied = false;
    task_proc->status = STATUS_FINISHED;
    ASSERT_ZERO(pthread_cond_signal(&task_proc->task_finished_cond));
}

void finish_task(worker_info_t* w, int result)
{
    task_processing_t* current_task = task_list_get(tasks, w->current_task_index);
    current_task->task->results[w->task_part_index] = result;
    current_task->currently_working--;
    w->current_task_index = -1;
    w->is_busy = false;
}

void mark_task_as_pending(task_processing_t* task_proc, int station_index)
{
    stations_list[station_index].is_occupied = true;
    task_proc->status = STATUS_PENDING;
    task_proc->currently_working = task_proc->task->capacity;
    task_proc->station_index = station_index;
}

void assign_task_workers(int task_index, int* assigned_workers, int workers_count)
{
    int part_index = 0;
    for (int i = 0; i < workers_count; i++) {
        assign_task_to_worker(task_index, &workers[assigned_workers[i]], part_index++);
        ASSERT_ZERO(pthread_cond_signal(&workers[assigned_workers[i]].worker_wait_cond));
    }
}

void* worker(void* worker_t_pointer)
{
    worker_info_t* w = (worker_info_t*)worker_t_pointer;
    ASSERT_ZERO(pthread_mutex_lock(&plant_mutex));
    while (w->worker->start > time(0)) {
        struct timespec ts;
        ASSERT_ZERO(clock_gettime(CLOCK_REALTIME, &ts));
        ts.tv_sec += max(w->worker->start - time(0), 0);
        int ret = pthread_cond_timedwait(&w->worker_wait_cond, &plant_mutex, &ts);
        if (ret != 0 && ret != ETIMEDOUT) {
            ASSERT_ZERO(-1);
        }
    }
    w->is_busy = false;
    while (true) {
        while (w->is_busy == false) {
            if (shutdown) {
                ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));
                return NULL;
            }
            if (w->should_die) {
                ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));
                return NULL;
            }
            ASSERT_ZERO(pthread_cond_signal(&inform_manager_cond));
            ASSERT_ZERO(pthread_cond_wait(&w->worker_wait_cond, &plant_mutex));
        }
        task_t* current_task = task_list_get(tasks, w->current_task_index)->task;
        int task_part = w->task_part_index;
        ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));

        int result = w->worker->work(w->worker, current_task, task_part);

        ASSERT_ZERO(pthread_mutex_lock(&plant_mutex));
        finish_task(w, result);
        ASSERT_ZERO(pthread_cond_signal(&inform_manager_cond));
    }
    return NULL;
}

void* manager()
{
    ASSERT_ZERO(pthread_mutex_lock(&plant_mutex));
    while (true) {
        int nearest_task_time = -1;
        for (int i = 0; i < task_list_size(tasks); i++) {
            task_processing_t* task_proc = task_list_get(tasks, i);
            if (!is_task_ever_possible(task_proc) && task_proc->status == STATUS_NOT_STARTED) {
                task_proc->status = STATUS_FAILED;
                doable_tasks--;
                ASSERT_ZERO(pthread_cond_signal(&task_proc->task_finished_cond));
                continue;
            }
            if (task_proc->status == STATUS_PENDING && task_proc->currently_working == 0) {
                end_task(task_proc, task_proc->station_index);
                doable_tasks--;
                continue;
            }
            if (task_proc->status != STATUS_NOT_STARTED) {
                continue;
            } else if (task_proc->task->start > time(0)) {
                if (nearest_task_time == -1 || task_proc->task->start < nearest_task_time) {
                    nearest_task_time = task_proc->task->start;
                }
                continue;
            }
            int station_index = get_empty_station_index(task_proc->task->capacity);
            if (station_index == -1) {
                continue;
            }
            int* assigned_workers = malloc(sizeof(int) * task_proc->task->capacity);
            int next_worker = 0;

            for (int j = 0; j < curr_worker; j++) {
                int status = get_worker_status(&workers[j], time(0));
                if (status == WORKER_STATUS_READY) {
                    if (next_worker >= task_proc->task->capacity) {
                        continue;
                    }
                    assigned_workers[next_worker++] = j;
                }
                if (status == WORKER_STATUS_END && !workers[j].should_die) {
                    workers[j].should_die = true;
                    ever_available_workers_count--;
                    ASSERT_ZERO(pthread_cond_signal(&workers[j].worker_wait_cond));
                }
            }

            if (next_worker < task_proc->task->capacity) {
                free(assigned_workers);
                if (is_task_ever_possible(task_proc) == false && task_proc->status == STATUS_NOT_STARTED) {
                    doable_tasks--;
                    task_proc->status = STATUS_FAILED;
                    ASSERT_ZERO(pthread_cond_signal(&task_proc->task_finished_cond));
                }

                continue;
            }
            mark_task_as_pending(task_proc, station_index);
            assign_task_workers(i, assigned_workers, task_proc->task->capacity);
            free(assigned_workers);
        }
        if (should_shutdown && doable_tasks == 0) {
            shutdown = true;
            for (int i = 0; i < curr_worker; i++) {
                ASSERT_ZERO(pthread_cond_signal(&workers[i].worker_wait_cond));
            }
            ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));
            return NULL;
        }
        if (nearest_task_time != -1 && nearest_task_time > time(0)) {
            struct timespec ts;
            ASSERT_ZERO(clock_gettime(CLOCK_REALTIME, &ts));
            ts.tv_sec += max(nearest_task_time - time(0), 0);
            int ret = pthread_cond_timedwait(&inform_manager_cond, &plant_mutex, &ts);
            if (ret != 0 && ret != ETIMEDOUT) {
                ASSERT_ZERO(-1);
            }
            continue;
        }
        ASSERT_ZERO(pthread_cond_wait(&inform_manager_cond, &plant_mutex));
    }
}

int init_plant(int* stations, int n_stations, int n_workers)
{
    curr_worker = 0;
    ever_available_workers_count = n_workers;
    should_shutdown = false;
    shutdown = false;
    doable_tasks = 0;

    workers_count = n_workers;

    workers = (worker_info_t*)malloc(sizeof(worker_info_t) * workers_count);

    if (workers == NULL) {
        return ERROR;
    }

    for (int i = 0; i < workers_count; i++) {
        workers[i].worker = NULL;
        workers[i].is_busy = true;
        workers[i].current_task_index = -1;
        workers[i].task_part_index = 0;
        workers[i].station_index = 0;
    }

    task_list_init(tasks, 2000);

    if (tasks == NULL) {
        free(workers);
        return ERROR;
    }

    stations_count = n_stations;
    stations_list = (station_t*)malloc(sizeof(station_t) * stations_count);

    if (stations_list == NULL) {
        task_list_destroy(tasks);
        free(workers);
        return ERROR;
    }

    for (int i = 0; i < stations_count; i++) {
        stations_list[i].capacity = stations[i];
        stations_list[i].is_occupied = false;
    }

    if (pthread_mutex_init(&plant_mutex, NULL) != 0) {
        task_list_destroy(tasks);
        free(workers);
        free(stations_list);
        return ERROR;
    }

    if (pthread_cond_init(&inform_manager_cond, NULL) != 0) {
        task_list_destroy(tasks);
        free(workers);
        free(stations_list);
        pthread_mutex_destroy(&plant_mutex);
        return ERROR;
    }

    if (pthread_create(&manager_thread, NULL, manager, NULL) != 0) {
        task_list_destroy(tasks);
        free(workers);
        free(stations_list);
        pthread_mutex_destroy(&plant_mutex);
        pthread_cond_destroy(&inform_manager_cond);
        return ERROR;
    }

    return PLANTOK;
}

int destroy_plant()
{
    ASSERT_ZERO(pthread_mutex_lock(&plant_mutex));
    should_shutdown = true;
    ASSERT_ZERO(pthread_cond_signal(&inform_manager_cond));
    ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));
    ASSERT_ZERO(pthread_join(manager_thread, NULL));
    for (int i = 0; i < curr_worker; i++) {
        ASSERT_ZERO(pthread_join(workers[i].thread, NULL));
        ASSERT_ZERO(pthread_cond_destroy(&workers[i].worker_wait_cond));
    }
    task_list_destroy(tasks);
    free(workers);
    free(stations_list);
    ASSERT_ZERO(pthread_mutex_destroy(&plant_mutex));
    ASSERT_ZERO(pthread_cond_destroy(&inform_manager_cond));

    curr_worker = 0;
    ever_available_workers_count = 0;
    should_shutdown = false;
    workers = NULL;
    stations_list = NULL;

    return PLANTOK;
}

int add_worker(worker_t* w)
{
    ASSERT_ZERO(pthread_mutex_lock(&plant_mutex));
    if (should_shutdown) {
        return fail();
    }
    worker_info_t* w_info = &workers[curr_worker++];
    w_info->worker = w;
    w_info->is_busy = true;
    w_info->current_task_index = -1;
    w_info->should_die = false;

    ASSERT_ZERO(pthread_cond_init(&w_info->worker_wait_cond, NULL));

    ASSERT_ZERO(pthread_create(&w_info->thread, NULL, worker, (void*)w_info));

    ASSERT_ZERO(pthread_cond_signal(&inform_manager_cond));

    ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));
    return PLANTOK;
}

int add_task(task_t* t)
{
    if (t == NULL) {
        return ERROR;
    }
    ASSERT_ZERO(pthread_mutex_lock(&plant_mutex));
    task_processing_t* existing_task = task_list_find_by_id(tasks, t->id);
    if (existing_task != NULL) {
        ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));
        return PLANTOK;
    }
    if (should_shutdown) {
        return fail();
    }
    doable_tasks++;
    task_list_add(tasks, t, STATUS_NOT_STARTED);

    ASSERT_ZERO(pthread_cond_signal(&inform_manager_cond));
    ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));
    return PLANTOK;
}

int collect_task(task_t* t)
{
    ASSERT_ZERO(pthread_mutex_lock(&plant_mutex));
    if (should_shutdown) {
        return fail();
    }
    task_processing_t* task_proc = task_list_find_by_id(tasks, t->id);
    if (task_proc == NULL) {
        return fail();
    }

    while (task_proc->status != STATUS_FINISHED && task_proc->status != STATUS_FAILED) {
        ASSERT_ZERO(pthread_cond_wait(&task_proc->task_finished_cond, &plant_mutex));
    }

    if (task_proc->status == STATUS_FAILED) {
        return fail();
    }
    ASSERT_ZERO(pthread_mutex_unlock(&plant_mutex));
    return PLANTOK;
}
