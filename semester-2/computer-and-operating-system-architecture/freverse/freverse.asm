section .text
global _start
_start:
    cmp         qword [rsp], 2      ; sprawdzenie czy dokładnie 1 argument
    jne         .error
.open_file:
    xor         eax, eax
    mov         al, 2               ; ustawia numer syscalla na 2 (sys_open)
                                    ; sztuczka z xorowaniem i ustawieniem mniejszej części rejestru oszczędza 1 bajt
    mov         rdi, [rsp+16]       ; nazwa pliku (rsp - ilość argumentów, rsp+8 - nazwa pliku z programem, rsp+16 - pierwszy argument)
    mov         esi, 2              ; O_RDWR - odczyt i edycja (bez tworzenia)
    xor         rdx, rdx            ; (ma znaczenie tylko przy tworzeniu)
    syscall
    test        rax, rax            ; jeżeli błąd (np. plik nie istnieje)
    js          .error              ; to skacze do .error
    sub         rsp, 160            ; zarezerwowana przestrzeń na strukturę zwracaną przez fstat + wyrównanie
.check_file_size:
    mov         rsi, rsp            ; wskaźnik na stos, gdzie znajdzie się struct
    mov         rdi, rax            ; fd zwrócony przez sys_open
    xor         eax, eax
    mov         al, 5               ; nr syscalla fstat
    syscall
    test        rax, rax
    js          .error

    mov         eax, [rsp+24]       ; zawiera typ pliku - sprawdzenie czy zwykły plik
    shr         eax, 12             ; usuwa bity które informują np. o prawach dostępu
    cmp         al, 8               ; 32768 >> 12 = 8, gdzie 32768 oznacza, że typ pliku to zwykły
    jne         .error              ; jeżeli plik nie jest zwykły, (np. /dev/random) to przechodzi do obsługi błędu
.mmap:
    mov         rsi, [rsp+48]       ; zawiera rozmiar pliku
    cmp         rsi, 1
    jna         .exit               ; jeżeli rozmiar pliku <=1, to kończymy działanie
    xor         eax, eax
    mov         al, 9               ; nr syscalla sys_mmap
    mov         r8, rdi             ; fd
    xor         edi, edi            ; preferowany adres - ustawienie na zero oznacza, że wybór automatyczny
    mov         edx, 3              ; odczyt i zapis, czyli 2 | 1 = 3
    mov         r10d, 1
    xor         r9d,r9d             ; offset = 0
    syscall
    test        rax, rax
    js          .error
.change_data:
    lea         r10, [rax+rsi]      ; wskaźnik końcowy (koniec - 8 bajtów)
    mov         r9, rax             ; wskaźnik początkowy
    mov         r11, rsi            ;
    shr         r11, 4              ; ilość par ośmiobajtowych bloków w pliku
.faster_loop:                       ; szybsza pętla odwraca po 8 bajtów na raz
    dec         r11
    js          .loop               ; jeżeli skończyły się wszystkie pary ośmiobajtowych bloków to przechodzi do pętli po bajcie
    sub         r10,  8             ; przesuwa końcowy wskaźnik
    mov         rdx, qword [r9]
    mov         rcx, qword [r10]
    bswap       rdx                 ; odwraca kolejność bajtów w rejestrze
    bswap       rcx
    mov         qword [r9], rcx
    mov         qword [r10],  rdx
    add         r9, 8               ; przesuwa początkowy wskaźnik
    jmp         .faster_loop
.loop:
    dec         r10                 ; przesuwa końcowy wskaźnik o bajt
    cmp         r9, r10
    jnb         .loop_end           ; jeżeli początkowy wskaźnik przeskoczył przed końcowy to koniec odwracania
    mov         r12b, byte [r9]
    mov         r11b, byte [r10]
    mov         byte [r9], r11b
    mov         byte [r10], r12b
    inc         r9                  ; przesuwa początkowy wskaźnik o bajt
    jmp         .loop
.loop_end:
.msync:
    mov         rdi, rax            ; adres początku zmapowanej przestrzeni
                                    ; rsi z wcześniej ma już length
    mov         edx, 1              ; zapis asynchroniczny
    xor         eax, eax
    mov         al, 26              ; nr syscalla sys_msync
    syscall
    xor         r10, r10
    test        rax, rax
    jns         .munmap
    mov         r10, 1
.munmap:
    xor         eax, eax
    mov         al, 11              ; nr syscalla sys_munmap
                                    ; rdi z wcześniej ma luż adres, a rsi length
    syscall
    test        rax, rax
    jns          .close_file
.error:
    mov         r10, 1
    jmp         .close_file
.exit:
    mov         r10, 0
.close_file:
    add         rsp, 160            ; wyczyszczenie stosu
    mov         rdi, r8             ; fd
    xor         eax, eax
    mov         al, 3               ; nr syscalla sys_close
    syscall
    test        rax, rax
    jns         .sys_exit
    mov         r10, 1             ; ustawienie kodu wyjścia na 1
.sys_exit:
    mov         rdi, r10           ; kod wyjścia
    xor         eax, eax
    mov         al, 60             ; numer sys_calla sys_exit
    syscall