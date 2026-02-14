// Program symulujący Automaty Moore'a

#include "memory_tests.h"
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include "ma.h"
#include "structs.h"
#include "utils.h"

/**
 * Funkcja tworząca i inicjalizująca strukturę moore_t.
 * Przyjmuje rozmiar wejścia (n), rozmiar stanu (s), rozmiar wyjścia (m),
 * funkcję przejść (t), funkcję wyjść (y) oraz wskaźnik na ciąg bitów reprezentujący początkowy stan automatu (q).
 * Jeżeli (q) jest NULL'em, stan jest inicjowany zerami.
 * Zwraca wskaźnik do utworzonej struktury lub NULL w razie błędu.
 */
moore_t *init(
    const size_t input_size, const size_t output_size, const size_t state_size,
    const transition_function_t transition_function,
    const output_function_t output_function, uint64_t const *state)
{
    if (transition_function == NULL || output_function == NULL)
    {
        errno = EINVAL;
        return NULL;
    }

    if (output_size == 0 || state_size == 0)
    {
        errno = EINVAL;
        return NULL;
    }

    moore_t *new = (moore_t *)malloc(sizeof(moore_t));

    if (new == NULL)
    {
        errno = ENOMEM;
        return NULL;
    }

    new->transition_function = transition_function;
    new->output_function = output_function;

    new->input_size = input_size;
    new->state_size = state_size;
    new->output_size = output_size;

    new->state = init_with_arr(new->state_size);
    if (new->state == NULL)
    {
        free(new);
        return NULL;
    }

    override_int_array(new->state, new->state_size, state);

    if (input_size > 0)
    {
        new->input = init_with_arr(new->input_size);
        if (new->input == NULL)
        {
            free(new->state);
            free(new);
            return NULL;
        }
    }
    else
    {
        new->input = NULL;
    }

    new->output = init_with_arr(new->output_size);
    if (new->output == NULL)
    {
        free(new->state);
        free(new->input);
        free(new);
        return NULL;
    }

    if (input_size > 0)
    {
        new->input_connections = (in_connection **)malloc(
            new->input_size * sizeof(in_connection *));

        if (new->input_connections == NULL)
        {
            errno = ENOMEM;
            free(new->state);
            free(new->input);
            free(new->output);
            free(new);
            return NULL;
        }

        for (size_t i = 0; i < new->input_size; i++)
        {
            new->input_connections[i] = NULL;
        }
    }
    else
    {
        new->input_connections = NULL;
    }

    new->output_connections = (out_connections *)malloc(
        sizeof(out_connections));

    if (new->output_connections == NULL)
    {
        free(new->state);
        free(new->input_connections);
        free(new->input);
        free(new->output);
        free(new);
        errno = ENOMEM;
        return NULL;
    }
    new->output_connections->last_index = -1;
    new->output_connections->size = 2;

    new->output_connections->cons = (out_connection **)malloc(
        new->output_connections->size * sizeof(out_connection *));

    if (new->output_connections->cons == NULL)
    {
        free(new->state);
        free(new->input_connections);
        free(new->input);
        free(new->output);
        free(new->output_connections);
        free(new);
        errno = ENOMEM;
        return NULL;
    }

    for (size_t i = 0; i < new->output_connections->size; i++)
    {
        new->output_connections->cons[i] = NULL;
    }

    update_output(new);

    return new;
}

/**
 * Funkcja tworząca automat.
 * Przyjmuje rozmiar wejścia (n), rozmiar stanu (s), rozmiar wyjścia (m),
 * funkcję przejść (t), funkcję wyjść (y) oraz wskaźnik na ciąg bitów reprezentujący początkowy stan automatu (q).
 * Zwraca wskaźnik do nowej maszyny lub NULL w razie błędu.
 */
moore_t *ma_create_full(
    size_t n, size_t m, size_t s,
    transition_function_t t,
    output_function_t y, uint64_t const *q)
{
    if (q == NULL)
    {
        errno = EINVAL;
        return NULL;
    }

    moore_t *new = init(n, m, s, t, y, q);

    if (new == NULL)
    {
        return NULL;
    }

    return new;
}

/*
 * Funkcja identyczności używana w automacie prostym
 */
static void simple_output_func(
    uint64_t *output, uint64_t const *state,
    size_t m, size_t)
{
    for (size_t i = 0; i < calc_parts_amount(m); i++)
    {
        output[i] = state[i];
    }
}

/**
 * Funkcja tworząca automat, w którym funkcja wyjść jest identycznością.
 * Przyjmuje rozmiar wejścia (n), rozmiar stanu (s) oraz funkcję przejść (t).
 * Zwraca wskaźnik do nowej maszyny lub NULL w razie błędu.
 */
moore_t *ma_create_simple(size_t n, size_t s, transition_function_t t)
{
    return init(n, s, s, t, simple_output_func, NULL);
}

/**
 * Funkcja zwracająca wskaźnik do wyjścia automatu.
 * Przyjmuje wskaźnik na automat.
 * Zwraca NULL i ustawia errno na EINVAL, jeśli wskaźnik do automatu jest NULL'em.
 */
uint64_t const *ma_get_output(moore_t const *a)
{
    if (a == NULL)
    {
        errno = EINVAL;
        return NULL;
    }
    return a->output;
}

/**
 * Funkcja ustawiająca wejście automatu na wartości podane w tablicy (input) z pominięciem podłączonych bitów.
 * Przyjmuje wskaźnik na automat oraz wskaźnik na tablicę z nowymi wartościami wejścia.
 * Zwraca 0 w razie sukcesu, -1 w przypadku błędu i ustawia errno = EINVAL.
 */
int ma_set_input(moore_t *a, uint64_t const *input)
{
    if (a == NULL || input == NULL || a->input_size == 0)
    {
        errno = EINVAL;
        return -1;
    }

    for (
        size_t part_index = 0;
        part_index < calc_parts_amount(a->input_size);
        part_index++)
    {
        uint64_t const *new_part = &input[part_index];
        uint64_t *input_part = &a->input[part_index];

        int bits_count = a->input_size - part_index * BITS_IN_PART;

        for (int i = 0; i < min(bits_count, BITS_IN_PART); i++)
        {
            if (a->input_connections[part_index * BITS_IN_PART + i] != NULL)
            {
                continue;
            }
            set_bit_for_part(input_part, i, get_bit_from_part(new_part, i));
        }
    }

    return 0;
}

/**
 * Funkcja wykonująca pojedynczy krok automatów.
 * Najpierw uzupełnia wejścia na podstawie połączeń, a następnie aktualizuje stany.
 * Przyjmuje tablicę automatów oraz długość owej tablicy.
 * Zwraca 0 w razie powodzenia,a w przypadku błędu -1 oraz ustawia errno = EINVAL lub ENOMEM.
 */
int ma_step(moore_t *at[], size_t num)
{
    if (at == NULL || num == 0)
    {
        errno = EINVAL;
        return -1;
    }
    for (size_t i = 0; i < num; i++)
    {
        const moore_t *curr = at[i];

        if (curr == NULL)
        {
            errno = EINVAL;
            return -1;
        }

        for (size_t bit = 0; bit < curr->input_size; bit++)
        {
            in_connection *con = curr->input_connections[bit];
            if (con == NULL)
            {
                continue;
            }

            const int in_part_index = con->input_bit / BITS_IN_PART;
            const int in_rel_bit = con->input_bit % BITS_IN_PART;

            const int out_part_index = con->output_bit / BITS_IN_PART;
            const int out_rel_bit = con->output_bit % BITS_IN_PART;

            const int val = get_bit_from_part(
                &con->output->output[out_part_index], out_rel_bit);

            set_bit_for_part(&curr->input[in_part_index], in_rel_bit, val);
        }
    }
    for (size_t i = 0; i < num; i++)
    {
        moore_t *curr = at[i];
        uint64_t *next = init_with_arr(curr->state_size);
        if (next == NULL)
        {
            return -1;
        }

        curr->transition_function(
            next, curr->input, curr->state, curr->input_size, curr->state_size);
        free(curr->state);
        curr->state = next;
        update_output(curr);
    }
    return 0;
}

/**
 * Funkcja zmieniająca stan maszyny.
 * Podana tablica z nową wartością jest kopiowana.
 * Przyjmuje wskaźnik na automat oraz tablicę z nowym stanem.
 * Zwraca 0 w razie sukcesu,a jeśli argumenty są niepoprawne -1 i ustawia errno = EINVAL.
 */
int ma_set_state(moore_t *a, uint64_t const *state)
{
    if (a == NULL || state == NULL)
    {
        errno = EINVAL;
        return -1;
    }
    override_int_array(a->state, a->state_size, state);
    update_output(a);
    return 0;
}

/**
 * Funkcja dodająca nowe połączenie wyjścia (a_out) z wejściem (a_in)
 * na konkretnym bicie.
 * Zwraca indeks nowo dodanego połączenia w tablicy połączeń wyjściowych (a_out)
 * lub -1 w razie błędu.
 */
long int add_out_connection(
    const moore_t *a_out, const size_t input_bit,
    moore_t *a_in)
{
    out_connections *out_cons = a_out->output_connections;

    if (out_cons->last_index + 1 >= (long int)out_cons->size)
    {
        out_cons->size *= 2;
        out_cons->cons = (out_connection **)realloc(
            out_cons->cons, out_cons->size * sizeof(out_connection *));

        if (out_cons->cons == NULL)
        {
            errno = ENOMEM;
            return -1;
        }
    }
    out_cons->last_index++;
    const long int add_index = out_cons->last_index;

    out_connection *out_con = (out_connection *)malloc(sizeof(out_connection));
    if (out_con == NULL)
    {
        errno = ENOMEM;
        return -1;
    }
    out_con->input = a_in;
    out_con->input_bit = input_bit;

    out_cons->cons[add_index] = out_con;

    return add_index;
}

/**
 * Funkcja łącząca ciąg bitów o danej długości między danymi automatami,
 * rozpoczynając od wskazanych indeksów.
 * Jeżeli na wejściowych bitach istnieją już połączenia, są one usuwane.
 * Zwraca 0 w razie sukcesu, lub -1 (ustawiając errno) w razie niepowodzenia.
 */
int ma_connect(moore_t *a_in, size_t in, moore_t *a_out, size_t out, size_t num)
{
    if (a_in == NULL || a_out == NULL || num == 0)
    {
        errno = EINVAL;
        return -1;
    }

    if (in + num > a_in->input_size || out + num > a_out->output_size)
    {
        errno = EINVAL;
        return -1;
    }

    const int result = ma_disconnect(a_in, in, num);

    if (result == -1)
    {
        return -1;
    }

    for (size_t i = 0; i < num; i++)
    {
        in_connection *con = (in_connection *)malloc(sizeof(in_connection));

        if (con == NULL)
        {
            ma_disconnect(a_in, in, num);
            errno = ENOMEM;
            return -1;
        }

        con->output = a_out;
        con->output_bit = i + out;
        con->input_bit = i + in;

        const long int second_result = add_out_connection(
            a_out, con->input_bit, a_in);

        if (second_result == -1)
        {
            ma_disconnect(a_in, in, num);
            return -1;
        }

        con->out_con_index = (size_t)second_result;

        a_in->input_connections[con->input_bit] = con;
    }

    return 0;
}

/**
 * Funkcja rozłączająca ciąg bitów wejścia o danej długości w wskazanym automacie.
 * Przyjmuje wskaźnik na automat, indeks początku ciągu oraz jego długość
 * Zwraca 0 w razie sukcesu lub -1 i ustawia errno = EINVAL, jeśli wystąpi błąd.
 */
int ma_disconnect(moore_t *a_in, size_t in, size_t num)
{
    if (a_in == NULL || num <= 0)
    {
        errno = EINVAL;
        return -1;
    }

    if (in + num > a_in->input_size)
    {
        errno = EINVAL;
        return -1;
    }

    for (size_t i = 0; i < num; i++)
    {
        const in_connection *con = a_in->input_connections[in + i];
        if (con == NULL)
        {
            continue;
        }
        free(con->output->output_connections->cons[con->out_con_index]);
        con->output->output_connections->cons[con->out_con_index] = NULL;

        free(a_in->input_connections[in + i]);
        a_in->input_connections[in + i] = NULL;
    }
    return 0;
}

/**
 * Funkcja zwalniająca zasoby zajęte przez automat.
 * Jeżeli wskaźnik na automat jest NULL'em, nie robi nic.
 */
void ma_delete(moore_t *a)
{
    if (a == NULL)
    {
        return;
    }

    ma_disconnect(a, 0, a->input_size);
    a->transition_function = NULL;
    a->output_function = NULL;
    if (a->input_size > 0)
        free(a->input);
    free(a->output);
    free(a->state);
    out_connections *out_cons = a->output_connections;
    for (long int i = 0; i <= out_cons->last_index; i++)
    {
        if (out_cons->cons[i] != NULL)
        {
            if (out_cons->cons[i]->input->input_connections[out_cons->cons[i]->input_bit] != NULL)
            {
                free(
                    out_cons->cons[i]->input->input_connections[out_cons->cons[i]->input_bit]);
                out_cons->cons[i]->input->input_connections[out_cons->cons[i]->input_bit] =
                    NULL;
            }
            free(out_cons->cons[i]);
            out_cons->cons[i] = NULL;
        }
    }
    free(out_cons->cons);
    if (a->input_size > 0)
        free(a->input_connections);
    a->input_connections = NULL;
    free(a->output_connections);
    a->output_connections = NULL;
    free(a);
}
