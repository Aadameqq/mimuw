#ifndef N
#define N 5
#endif

#include <stdio.h>
#include <stdbool.h>

int walls[6][N][N];

#define UP_WALL 0
#define LEFT_WALL 1
#define FRONT_WALL 2
#define RIGHT_WALL 3
#define BACK_WALL 4
#define DOWN_WALL 5

#define CLOCKWISE_ROTATION ' '
#define COUNTER_CLOCKWISE_ROTATION '\''
#define TWICE_CLOCKWISE_ROTATION '"'

#define TOP_PART 0
#define RIGHT_PART 1
#define BOTTOM_PART 2
#define LEFT_PART 3

#define TOP_REFERENCE 0
#define RIGHT_REFERENCE 1
#define BOTTOM_REFERENCE 2
#define LEFT_REFERENCE 3

#define FAILURE (-1)

int oppositeWalls[6] = {DOWN_WALL, RIGHT_WALL, BACK_WALL, LEFT_WALL, FRONT_WALL, UP_WALL};

int wallReferences[6][4][2];

void setReference(int target, int referenceIndex, int referenced, int part)
{
    wallReferences[target][referenceIndex][0] = referenced;
    wallReferences[target][referenceIndex][1] = part;
}

void setUpWallReferences()
{
    setReference(UP_WALL, TOP_REFERENCE, BACK_WALL, TOP_PART);
    setReference(UP_WALL, RIGHT_REFERENCE, RIGHT_WALL, TOP_PART);
    setReference(UP_WALL, BOTTOM_REFERENCE, FRONT_WALL, TOP_PART);
    setReference(UP_WALL, LEFT_REFERENCE, LEFT_WALL, TOP_PART);
}

void setFrontWallReferences()
{
    setReference(FRONT_WALL, TOP_REFERENCE, UP_WALL, BOTTOM_PART);
    setReference(FRONT_WALL, RIGHT_REFERENCE, RIGHT_WALL, LEFT_PART);
    setReference(FRONT_WALL, BOTTOM_REFERENCE, DOWN_WALL, TOP_PART);
    setReference(FRONT_WALL, LEFT_REFERENCE, LEFT_WALL, RIGHT_PART);
}

void setRightWallReferences()
{
    setReference(RIGHT_WALL, TOP_REFERENCE, UP_WALL, RIGHT_PART);
    setReference(RIGHT_WALL, RIGHT_REFERENCE, BACK_WALL, LEFT_PART);
    setReference(RIGHT_WALL, BOTTOM_REFERENCE, DOWN_WALL, RIGHT_PART);
    setReference(RIGHT_WALL, LEFT_REFERENCE, FRONT_WALL, RIGHT_PART);
}

void setDownWallReferences()
{
    setReference(DOWN_WALL, TOP_REFERENCE, FRONT_WALL, BOTTOM_PART);
    setReference(DOWN_WALL, RIGHT_REFERENCE, RIGHT_WALL, BOTTOM_PART);
    setReference(DOWN_WALL, BOTTOM_REFERENCE, BACK_WALL, BOTTOM_PART);
    setReference(DOWN_WALL, LEFT_REFERENCE, LEFT_WALL, BOTTOM_PART);
}

void setBackWallReferences()
{
    setReference(BACK_WALL, TOP_REFERENCE, UP_WALL, TOP_PART);
    setReference(BACK_WALL, LEFT_REFERENCE, RIGHT_WALL, RIGHT_PART);
    setReference(BACK_WALL, BOTTOM_REFERENCE, DOWN_WALL, BOTTOM_PART);
    setReference(BACK_WALL, RIGHT_REFERENCE, LEFT_WALL, LEFT_PART);
}

void setLeftWallReferences()
{
    setReference(LEFT_WALL, TOP_REFERENCE, UP_WALL, LEFT_PART);
    setReference(LEFT_WALL, RIGHT_REFERENCE, FRONT_WALL, LEFT_PART);
    setReference(LEFT_WALL, BOTTOM_REFERENCE, DOWN_WALL, LEFT_PART);
    setReference(LEFT_WALL, LEFT_REFERENCE, BACK_WALL, RIGHT_PART);
}

void initializeCube()
{
    for (int wallIndex = 0; wallIndex < 6; wallIndex++)
    {
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                walls[wallIndex][i][j] = wallIndex;
            }
        }
    }
    setUpWallReferences();
    setFrontWallReferences();
    setRightWallReferences();
    setDownWallReferences();
    setBackWallReferences();
    setLeftWallReferences();
}

void printRowOfOffset()
{
    const int BORDER_SIZE = 1;

    for (int column = 0; column < N + BORDER_SIZE; column++)
    {
        putchar(' ');
    }
}

char numberToChar(int num)
{
    return (char)(num + '0');
}

void printWallRow(int wallIndex, int row)
{
    for (int column = 0; column < N; column++)
    {
        putchar(numberToChar(walls[wallIndex][row][column]));
    }
}

void printSchemeEdges(int wallIndex)
{
    for (int row = 0; row < N; row++)
    {
        printRowOfOffset();
        printWallRow(wallIndex, row);
        putchar('\n');
    }
}

void printSchemeMiddle()
{
    for (int row = 0; row < N; row++)
    {
        printWallRow(LEFT_WALL, row);
        putchar('|');
        printWallRow(FRONT_WALL, row);
        putchar('|');
        printWallRow(RIGHT_WALL, row);
        putchar('|');
        printWallRow(BACK_WALL, row);
        putchar('\n');
    }
}

void presentCube()
{
    printf("\n");
    printSchemeEdges(UP_WALL);
    printSchemeMiddle();
    printSchemeEdges(DOWN_WALL);
}

void copyWall(int wallIndex, int copy[][N])
{
    for (int i = 0; i < N; i++)
    {
        for (int j = 0; j < N; j++)
        {
            copy[i][j] = walls[wallIndex][i][j];
        }
    }
}

void rotateCenter(int wallIndex, char rotation)
{
    int copy[N][N];
    copyWall(wallIndex, copy);

    for (int i = 0; i < N; i++)
    {
        for (int j = 0; j < N; j++)
        {
            switch (rotation)
            {
            case CLOCKWISE_ROTATION:
                walls[wallIndex][i][N - 1 - j] = copy[j][i];
                break;
            case TWICE_CLOCKWISE_ROTATION:
                walls[wallIndex][i][j] = copy[N - 1 - i][N - 1 - j];
                break;
            case COUNTER_CLOCKWISE_ROTATION:
                walls[wallIndex][i][N - 1 - j] = copy[N - 1 - j][N - 1 - i];
                break;
            }
        }
    }
}

void setEdgePiece(int index, int depth, int value, int reference[])
{
    switch (reference[1])
    {
    case TOP_PART:
        walls[reference[0]][depth - 1][N - 1 - index] = value;
        break;
    case BOTTOM_PART:
        walls[reference[0]][N - depth][index] = value;
        break;
    case RIGHT_PART:
        walls[reference[0]][N - 1 - index][N - depth] = value;
        break;
    case LEFT_PART:
        walls[reference[0]][index][depth - 1] = value;
        break;
    }
}

int getEdgePiece(int index, int depth, int reference[])
{
    switch (reference[1])
    {
    case TOP_PART:
        return walls[reference[0]][depth - 1][N - 1 - index];
    case BOTTOM_PART:
        return walls[reference[0]][N - depth][index];
    case RIGHT_PART:
        return walls[reference[0]][N - 1 - index][N - depth];
    case LEFT_PART:
        return walls[reference[0]][index][depth - 1];
    }
    return FAILURE;
}

void rotateClockwise(int wallIndex, int depth)
{
    for (int currentDepth = 1; currentDepth <= depth; currentDepth++)
    {
        for (int i = 0; i < N; i++)
        {
            int top = getEdgePiece(i, currentDepth, wallReferences[wallIndex][TOP_REFERENCE]);
            int right = getEdgePiece(i, currentDepth, wallReferences[wallIndex][RIGHT_REFERENCE]);
            int bottom = getEdgePiece(i, currentDepth, wallReferences[wallIndex][BOTTOM_REFERENCE]);
            int left = getEdgePiece(i, currentDepth, wallReferences[wallIndex][LEFT_REFERENCE]);

            setEdgePiece(i, currentDepth, bottom, wallReferences[wallIndex][LEFT_REFERENCE]);
            setEdgePiece(i, currentDepth, right, wallReferences[wallIndex][BOTTOM_REFERENCE]);
            setEdgePiece(i, currentDepth, top, wallReferences[wallIndex][RIGHT_REFERENCE]);
            setEdgePiece(i, currentDepth, left, wallReferences[wallIndex][TOP_REFERENCE]);
        }
    }
    rotateCenter(wallIndex, CLOCKWISE_ROTATION);
    if (depth == N)
    {
        rotateCenter(oppositeWalls[wallIndex], COUNTER_CLOCKWISE_ROTATION);
    }
}

void rotateCounterClockwise(int wallIndex, int depth)
{
    for (int currentDepth = 1; currentDepth <= depth; currentDepth++)
    {
        for (int i = 0; i < N; i++)
        {
            int top = getEdgePiece(i, currentDepth, wallReferences[wallIndex][TOP_REFERENCE]);
            int left = getEdgePiece(i, currentDepth, wallReferences[wallIndex][LEFT_REFERENCE]);
            int bottom = getEdgePiece(i, currentDepth, wallReferences[wallIndex][BOTTOM_REFERENCE]);
            int right = getEdgePiece(i, currentDepth, wallReferences[wallIndex][RIGHT_REFERENCE]);

            setEdgePiece(i, currentDepth, bottom, wallReferences[wallIndex][RIGHT_REFERENCE]);
            setEdgePiece(i, currentDepth, left, wallReferences[wallIndex][BOTTOM_REFERENCE]);
            setEdgePiece(i, currentDepth, top, wallReferences[wallIndex][LEFT_REFERENCE]);
            setEdgePiece(i, currentDepth, right, wallReferences[wallIndex][TOP_REFERENCE]);
        }
    }
    rotateCenter(wallIndex, COUNTER_CLOCKWISE_ROTATION);
    if (depth == N)
    {
        rotateCenter(oppositeWalls[wallIndex], CLOCKWISE_ROTATION);
    }
}

void rotateTwiceClockwise(int wallIndex, int depth)
{
    for (int currentDepth = 1; currentDepth <= depth; currentDepth++)
    {
        for (int i = 0; i < N; i++)
        {
            int top = getEdgePiece(i, currentDepth, wallReferences[wallIndex][TOP_REFERENCE]);
            int left = getEdgePiece(i, currentDepth, wallReferences[wallIndex][LEFT_REFERENCE]);
            int bottom = getEdgePiece(i, currentDepth, wallReferences[wallIndex][BOTTOM_REFERENCE]);
            int right = getEdgePiece(i, currentDepth, wallReferences[wallIndex][RIGHT_REFERENCE]);

            setEdgePiece(i, currentDepth, bottom, wallReferences[wallIndex][TOP_REFERENCE]);
            setEdgePiece(i, currentDepth, top, wallReferences[wallIndex][BOTTOM_REFERENCE]);
            setEdgePiece(i, currentDepth, right, wallReferences[wallIndex][LEFT_REFERENCE]);
            setEdgePiece(i, currentDepth, left, wallReferences[wallIndex][RIGHT_REFERENCE]);
        }
    }
    rotateCenter(wallIndex, TWICE_CLOCKWISE_ROTATION);
    if (depth == N)
    {
        rotateCenter(oppositeWalls[wallIndex], TWICE_CLOCKWISE_ROTATION);
    }
}

int convertWallCodeToIndex(int wallCode)
{
    switch (wallCode)
    {
    case 'u':
        return UP_WALL;
    case 'l':
        return LEFT_WALL;
    case 'f':
        return FRONT_WALL;
    case 'r':
        return RIGHT_WALL;
    case 'b':
        return BACK_WALL;
    case 'd':
        return DOWN_WALL;
    }
    return FAILURE;
}

bool isWallCode(int wallCode)
{
    return convertWallCodeToIndex(wallCode) != FAILURE;
}

bool isNumber(int c)
{
    const int ZERO_ASCII_CODE = 48;
    const int NINE_ASCII_CODE = 57;

    return ZERO_ASCII_CODE <= c &&
           c <= NINE_ASCII_CODE;
}

int charToNumber(int c)
{
    return c - '0';
}

void tryReadRotation(int *rotation)
{
    int possibleRotation = getchar();
    if (possibleRotation == COUNTER_CLOCKWISE_ROTATION || possibleRotation == TWICE_CLOCKWISE_ROTATION)
    {
        *rotation = possibleRotation;
    }
    ungetc(possibleRotation, stdin);
}

void rotate(int wallIndex, int depth, int rotation)
{
    switch (rotation)
    {
    case COUNTER_CLOCKWISE_ROTATION:
        rotateCounterClockwise(wallIndex, depth);
        break;
    case TWICE_CLOCKWISE_ROTATION:
        rotateTwiceClockwise(wallIndex, depth);
        break;
    case CLOCKWISE_ROTATION:
        rotateClockwise(wallIndex, depth);
        break;
    }
}

int readDepth()
{
    char numberAsDigits[N];
    int index = 0;
    int currentDigit = getchar();
    while (isNumber(currentDigit))
    {
        numberAsDigits[index] = (char)currentDigit;
        index++;
        currentDigit = getchar();
    }
    ungetc(currentDigit, stdin);

    int multiplier = 1;
    int num = 0;
    for (int i = index - 1; i >= 0; i--)
    {
        num += charToNumber(numberAsDigits[i]) * multiplier;
        multiplier *= 10;
    }
    return num;
}

int main()
{
    initializeCube();

    int currentChar = getchar();
    while (currentChar != '.' && currentChar != EOF)
    {
        int wallIndex;
        int depth = 1;
        int rotation = CLOCKWISE_ROTATION;
        if (isNumber(currentChar))
        {
            ungetc(currentChar, stdin);
            depth = readDepth();
            wallIndex = convertWallCodeToIndex(getchar());
            tryReadRotation(&rotation);
            rotate(wallIndex, depth, rotation);
        }
        else if (isWallCode(currentChar))
        {
            wallIndex = convertWallCodeToIndex(currentChar);
            tryReadRotation(&rotation);
            rotate(wallIndex, depth, rotation);
        }
        else if (currentChar == '\n')
        {
            presentCube();
        }
        currentChar = getchar();
    }
}