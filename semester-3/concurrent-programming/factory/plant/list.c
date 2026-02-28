#include "../common/err.h"
#include "../common/plant.h"
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct task_processing_t {
    task_t* task;
    int status;
    pthread_cond_t task_finished_cond;
    int currently_working;
    int station_index;
} task_processing_t;

typedef struct task_list_t {
    task_processing_t* tasks;
    int capacity;
    int size;
} task_list_t;

int task_list_init(task_list_t* list, int initial_capacity)
{
    if (!list) {
        return -1;
    }

    list->tasks = malloc(sizeof(task_processing_t) * initial_capacity);
    if (!list->tasks) {
        return -1;
    }

    for (int i = 0; i < initial_capacity; i++) {
        list->tasks[i].task = NULL;
        list->tasks[i].status = 2;
        list->tasks[i].currently_working = 0;
        list->tasks[i].station_index = 0;
    }

    list->capacity = initial_capacity;
    list->size = 0;

    return 0;
}

int task_list_resize(task_list_t* list, int new_capacity)
{
    int old_capacity = list->capacity;
    task_processing_t* new_tasks = realloc(list->tasks, sizeof(task_processing_t) * new_capacity);
    if (!new_tasks) {
        return -1;
    }

    for (int i = old_capacity; i < new_capacity; i++) {
        new_tasks[i].task = NULL;
        new_tasks[i].status = 2;
        new_tasks[i].currently_working = 0;
        new_tasks[i].station_index = 0;
    }

    list->tasks = new_tasks;
    list->capacity = new_capacity;
    return 0;
}

task_processing_t* task_list_add(task_list_t* list, task_t* task, int status)
{
    if (list->size >= list->capacity) {
        ASSERT_ZERO(task_list_resize(list, list->capacity * 2));
    }

    task_processing_t* new_task = &list->tasks[list->size];
    new_task->task = task;
    new_task->status = status;
    new_task->currently_working = 0;

    ASSERT_ZERO(pthread_cond_init(&new_task->task_finished_cond, NULL));

    list->size++;

    return new_task;
}

task_processing_t* task_list_get(task_list_t* list, int index)
{
    return &list->tasks[index];
}

task_processing_t* task_list_find_by_id(task_list_t* list, int task_id)
{
    for (int i = 0; i < list->size; i++) {
        if (list->tasks[i].task && list->tasks[i].task->id == task_id) {
            return &list->tasks[i];
        }
    }

    return NULL;
}

int task_list_size(task_list_t* list)
{
    return list->size;
}

void task_list_destroy(task_list_t* list)
{
    if (!list) {
        return;
    }

    for (int i = 0; i < list->size; i++) {
        ASSERT_ZERO(pthread_cond_destroy(&list->tasks[i].task_finished_cond));
    }

    free(list->tasks);

    list->tasks = NULL;
    list->capacity = 0;
    list->size = 0;
}
