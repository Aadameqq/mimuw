# nsqrt

## Project Description

This project involves implementing a function in x86-64 Assembly, callable from C, to calculate the integer square root of a large non-negative integer.

Given a $2n$-bit integer $X$, the goal is to find an $n$-bit integer $Q$ such that:
$$Q^2 \le X < (Q+1)^2$$

## Key Features

- **Language:** x86-64 Assembly.
- **Input/Output:** Handles arbitrarily large integers (bit width $n$ divisible by 64, up to 256,000 bits).
- **Data Representation:** Numbers are stored as arrays of `uint64_t` in little-endian order.
- **Algorithm:** Uses an iterative bit-by-bit calculation method, similar to the long division algorithm for square roots, optimizing for low-level memory operations.
- **Optimization Goal:** Prioritized minimizing the binary code size (byte count) over runtime performance.

## What I Learned

- Implementing complex arithmetic algorithms in low-level Assembly x86-64.
- Managing memory and manipulating pointers passed from C.
- Handling multi-precision arithmetic for arbitrarily large integers.
- Optimizing computation using CPU registers and carry flags.
- Techniques for minimizing binary code size, prioritizing compactness over execution speed.
