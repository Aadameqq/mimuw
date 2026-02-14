#ifndef STRUCTS_H
#define STRUCTS_H
#define BITS_IN_PART 64
#include "ma.h"

typedef struct in_connection
{
    moore_t *output;
    size_t output_bit;
    size_t input_bit;
    size_t out_con_index;
} in_connection;

typedef struct out_connection
{
    moore_t *input;
    size_t input_bit;
} out_connection;

typedef struct out_connections
{
    out_connection **cons;
    size_t size;
    long int last_index;
} out_connections;


typedef struct moore
{
    uint64_t *input;
    size_t input_size;
    uint64_t *output;
    size_t output_size;
    uint64_t *state;
    size_t state_size;
    out_connections *output_connections;
    in_connection **input_connections;
    transition_function_t transition_function;
    output_function_t output_function;
} moore_t;

#endif // STRUCTS_H
