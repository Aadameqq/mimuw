#include <stdio.h>

#define TOTAL_CARDS 81
#define TOTAL_ATTRIBUTES 4
#define DEFAULT_CARDS_IN_USE_AMOUNT 12

int cards[TOTAL_CARDS][TOTAL_ATTRIBUTES];

int availableCards[TOTAL_CARDS];
int total = 0;
int currentlyInUse = 0;

int max(int x, int y)
{
    if (x > y)
        return x;
    return y;
}

int min(int x, int y)
{
    if (x < y)
        return x;
    return y;
}

void printCard(int i)
{
    printf(" ");
    for (int j = 0; j < TOTAL_ATTRIBUTES; j++)
    {
        printf("%d", cards[availableCards[i]][j]);
    }
}

void showCardsInUse()
{
    printf("=");
    for (int i = 0; i < currentlyInUse; i++)
    {
        printCard(i);
    }
    printf("\n");
}

int isAttributeEqual(int attrIndex, int i, int j, int k)
{
    return cards[availableCards[i]][attrIndex] == cards[availableCards[j]][attrIndex] &&
           cards[availableCards[i]][attrIndex] == cards[availableCards[k]][attrIndex];
}

int isAttributeDifferent(int attrIndex, int i, int j, int k)
{
    return cards[availableCards[i]][attrIndex] != cards[availableCards[j]][attrIndex] &&
           cards[availableCards[i]][attrIndex] != cards[availableCards[k]][attrIndex] &&
           cards[availableCards[k]][attrIndex] != cards[availableCards[j]][attrIndex];
}

int findCardsForRemoval(int forRemoval[])
{
    int hasFound = 0;

    for (int i = 0; i < currentlyInUse && !hasFound; i++)
    {
        for (int j = i + 1; j < currentlyInUse && !hasFound; j++)
        {
            for (int k = j + 1; k < currentlyInUse && !hasFound; k++)
            {
                int isOk = 1;
                for (int attrIndex = 0; attrIndex < TOTAL_ATTRIBUTES && isOk; attrIndex++)
                {
                    if (!isAttributeDifferent(attrIndex, i, j, k) && !isAttributeEqual(attrIndex, i, j, k))
                    {
                        isOk = 0;
                    }
                }
                if (isOk)
                {
                    forRemoval[0] = i;
                    forRemoval[1] = j;
                    forRemoval[2] = k;
                    hasFound = 1;
                }
            }
        }
    }

    return hasFound;
}

const int DRAW_FAILURE = 0;
const int DRAW_SUCCESS = 1;

int drawCards()
{
    const int CARDS_TO_DRAW = 3;

    int cardsLeftToDraw = total - currentlyInUse;

    if (cardsLeftToDraw < CARDS_TO_DRAW)
    {
        return DRAW_FAILURE;
    }

    currentlyInUse += CARDS_TO_DRAW;
    printf("+\n");
    return DRAW_SUCCESS;
}

void removeCard(int index)
{
    for (int i = index + 1; i < total; i++)
    {
        availableCards[i - 1] = availableCards[i];
    }
    total--;
    if (total < currentlyInUse)
    {
        currentlyInUse--;
    }
}

void removeCards(int cardsForRemoval[])
{
    printf("-");
    for (int i = 0; i < 3; i++)
    {
        printCard(cardsForRemoval[i] - i);
        removeCard(cardsForRemoval[i] - i);
    }
    printf("\n");
}

int isCharACardAttribute(int x)
{
    const int ONE_IN_ASCII = 49;
    const int THREE_IN_ASCII = 51;

    return x >= ONE_IN_ASCII && x <= THREE_IN_ASCII;
}

int charToInt(int x)
{
    return x - 48;
}

int main()
{
    int x = getchar();
    int currentCard = 0;
    int currentAttribute = 0;
    while (x != EOF)
    {
        if (isCharACardAttribute(x))
        {
            cards[currentCard][currentAttribute] = charToInt(x);
            currentAttribute++;
            if (currentAttribute == TOTAL_ATTRIBUTES)
            {
                currentAttribute = 0;
                availableCards[currentCard] = currentCard;
                currentCard++;
            }
        }
        x = getchar();
    }
    total = currentCard;
    currentlyInUse = min(total, DEFAULT_CARDS_IN_USE_AMOUNT);

    int currentlyForRemoval[3];
    int shouldStopGame = 0;
    while (!shouldStopGame)
    {

        showCardsInUse();
        int hasFound = findCardsForRemoval(currentlyForRemoval);
        if (!hasFound)
        {
            int result = drawCards();
            if (result == DRAW_FAILURE)
            {
                shouldStopGame = 1;
            }
        }
        else
        {
            removeCards(currentlyForRemoval);
        }
    }
}