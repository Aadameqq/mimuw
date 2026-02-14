# Rubik's Cube Simulation

## Project Description

This project involves implementing a simulation of a generic $N \times N \times N$ Rubik's Cube (where $N$ is a positive integer) in C. The program executes a sequence of commands to rotate the layers of a solved cube and prints its current state upon request.

## Key Features

-   **Language:** C (C17 standard).
-   **Configurable Size:** The cube size $N$ is defined as a symbolic constant, selectable at compile time (default $N=5$).
-   **Operation Processing:** Reads and parses a stream of commands for:
    -   **Rotations:** Rotate specific layers (single or multiple) of any face (Up, Left, Front, Right, Back, Down) by $90^\circ$, $-90^\circ$, or $180^\circ$.
    -   **State Output:** Prints the current colorful configuration of all 6 faces in a specific unfolded net format.
-   **Data Validation:** Adheres to strict compilation flags (Wall, Wextra, Werror, etc.) and memory safety checks (Valgrind) as per course requirements.

## Input Format

The program reads a sequence of commands terminated by a dot (`.`).
-   **Rotation Command:** `[Number of Layers][Face][Angle]`
    -   *Layers:* Empty (1) or positive integer $\le N$.
    -   *Face:* `u`, `l`, `f`, `r`, `b`, `d`.
    -   *Angle:* Empty ($90^\circ$), `'` ($-90^\circ$), or `"` ($180^\circ$).
-   **Print Command:** A newline character.

## Output Format

The cube is printed as an unfolded net map with numeric color codes (0-5) representing the faces:
```text
  u
l|f|r|b
  d
```
The output format is strictly defined, including exact spacing and separators.

## What I Learned

-   Modelling complex 3D structures and transformations in 1D/2D arrays.
-   Implementing tensor/matrix rotations and layer shifting logic.
-   Parsing text-based command streams with context-sensitive grammar.
-   Writing rigorous C code compliant with strict static analysis and memory safety standards.
