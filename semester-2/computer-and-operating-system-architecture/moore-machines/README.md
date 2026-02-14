# Moore Machines

## Project Description

This project involves implementing a dynamically loaded C library (`libma.so`) that simulates **Moore Machines**, a type of finite state automaton used in digital logic circuits.

The library allows creating, connecting, and simulating networks of interacting Moore machines. It models inputs, outputs, and internal states as bit sequences.

## Key Features

-   **Dynamic Library:** Implemented as a shared object (`.so`) for dynamic linking.
-   **Machine Representation:** Defines a `moore_t` structure handling internal state $Q$, transition function $\delta$ (inputs $\times$ state $\to$ next state), and output function $\lambda$ (state $\to$ output).
-   **Bitwise Logic:** Operates on arbitrary-length bit strings (using `uint64_t` arrays) to simulate digital signals.
-   **Connectivity:** Supports complex wiring between machines—connecting outputs of one machine to inputs of another (`ma_connect`).
-   **Synchronous Simulation:** The `ma_step` function advances the state of all machines in the network simultaneously, ensuring synchronous behavior typical of digital circuits.
-   **Memory Safety:** Robust memory management with wrappers (`malloc`, `free`, etc.) to intercept and test allocation failures, ensuring no memory leaks occur even under error conditions.

## API Overview

-   `ma_create_full`: Creates a custom machine with user-defined transition and output functions.
-   `ma_create_simple`: Creates a simplified machine where the output equals the internal state.
-   `ma_connect` / `ma_disconnect`: Manages signal connections between machines.
-   `ma_step`: Executes one clock cycle for a set of machines.
-   `ma_set_input` / `ma_set_state` / `ma_get_output`: Manual control and inspection of machine signals.

## What I Learned

-   Building and linking dynamic libraries in C / Linux.
-   Managing complex pointer networks and dynamic memory in C.
-   Simulating digital logic and synchronous systems in software.
-   Implementing robust error handling and memory safety patterns (weak exception safety).
