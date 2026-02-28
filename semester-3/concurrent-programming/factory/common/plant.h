#pragma once

#include <time.h>

#define PLANTOK 0
#define ERROR -1

typedef int (*task_function_t)(int);

typedef struct task_t {
    int id;
    time_t start;
    task_function_t task_function;
    int capacity;
    int* data;
    int* results;
} task_t;

struct worker_t;

typedef int (*worker_function_t)(struct worker_t* w, task_t* t, int i);

typedef struct worker_t {
    int id;
    time_t start;
    time_t end;
    worker_function_t work;
} worker_t;

int init_plant(int* stations, int n_stations, int n_workers);
int destroy_plant();
int add_worker(worker_t* w);
int add_task(task_t* t);
int collect_task(task_t* t);
