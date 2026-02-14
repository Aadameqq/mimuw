global nsqrt
section .text

; rdi - wskaźnik na Q
; rsi - wskaźnik na X (używany jako R)
; rdx - wartość n
; void nsqrt(rdi=Q, rsi=X, rdx=n)
nsqrt:
    mov r11, rdx ; r11 będzie przechowywało 2n czyli ilość słów w X oraz T
    shr r11, 5 ; r11 = 2(n/64) = n/32 = n>>5
    dec rdx ; rdx = n-j; początkowo j = 1
    mov r10, r11
    shl r10, 3 ; ilość użytych bajtów stosu, czyli ilość słów 8 bajtowych * 8
    or  r10, 8 ; wyrównanie do liczby bajtów podzielnej przez 8
    sub rsp, r10
clean_t:
    xor rax,rax ; wartość wstawiana do T; później zawiera informację czy q=1
    mov rcx, r11 ; ilość słów
    mov r8, rdi
    mov rdi, rsp ; adres początku tablicy
    rep stosq ; kopiuje do każdego z rcx słów tablicy o początku pod adresem rdi wartość z rax
    mov rdi, r8 ; przywrócenie poprzedniej wartości rdi
add_four: ; dodaje czwórkę na pozycji 2(n-j) dla j=1 czyli 2n-2, która
          ; później będzie cofana przy każdej iteracji
    lea rcx, [rdx + rdx] ; rcx = 2(n-j)
    mov r8, rcx ;
    shr r8, 6 ; r8 = 2(n-j)/64 = indeks słowa w którym jest bit 2(n-j)
    and rcx, 63 ; usuwa wszystko poza pierwszymi 6 bitami - reszta z dzielenia przez 64 -
                ; zawiera indeks bitu w słowie
    bts qword [rsp+8*r8], rcx ; wstawia 1 na pozycji rcx do T[indeks_słowa]
main_loop: ; pętla dla j=1,...,n, czyli n-j=n-1,...,0
comparison:
    lea r8, [r11 - 1] ; r8 czyli indeks = 2n-1
.outer_loop:
    mov rcx, qword [rsi+r8*8]
    cmp qword [rsp+r8*8],rcx ; porównanie R[indeks] oraz T[indeks]
    jb  .substract ; jeżeli T[indeks] < R[indeks] to q=1, więc przechodzi do odejmowania R[indeks]-T[indeks]
    jne .end ; jeżeli nie są równe, czyli T[indeks] > R[indeks] to q=0 i kończy pętlę
    dec r8
    jns .outer_loop ; jeżeli indeks jest nieujemny to kontynuuje
.substract:
    xor r9, r9 ; r9 czyli indeks = 0
    lea r8, [r11 -1] ; dodatkowy indeks pomagający ominąć nadpisywanie CF przez cmp
    clc ; wyczyszczenie CF
.loop:
    mov rcx, qword [rsp+r9*8]
    sbb qword [rsi + r9*8], rcx ; R[indeks] -= T[indeks] + borrow
    inc r9
    dec r8
    jns .loop ; jeżeli dodatkowy indeks nieujemny to kontynuuje
    xor rax, 1 ; zapisuje, że q=1
.end:
sub_four: ; zmniejsza potęgę 4^(n-j) o 1 czyli dzieli przez 4
    ; jako iż unshift dzieli przez 2 to tu dzieli tylko przez 2
    ; 2^(2n-2j) - 2^(2n-2j-1) = 2^(2n-2j-1)(2-1) = 2^(2n-2j-1)
    lea rcx, [rdx+rdx]
    dec rcx ; rcx = 2(n-j)-1
    js  .end ; w ostatniej iteracji 2(n-j)-1<0. dalsze działanie ma sens tylko, gdy jest kolejna iteracja
    mov r9, rcx
    shr r9, 6 ; r9 = (2(n-j)-1)/64 - indeks słowa w którym jest bit 2(n-j)-1
    and rcx, 63 ; usuwa wszystko poza pierwszymi 6 bitami - reszta z dzielenia przez 64
                ; zawiera względny indeks bitu w słowie
    xor r8, r8
    bts r8, rcx ; ustawia bit na 1 na pozycji rcx
    mov rcx, r11 ; pomocniczy indeks potrzebny do pętli
    sub rcx, r9 ; rcx = 2n - (2n-2j-1)
    sub qword [rsp+8*r9], r8 ; T[indeks_słowa] -= r8 oraz ustawia CF
.loop: ; dla każdego kolejnego słowa obsługujemy borrow z odejmowania
    inc  r9
    dec  rcx ; rcx jest dobrany tak, by pętla wykonała się dla każdego słowa
            ; o indeksach od r9+1 do 2n-1. Jest to potrzebne, gdyż cmp nadpisuje CF, a dec nie
    jz  .end ; jeżeli rcx == 0 to przerywa pętlę
    sbb qword [rsp+8*r9], 0 ; T[indeks_słowa] -= 0 + borrow
    jmp .loop
.end:
unshift:
    clc ; wyczyszczenie CF
    lea r8, [r11 -1] ; r8 czyli indeks = 2n-1
.loop:
    rcr qword [rsp + r8*8], 1 ; przesunięcie o 1 bit z obsługą CF
    dec r8
    jns .loop ; jeżeli indeks nieujemny to kontynuuje
.end:
    cmp rax, 1
    jne post_update_t ; jeżeli q!=1 to pomija edycję T
update_t:
    lea rcx, [rdx+rdx] ; rcx = 2(n-j)
    mov r8, rcx
    shr r8, 6 ; r8 = 2(n-j)/64 = indeks słowa w którym jest bit 2(n-j)
    and rcx, 63 ; usuwa wszystko poza pierwszymi 6 bitami - reszta z dzielenia przez 64 -
                ; zawiera indeks bitu w słowie
    bts qword [rsp+8*r8], rcx ; w słowie T[indeks_słowa] ustawia bit o indeksie rcx na 1
    xor rax, rax ; czyści informację, że q=1
post_update_t:
    dec rdx ; zwiększenie j, czyli zmniejszenie rdx=n-j
    jns main_loop ; jeżeli n-j jest nieujemne, czyli j<=n to kontynuujemy
copy_t: ; kopiuje tablicę T do Q
    shr r11, 1 ; r11 = 2n/2 = n
    mov rcx, r11
    mov rsi, rsp
    rep movsq ; przekleja rcx słów z rsi do rdi
.end:
    add  rsp, r10 ; przywrócenie rejestru
    ret