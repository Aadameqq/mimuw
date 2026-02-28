# Factory

## Project Description

A C concurrency assignment that implements a thread-safe factory (`plant`) for
scheduling tasks across workers and stations. Tasks can be scheduled for the
future, require a fixed number of workers, and must run on a station with
sufficient capacity.

## Key Features

- **Concurrent scheduling:** Manager thread assigns tasks to available workers
  while honoring task start times and station capacity.
- **Worker lifecycle:** Workers run in their own threads and process task parts
  via the provided `work` callback.
- **Task lifecycle:** `add_task` enqueues work, `collect_task` blocks until
  success or failure, and impossible tasks fail eagerly.
- **Station constraints:** Each task runs on a single station with enough
  capacity for all assigned workers.
- **Clean shutdown:** `destroy_plant` waits for in-flight work to finish and
  releases resources.

## Project Structure

- `plant/solution.c`: Factory implementation and synchronization logic.
- `plant/list.c`: Task tracking list and state management.
- `common/err.c`: Shared error helpers.
- `common/plant.h`: Public API definitions for workers, tasks, and plant calls.

The public API consists of:

```c
int init_plant(int* stations, int n_stations, int n_workers);
int destroy_plant();
int add_worker(worker_t* w);
int add_task(task_t* t);
int collect_task(task_t* t);
```

## What I Learned

- Coordinating worker threads with condition variables and shared state.
- Scheduling policies that respect time windows, station capacity, and worker availability.
- Designing a clear task lifecycle with failure propagation in concurrent code.
- Making concurrency decisions observable and deterministic enough for testing.
