package lib.finanse;

import lotto.finanse.Pieniądze;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PieniądzeTest
{
    @Test
    void testTworzeniaPowinnoZrwócićWyjątekGdyPrzekazanoUjemnąWartość()
    {
        assertThrows(IllegalArgumentException.class, () -> Pieniądze.daj(-20));
        assertThrows(IllegalArgumentException.class, () -> Pieniądze.daj(1, -20));
        assertThrows(IllegalArgumentException.class, () -> Pieniądze.daj(-1, -20));
    }

    @Test
    void testSumyPowinnoZwrócićPrawidłowąSumę()
    {
        Pieniądze a = Pieniądze.daj(1, 48);
        Pieniądze b = Pieniądze.daj(5, 96);

        assertEquals(Pieniądze.daj(7, 44), a.suma(b));
    }

    @Test
    void testRóżnicyPowinnoZwrócićWyjątekGdyWynikJestUjemny()
    {
        Pieniądze a = Pieniądze.daj(1, 48);
        Pieniądze b = Pieniądze.daj(5, 96);

        assertThrows(IllegalArgumentException.class, () -> a.różnica(b));
    }

    @Test
    void testRóżnicyPowinnoZwrócićPrawidłowąRóżnicę()
    {
        Pieniądze a = Pieniądze.daj(5, 25);
        Pieniądze b = Pieniądze.daj(1, 48);

        assertEquals(Pieniądze.daj(3, 77), a.różnica(b));

        Pieniądze c = Pieniądze.daj(2, 1);
        Pieniądze d = Pieniądze.daj(1, 48);

        assertEquals(Pieniądze.daj(0, 53), c.różnica(d));
    }

    @Test
    void testRóżnicyPowinnoZwrócićZeroGdyWynikJestRównyZero()
    {
        Pieniądze a = Pieniądze.daj(1, 48);
        Pieniądze b = Pieniądze.daj(1, 48);

        assertEquals(Pieniądze.daj(0, 0), a.różnica(b));
    }

    @Test
    void testIloczynuPowinnoZwrócićPrawidłowąWartość()
    {
        Pieniądze a = Pieniądze.daj(1, 48);
        Pieniądze b = Pieniądze.daj(5, 99);

        assertEquals(Pieniądze.daj(2, 96), a.iloczyn(2));
        assertEquals(Pieniądze.daj(17, 97), b.iloczyn(3));
    }

    @Test
    void testProcentuPowinnoZaokrąglićWDół()
    {
        Pieniądze a = Pieniądze.daj(19, 99);

        assertEquals(Pieniądze.daj(0, 99), a.procent(5));
        assertEquals(Pieniądze.daj(3, 79), a.procent(19));
    }

    @Test
    void testDzieleniaPowinnoZaokrąglićWDół()
    {
        Pieniądze a = Pieniądze.daj(19, 99);

        assertEquals(Pieniądze.daj(0, 86), a.podzieloneNa(23));
        assertEquals(Pieniądze.daj(2, 49), a.podzieloneNa(8));
    }

    @Test
    void testDzieleniaZwrócićWyjątekGdyPrzekazanoLiczbęNiedodatnią()
    {
        Pieniądze a = Pieniądze.daj(19, 99);

        assertThrows(ArithmeticException.class, () -> a.podzieloneNa(0));
        assertThrows(ArithmeticException.class, () -> a.podzieloneNa(-3));
    }

    @Test
    void testToString()
    {
        Pieniądze a = Pieniądze.daj(243, 48);
        Pieniądze b = Pieniądze.daj(0, 99);
        Pieniądze c = Pieniądze.daj(12);

        assertEquals("243 zł 48 gr", a.toString());
        assertEquals("0 zł 99 gr", b.toString());
        assertEquals("12 zł", c.toString());
    }

    @ParameterizedTest
    @MethodSource("compareToDane")
    void testCompareTo(Pieniądze a, Pieniądze b, int oczekiwane)
    {
        assertEquals(oczekiwane, a.compareTo(b));
    }

    static Stream<Arguments> compareToDane()
    {
        return Stream.of(
                Arguments.of(Pieniądze.daj(1, 48), Pieniądze.daj(1, 48), 0),
                Arguments.of(Pieniądze.daj(2, 0), Pieniądze.daj(1, 99), 1),
                Arguments.of(Pieniądze.daj(1, 99), Pieniądze.daj(2, 0), -1),
                Arguments.of(Pieniądze.daj(0, 0), Pieniądze.daj(0, 0), 0),
                Arguments.of(Pieniądze.daj(5, 0), Pieniądze.daj(4, 99), 1),
                Arguments.of(Pieniądze.daj(3, 50), Pieniądze.daj(3, 51), -1)
        );
    }
}
