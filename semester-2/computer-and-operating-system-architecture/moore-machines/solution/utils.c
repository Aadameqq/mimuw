#include <errno.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include "ma.h"
#include "structs.h"
#include "utils.h"

/**
 * Funkcja aktualizująca wyjście automatu.
 */
void update_output(moore_t *a)
{
    a->output_function(a->output, a->state, a->output_size, a->state_size);
}

/**
 * Funkcja zwracająca mniejszą z dwóch liczb a i b.
 */
int min(int a, int b)
{
    if (a < b)
    {
        return a;
    }
    return b;
}

/**
 * Funkcja obliczająca liczbę 64-bitowych bloków potrzebnych
 * do przechowania danej liczby bitów (bits).
 */
size_t calc_parts_amount(const size_t bits)
{
    if (bits == 0)
        return 0;

    if (bits % BITS_IN_PART == 0)
    {
        return bits / BITS_IN_PART;
    }
    return bits / BITS_IN_PART + 1;
}

/**
 * Funkcja, która przepisuje tablicę 64-bitowych wartości
 * lub zeruje ją, jeżeli new jest wskaźnikiem na NULL.
 */
void override_int_array(uint64_t *arr, const size_t length, uint64_t const *new)
{
    for (
        size_t part_index = 0;
        part_index < calc_parts_amount(length);
        part_index++
    )
    {
        if (new == NULL)
        {
            arr[part_index] = 0;
        } else
        {
            arr[part_index] = new[part_index];
        }
    }
}

/**
 * Funkcja alokująca tablicę o długości potrzebnej do przechowania zadanej liczby bitów.
 * Funkcja dodatkowo inicjuje tą tablicę zerami.
 * Zwraca wskaźnik do zaalokowanej tablicy lub NULL w razie błędu.
 */
uint64_t *init_with_arr(size_t absolute_size)
{
    uint64_t *created = (uint64_t *) malloc(
        calc_parts_amount(absolute_size) * sizeof(uint64_t)
    );

    if (created == NULL)
    {
        errno = ENOMEM;
        return NULL;
    }
    override_int_array(created, absolute_size, NULL);
    return created;
}

/**
 * Funkcja pobierająca bit o indeksie (bit) z zmiennej (*part).
 * Zwraca 0 lub 1.
 */
int get_bit_from_part(uint64_t const *part, const int bit)
{
    return ((*part) & ((uint64_t) (1) << bit)) == 0 ? 0 : 1;
}

/**
 * Funkcja ustawiająca bit (bit) w 64-bitowej zmiennej (*part) na wskazaną wartość.
 */
void set_bit_for_part(uint64_t *part, int bit, int new_val)
{
    const int curr_val = get_bit_from_part(part, bit);

    const uint64_t new_bit = ((uint64_t) (1) << bit);

    if (curr_val == new_val)
    {
        return;
    }

    if (curr_val == 0)
    {
        *part = (*part) | new_bit;
    }

    if (curr_val == 1)
    {
        *part = (*part) ^ new_bit;
    }
}
