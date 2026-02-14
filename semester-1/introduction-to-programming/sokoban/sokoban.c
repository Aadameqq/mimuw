#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#define EmptyField '-'
#define EmptyTargetField '+'
#define Wall '#'
#define EmptyFieldWithPlayer '@'
#define TargetFieldWithPlayer '*'

#define Down '2'
#define Left '4'
#define Right '6'
#define Up '8'

typedef struct
{
    int row;
    int col;
} Position;

typedef struct
{
    char chestId;
    char direction;
    Position previousPosition;
} HistoryElement;

typedef struct
{
    char **map;
    int *rowsLengths;
    int rowsAmount;
    Position playerPosition;
    HistoryElement *history;
    int historyRealSize;
    int historySize;
} Map;

typedef struct
{
    Position position;
    char direction;
    char chestId;
} ChestMovementCommand;

// Sprawdza czy dany znak reprezentuje postać
bool fieldIsPlayer(char field)
{
    return field == EmptyFieldWithPlayer || field == TargetFieldWithPlayer;
}

// Oblicza nowy rozmiar tablicy dynamicznej
void calculateNewSize(int *size)
{
    *size = 2 * ((*size) + 1);
}

// Powiększa tablicę char'ów do danego rozmiaru
void resizeCharArray(char **arr, int size)
{
    *arr = realloc(*arr, (size_t)(size) * sizeof(char));
}

// Powiększa tablicę tablic char'ów do danego rozmiaru
void resizeCharPointerArray(char ***arr, int size)
{
    *arr = realloc(*arr, (size_t)(size) * sizeof(char *));
}

// Powiększa tablicę int'ów do danego rozmiaru
void resizeInt(int **arr, int size)
{
    *arr = realloc(*arr, (size_t)(size) * sizeof(int));
}

// Wczytuje pojedyńczy wiersz planszy
void readRow(char **arr, int *size, Position *playerPosition, int row)
{
    *arr = NULL;
    *size = 0;
    int new;
    int realSize = 0;
    for (int i = 0; ((new = getchar()) != '\n') && (new != EOF); i++)
    {
        if (i == realSize)
        {
            calculateNewSize(&realSize);
            resizeCharArray(arr, realSize);
        }

        (*arr)[i] = (char)new;
        *size += 1;

        if (fieldIsPlayer((char)new))
        {
            playerPosition->row = row;
            playerPosition->col = i;
        }
    }
}

// Sprawdza czy dany znak reprezentuje skrzynię nie znajdującą się na polu docelowym
bool fieldIsEmptyFieldWithChest(char x)
{
    const int aLetterAsciiCode = 97;
    const int zLetterAsciiCode = 122;

    return (x >= aLetterAsciiCode && x <= zLetterAsciiCode);
}

// Sprawdza czy dany znak reprezentuje skrzynię znajdującą się na polu docelowym
bool fieldIsTargetFieldWithChest(char x)
{
    const int ALetterAsciiCode = 65;
    const int ZLetterAsciiCode = 90;

    return (x >= ALetterAsciiCode && x <= ZLetterAsciiCode);
}

// Zwraca wartość danego pola mapy
char getField(Map *map, Position *position)
{
    return map->map[position->row][position->col];
}

// Ustawia wartość danego pola mapy na daną wartość
void setField(Map *map, Position *position, char value)
{
    map->map[position->row][position->col] = value;
}

// Sprawdza czy dany znak jest elementem mapy
bool isMapField(char x)
{
    return fieldIsEmptyFieldWithChest(x) ||
           fieldIsTargetFieldWithChest(x) ||
           x == Wall ||
           x == EmptyField ||
           x == EmptyTargetField ||
           x == EmptyFieldWithPlayer ||
           x == TargetFieldWithPlayer;
}

// Wczytuje mapę
void readMap(Map *map)
{
    int realSize = 0;
    int new;
    Position playerPosition;
    for (int i = 0; (new = getchar()) && isMapField((char)new); i++)
    {
        ungetc(new, stdin);
        if (i == realSize)
        {
            calculateNewSize(&realSize);
            resizeCharPointerArray(&map->map, realSize);
            resizeInt(&map->rowsLengths, realSize);
        }
        readRow(&(map->map)[i], &(map->rowsLengths)[i], &playerPosition, i);
        map->rowsAmount = i + 1;
    }
    map->playerPosition = playerPosition;
    ungetc(new, stdin);
}

// Wyświetla mapę
void printMap(Map *map)
{
    for (int row = 0; row < map->rowsAmount; row++)
    {
        for (int col = 0; col < map->rowsLengths[row]; col++)
        {
            printf("%c", map->map[row][col]);
        }

        printf("\n");
    }
}

// Czyści pamięć zajętą przez tablicę tablic przechowującą stan mapy
void freeMap(Map *map)
{
    for (int row = 0; row < map->rowsAmount; row++)
    {
        free(map->map[row]);
    }
    free(map->map);
    free(map->rowsLengths);
    free(map->history);
}

// Ustawia wartości początkowe
void initMap(Map *gameMap)
{
    gameMap->map = NULL;
    gameMap->rowsLengths = NULL;
    gameMap->history = NULL;
    gameMap->rowsAmount = 0;
    gameMap->historyRealSize = 0;
    gameMap->historySize = 0;
}

// Sprawdza czy dana pozycja znajduje się w obrębie mapy
bool isPositionOnMap(Map *map, Position *position)
{
    return position->row >= 0 &&
           position->col >= 0 &&
           position->row < map->rowsAmount &&
           position->col < map->rowsLengths[position->row];
}

// Sprawdza czy dana pozycja mapy nie jest ścianą
bool isPositionOnMapAndNotOnWall(Map *map, Position *position)
{
    if (!isPositionOnMap(map, position))
        return false;
    char field = getField(map, position);
    return field != Wall;
}

// Aktualizuje pozycję w zależności od wskazanego kierunku ruchu.
void calculateNewPosition(Position *position, char direction)
{
    switch (direction)
    {
    case Up:
        position->row -= 1;
        break;
    case Down:
        position->row += 1;
        break;
    case Left:
        position->col -= 1;
        break;
    case Right:
        position->col += 1;
        break;
    }
}

// Sprawdza czy dane przesunięcie to przesunięcie na skos
bool isOffsetDiagonal(int row, int col)
{
    return row * col != 0;
}

// Aktualizuje pozycję o zadane przesunięcie
void applyOffset(Position *position, int rowOffset, int colOffset)
{
    position->row += rowOffset;
    position->col += colOffset;
}

// Konwertuje małą literę na wielką literę w kodzie ASCII.
char toUpperCase(char c)
{
    return c - 'a' + 'A';
}

// Sprawdza czy na daną pozycję da się wejść
bool isPositionPassable(Map *map, Position *position)
{
    if (!isPositionOnMap(map, position))
        return false;
    char field = getField(map, position);
    return field != Wall &&
           !fieldIsEmptyFieldWithChest(field) &&
           !fieldIsTargetFieldWithChest(field);
}

// Rekurencyjnie wyszukuje ścieżkę do skrzyni o wskazanym identyfikatorze (znaku reprezentującym ją na mapie)
bool tryFindChestIfPathExists(Map *map, ChestMovementCommand *command, Position startPosition, int **visited)
{
    if (!isPositionPassable(map, &startPosition) || visited[startPosition.row][startPosition.col] == true)
    {
        return false;
    }
    visited[startPosition.row][startPosition.col] = true;

    Position positionToCheck = startPosition;
    calculateNewPosition(&positionToCheck, command->direction);
    if (isPositionOnMap(map, &positionToCheck) &&
        (getField(map, &positionToCheck) == toUpperCase(command->chestId) ||
         getField(map, &positionToCheck) == command->chestId))
    {
        command->position = positionToCheck;
        return true;
    }

    bool ok = false;

    for (int rowOffset = -1; rowOffset <= 1 && !ok; rowOffset++)
    {
        for (int colOffset = -1; colOffset <= 1 && !ok; colOffset++)
        {
            if ((rowOffset == 0 && colOffset == 0) || isOffsetDiagonal(rowOffset, colOffset))
            {
                continue;
            }
            applyOffset(&startPosition, rowOffset, colOffset);
            ok = tryFindChestIfPathExists(map, command, startPosition, visited);
            applyOffset(&startPosition, -1 * rowOffset, -1 * colOffset);
        }
    }

    return ok;
}

// Sprawdza czy daną skrzynię da się przesunąć w danym kierunku
bool canChestBeMoved(Map *map, ChestMovementCommand *command)
{
    Position nextPosition = command->position;
    calculateNewPosition(&nextPosition, command->direction);

    return isPositionPassable(map, &nextPosition);
}

// Przesuwa skrzynię w wskazanym kierunku
void moveChest(Map *map, ChestMovementCommand *command, bool shouldClear)
{
    Position nextPosition = command->position;
    calculateNewPosition(&nextPosition, command->direction);

    char field = getField(map, &nextPosition);

    if (field == EmptyField || field == EmptyFieldWithPlayer)
    {
        setField(map, &nextPosition, command->chestId);
    }
    if (field == EmptyTargetField || field == TargetFieldWithPlayer)
    {
        setField(map, &nextPosition, toUpperCase(command->chestId));
    }

    if (shouldClear)
    {
        char oldField = getField(map, &command->position);

        if (fieldIsTargetFieldWithChest(oldField))
        {
            setField(map, &command->position, EmptyTargetField);
        }

        if (fieldIsEmptyFieldWithChest(oldField))
        {
            setField(map, &command->position, EmptyField);
        }
    }
}

// Przesuwa gracza na wskazaną pozycję
void movePlayer(Map *map, Position *newPosition, bool shouldClear)
{
    char field = getField(map, newPosition);

    if (field == EmptyField || fieldIsEmptyFieldWithChest(field))
    {
        setField(map, newPosition, EmptyFieldWithPlayer);
    }
    if (field == EmptyTargetField || fieldIsTargetFieldWithChest(field))
    {
        setField(map, newPosition, TargetFieldWithPlayer);
    }
    if (shouldClear)
    {
        char oldField = getField(map, &map->playerPosition);

        if (oldField == TargetFieldWithPlayer)
        {
            setField(map, &map->playerPosition, EmptyTargetField);
        }

        if (oldField == EmptyFieldWithPlayer)
        {
            setField(map, &map->playerPosition, EmptyField);
        }
    }

    map->playerPosition = *newPosition;
}

// Tworzy i inicjalizuje listę odwiedzonych pól oraz ustawia wszystkie pola jako nieodwiedzone.
void initializeVisitedList(int ***visited, Map *map)
{
    *visited = (int **)malloc((size_t)map->rowsAmount * sizeof(int *));

    for (int row = 0; row < map->rowsAmount; row++)
    {
        (*visited)[row] = (int *)malloc((size_t)map->rowsLengths[row] * sizeof(int));

        for (int col = 0; col < map->rowsLengths[row]; col++)
        {
            (*visited)[row][col] = 0;
        }
    }
}

// Usuwa zawartość tablicy odwiedzonych pól
void cleanVisitedList(int **visited, Map *map)
{
    for (int row = 0; row < map->rowsAmount; row++)
    {
        for (int col = 0; col < map->rowsLengths[row]; col++)
        {
            visited[row][col] = 0;
        }
    }
}

// Czyści pamięć zajętą przez tablicę tablic przechowującą listę odwiedzonych pól
void freeVisitedList(int **visited, Map *map)
{
    for (int row = 0; row < map->rowsAmount; row++)
    {
        free((visited)[row]);
    }
    free(visited);
}

// Powiększa tablicę zawierającą historię przesunięć do danego rozmiaru
void resizeHistory(HistoryElement **arr, int size)
{
    *arr = realloc(*arr, (size_t)(size) * sizeof(HistoryElement));
}

// Dodaje przesunięcie do historii
void addToHistory(Map *map, HistoryElement *historyElement)
{
    if (map->historySize == map->historyRealSize)
    {
        calculateNewSize(&map->historyRealSize);
        resizeHistory(&map->history, map->historyRealSize);
    }
    map->history[map->historySize] = *historyElement;
    map->historySize++;
}

// Zwraca kierunek przeciwny do wskazanego
char getOppositeDirection(char direction)
{
    switch (direction)
    {
    case Down:
        return Up;
    case Up:
        return Down;
    case Right:
        return Left;
    case Left:
        return Right;
    default:
        return 0;
    }
}

// Cofa ostatni ruch
void undoRecentMove(Map *map)
{
    if (map->historySize <= 0)
    {
        return;
    }

    HistoryElement el = map->history[map->historySize - 1];
    map->historySize--;
    Position chestPosition = map->playerPosition;
    movePlayer(map, &el.previousPosition, true);

    ChestMovementCommand cmd;
    cmd.chestId = el.chestId;
    cmd.direction = getOppositeDirection(el.direction);
    calculateNewPosition(&chestPosition, el.direction);
    cmd.position = chestPosition;

    bool shouldClear = true;

    if (getField(map, &chestPosition) == EmptyFieldWithPlayer || getField(map, &chestPosition) == TargetFieldWithPlayer)
    {
        shouldClear = false;
    }

    moveChest(map, &cmd, shouldClear);
}

// Wykonuje rozkaz przesunięcia
void move(Map *map, char chestId, char direction, int **visited)
{
    ChestMovementCommand command;
    command.chestId = chestId;
    command.direction = direction;

    bool result = tryFindChestIfPathExists(map, &command, map->playerPosition, visited);
    cleanVisitedList(visited, map);

    if (!result)
        return;

    if (!canChestBeMoved(map, &command))
        return;

    moveChest(map, &command, false);
    HistoryElement el;
    el.chestId = command.chestId;
    el.direction = command.direction;
    el.previousPosition = map->playerPosition;
    addToHistory(map, &el);
    movePlayer(map, &command.position, true);
}

// Wykonuje komendy użytkownika: drukuje mapę, przesuwa skrzynie lub cofa ostatni ruch.
void executeCommands(Map *map)
{
    int **visited = NULL;
    initializeVisitedList(&visited, map);
    char input;
    while ((input = (char)getchar()) != '.')
    {
        if (input == '\n')
        {
            printMap(map);
        }
        else if (fieldIsEmptyFieldWithChest(input))
        {
            char direction = (char)getchar();
            move(map, input, direction, visited);
        }
        else if (input == '0')
        {
            undoRecentMove(map);
        }
    }
    freeVisitedList(visited, map);
}

int main()
{
    Map map;
    initMap(&map);
    readMap(&map);
    executeCommands(&map);
    freeMap(&map);
}
