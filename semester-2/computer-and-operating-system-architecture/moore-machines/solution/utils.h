#ifndef UTILS_H
#define UTILS_H
#include "ma.h"

void update_output(moore_t *a);

int min(int a, int b);

size_t calc_parts_amount(const size_t bits);

void override_int_array(uint64_t *arr, size_t length, uint64_t const *new);

uint64_t *init_with_arr(size_t absolute_size);

int get_bit_from_part(uint64_t const *part, const int bit);

void set_bit_for_part(uint64_t *part, int bit, int new_val);
#endif // UTILS_H
