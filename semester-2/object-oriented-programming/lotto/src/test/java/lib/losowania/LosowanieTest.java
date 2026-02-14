package lib.losowania;

import lotto.finanse.Pieniądze;
import lotto.losowania.Losowanie;
import lotto.losowania.Wygrana;
import lotto.zakłady.Zakład;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LosowanieTest
{
    private Losowanie testoweLosowanie;
    private Map<Integer, Wygrana> wygrane = Map.of(
            4, new Wygrana(4, 1, Pieniądze.daj(10), Pieniądze.zero()),
            3, new Wygrana(3, 1, Pieniądze.daj(100), Pieniądze.zero()),
            2, new Wygrana(2, 1, Pieniądze.daj(1000), Pieniądze.zero()),
            1, new Wygrana(1, 1, Pieniądze.daj(10000), Pieniądze.zero())
    );

    @BeforeEach
    void skonfiguruj()
    {
        testoweLosowanie = new Losowanie(1, Set.of(1, 2, 3, 4, 5, 6), wygrane, Pieniądze.zero());
    }

    @ParameterizedTest
    @MethodSource("daneDoTestuMniejNiżTrzyLiczby")
    void obliczStopieńNagrodyPowinienZwrócićZeroGdyZakładTrafiłMniejNiżTrzyLiczby(Set<Integer> liczby)
    {
        Zakład zakład = new Zakład(liczby);
        assertEquals(Pieniądze.zero(), testoweLosowanie.obliczNagrodę(zakład));
    }


    static Stream<Arguments> daneDoTestuMniejNiżTrzyLiczby()
    {
        return Stream.of(
                Arguments.of(Set.of(1, 2, 8, 9, 10, 11)),
                Arguments.of(Set.of(1, 8, 9, 10, 11, 12)),
                Arguments.of(Set.of(8, 9, 10, 11, 12, 13))
        );
    }

    @ParameterizedTest
    @MethodSource("daneDoTestuNagrodyOOdpowiadającymStopniu")
    void obliczStopieńNagrodyPowinienZwrócićKwotęNagrodyOOdpowiadającymStopniu(Set<Integer> liczby, int stopień)
    {
        Zakład zakład = new Zakład(liczby);
        assertEquals(wygrane.get(stopień).pojedyńczaWygrana(), testoweLosowanie.obliczNagrodę(zakład));
    }

    static Stream<Arguments> daneDoTestuNagrodyOOdpowiadającymStopniu()
    {
        return Stream.of(
                Arguments.of(Set.of(1, 2, 3, 4, 5, 6), 1),
                Arguments.of(Set.of(1, 2, 3, 4, 5, 10), 2),
                Arguments.of(Set.of(1, 2, 3, 4, 10, 11), 3),
                Arguments.of(Set.of(1, 2, 3, 10, 11, 12), 4)
        );
    }
}
