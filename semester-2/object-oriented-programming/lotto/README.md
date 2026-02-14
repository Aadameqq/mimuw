# Lotto System Simulation

## Project Description

A complex Java simulation of the Polish lottery system (Totolotek), modeling the entire ecosystem of lottery games. The project simulates the interactions between the Central Headquarters, local lottery offices (Kolektura), players with different strategies, and the State Budget. It handles the full lifecycle of a lottery game: filling out betting slips, purchasing coupons, conducting drawings, calculating prize pools (including rollovers), and processing tax obligations.

**Note:** The codebase (classes, methods, variables, and comments) is written entirely in **Polish**, reflecting the local context of the university course.

## Key Features

-   **System Architecture:**
    -   **Central Headquarters (Centrala):** Manages drawings, calculates prize pools for I-IV tiers, handles rollovers (kumulacja), and publishes results.
    -   **Lottery Offices (Kolektura):** Sells coupons, validates tickets, processes payouts, and transfers revenue to headquarters.
    -   **State Budget:** Tracks tax revenue from ticket sales and high winnings, and provides subsidies when the lottery runs a deficit.

-   **Gameplay Mechanics:**
    -   **Coupons & Slips:** Supports manual input via filled slips (Blankiet) and "Quick Pick" (Chybił-Trafił). Coupons are secured with unique IDs and checksums.
    -   **Drawings:** Simulates 6/49 number draws with realistic rules for prize distribution (guaranteed minimums, percentage splits).
    -   **Taxation:** Automatically calculates proper taxes on sold tickets and high-value winnings.

-   **Player Simulation:**
    -   Implemented a strategy pattern for various player types:
        -   **Minimalist:** Plays one random bet in a favorite office.
        -   **Random Player:** Buys random quantities of coupons unpredictably.
        -   **Fixed Numbers Player:** Plays the same set of numbers repeatedly.
        -   **Fixed Slip Player:** Uses a specific slip periodically across multiple offices.

-   **Financial Precision:** Uses integer arithmetic (handling cents/grosze) instead of floating-point to avoid rounding errors in financial calculations, ensuring accurate accounting.

## Project Structure

-   `src/main/java/lotto/zarządzanie/`: Core logic for central management ("Centrala").
-   `src/main/java/lotto/zakłady/`: Logic for coupons and slips processing ("Kolektura").
-   `src/main/java/lotto/gracze/`: Implementations of different player behaviors.
-   `src/main/java/lotto/finanse/`: Financial utilities and state budget handling.
-   `src/main/java/lotto/losowania/`: Simulation of lottery draws.
-   `src/test/`: Comprehensive JUnit tests covering non-random logic.

## Usage

The main simulation scenario (in `Main.java`) demonstrates:
1.  Initialization of the Central Headquarters and 10 offices.
2.  Creation of ~800 players of various strategies.
3.  Execution of 20 sequential drawings.
4.  Reporting of financial results, including state budget revenue and total prizes paid.

## What I Learned

-   Object-oriented modeling of complex business domains with multiple interacting actors.
-   Handling financial calculations safely without floating-point errors.
-   Implementing design patterns:
    -   **Strategy** for changing player behaviors.
    -   **Observer** for notifying players about draw results.
    -   **Value Object** for immutable data (e.g., money representation).
-   Applying **Dependency Inversion** to facilitate deterministic unit testing (e.g., controlling random events).
-   Using **test stubs** to isolate components during testing.
-   Writing unit tests for business logic validation.
