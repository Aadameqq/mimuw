package lib.zakłady;

import lotto.zakłady.Zakład;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ZakładTest
{
    @ParameterizedTest
    @MethodSource("toStringDane")
    void toStringPowinnoPrawidłowoWyświetlaćLiczby(Set<Integer> liczby, String oczekiwany)
    {
        Zakład zakład = new Zakład(liczby);
        assertEquals(oczekiwany, zakład.toString());
    }

    static Stream<Arguments> toStringDane()
    {
        return Stream.of(
                Arguments.of(Set.of(2, 4, 6, 8, 10, 12), " 2  4  6  8 10 12 "),
                Arguments.of(Set.of(10, 12, 14, 16, 18, 20), "10 12 14 16 18 20 ")
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 7, 10})
    void toStringPowinnoZwracaćWyjątekGdyNieprawidłowaIlośćLiczb(int ilość)
    {
        Set<Integer> liczby = new HashSet<>();
        for (int i = 1; i <= ilość; i++)
        {
            liczby.add(i);
        }

        assertThrows(IllegalArgumentException.class, () -> new Zakład(liczby));
    }
}
