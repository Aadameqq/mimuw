# SET Game Simulation

## Project Description

A simulation of the **SET** card game's end-game phase. The program processes a sequence of cards (defined by 4 attributes) and simulates a deterministic player finding and removing valid sets according to strict rules.

## Key Features

-   **Language:** C
-   **Game Logic:** Implements rules for identifying valid Sets (three cards where each attribute is either all identical or all different).
-   **Deterministic Simulation:** Always selects the "first" available set based on table position.
-   **Dynamic State:** Manages table updates—removing sets and refilling from the deck—until the game ends.
-   **Input/Output:** Parses integer-based card representations and generates a step-by-step game log.

## What I Learned

-   Implementing complex game rules and validation logic.
-   Implementing search algorithms for combinations.
-   Managing program state and processing standard input/output.
