# Sokoban

## Project Description

A C implementation of the logic puzzle **Sokoban**, created as a university project. This version features an advanced command processing system where the user commands specific crates to move, and the program automatically calculates if the player character can reach the necessary pushing position using a pathfinding algorithm. It supports movement history and undo functionality.

## Key Features

-   **Language:** C (C17 standard).
-   **Smart Movement:** The player specifies *which crate* to push and *where*. The program automatically validates if the character can walk to the required pushing spot (pathfinding BFS/DFS logic).
-   **Undo System:** Supports an unlimited (memory-permitting) history of moves, allowing players to revert mistakes with the `0` command.
-   **Dynamic Board:** Reads an initial board state of arbitrary size (containing walls `#`, floors `-`, targets `+`, player `@`/`*`, and labeled crates `a-z`/`A-Z`).
-   **Robustness:** Compiled with strict GCC flags (Wall, Werror, sanitizers) and memory-checked with Valgrind.

## Input/Output

-   **Input:** Initial board map followed by a stream of commands (newline to print, `[crate][direction]` to move, `0` to undo).
-   **Output:** Character-based visualization of the board state.

## What I Learned

-   Implementing graph traversal algorithms (BFS) on a grid to determining reachability.
-   Managing dynamic data structures for undo history (stacks/linked lists).
-   Strict memory management in C to prevent leaks and invalid accesses.
