CC = gcc

CFLAGS = -Wall -Wextra -Wno-implicit-fallthrough -std=gnu17 -fPIC -O2

LDFLAGS = -shared -Wl,--wrap=malloc -Wl,--wrap=calloc -Wl,--wrap=realloc -Wl,--wrap=reallocarray -Wl,--wrap=free -Wl,--wrap=strdup -Wl,--wrap=strndup

SRCS = ma.c memory_tests.c ma.h memory_tests.h structs.h utils.c utils.h

OBJS = $(SRCS:.c=.o)

.PHONY: all
all: libma.so

libma.so: $(OBJS)
	$(CC) $(LDFLAGS) -o $@ $(OBJS)

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

.PHONY: clean
clean:
	rm -f *.o libma.so