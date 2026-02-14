# freverse

## Project Description

A utility program written in x86-64 Assembly that reverses the contents of a file in-place. The program handles large files (exceeding 4 GiB) efficiently using direct Linux system calls.

## Key Features

-   **System Calls:** Directly uses Linux system calls (e.g., `sys_open`, `sys_read`, `sys_write`, `sys_lseek`, `sys_mmap`) avoiding standard library dependencies.
-   **Efficiency:** Designed to handle files of arbitrary size, including those larger than 4 GiB.
-   **Robustness:** Implements comprehensive error handling for invalid arguments and failed system calls, ensuring resources like file descriptors are properly closed.
-   **Minimalism:** operates silently without terminal output and compiles directly with `nasm` and `ld`.

## What I Learned

-   Direct interaction with the Linux kernel via system calls (the ABI).
-   File manipulation and I/O operations at the lowest level.
-   Using memory mapping (`mmap`) for efficient file processing.
-   Managing file descriptors and ensuring resource cleanup in Assembly.
