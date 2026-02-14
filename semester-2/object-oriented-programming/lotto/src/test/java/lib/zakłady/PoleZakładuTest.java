package lib.zakłady;

import lotto.zakłady.NieważnePoleZakładu;
import lotto.zakłady.PoleZakładu;
import lotto.zakłady.Zakład;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class PoleZakładuTest
{
    private PoleZakładu testowePoleZakładu;

    @BeforeEach
    void skonfiguruj()
    {
        testowePoleZakładu = new PoleZakładu(1);
    }

    @Test
    void czyAnulowanePowinnoZwrócićFałszPoUtworzeniu()
    {
        assertFalse(testowePoleZakładu.czyAnulowany());
    }

    @Test
    void czyAnulowanePowinnoZwrócićPrawdęGdyAnulowanoPole()
    {
        testowePoleZakładu.anuluj();
        assertTrue(testowePoleZakładu.czyAnulowany());
    }

    @Test
    void czyPoprawnePowinnoZwrócićFałszGdyAnulowanoPole()
    {
        testowePoleZakładu.anuluj();
        assertFalse(testowePoleZakładu.czyPoprawne());
    }

    @Test
    void czyPoprawnePowinnoZwrócićFałszGdyZaznaczonoNiepoprawnąIlośćKratek()
    {
        for (int i = 1; i <= 5; i++)
        {
            testowePoleZakładu.zaznacz(i);
        }

        assertFalse(testowePoleZakładu.czyPoprawne());

        for (int i = 6; i <= 7; i++)
        {
            testowePoleZakładu.zaznacz(i);
        }

        assertFalse(testowePoleZakładu.czyPoprawne());

    }

    @Test
    void zaznaczPowinnoZwrócićWyjątekGdyPodanoNiepoprawnąLiczbę()
    {
        assertThrows(IllegalArgumentException.class, () -> testowePoleZakładu.zaznacz(-1));
        assertThrows(IllegalArgumentException.class, () -> testowePoleZakładu.zaznacz(50));
    }

    @Test
    void zaznaczPowinnoZaznaczyćDanąLiczbę()
    {
        testowePoleZakładu.zaznacz(5);
        assertTrue(testowePoleZakładu.czyZaznaczone(5));
    }

    @Test
    void zaznaczPowinnoZwiększyćIlośćZaznaczonych()
    {
        testowePoleZakładu.zaznacz(5);
        assertEquals(1, testowePoleZakładu.ilośćZaznaczonych());

        testowePoleZakładu.zaznacz(1);
        assertEquals(2, testowePoleZakładu.ilośćZaznaczonych());
    }

    @Test
    void zaznaczPowinnoNieZmieniaćIlościZaznaczonychGdyZaznaczonoLiczbęKtóraByłaJużZaznaczona()
    {
        testowePoleZakładu.zaznacz(5);
        assertEquals(1, testowePoleZakładu.ilośćZaznaczonych());

        testowePoleZakładu.zaznacz(5);
        assertEquals(1, testowePoleZakładu.ilośćZaznaczonych());
    }

    @Test
    void zaznaczPowinnoZwrócićWyjątekGdyLiczbaNieZZakresu()
    {
        assertThrows(IllegalArgumentException.class, () -> testowePoleZakładu.zaznacz(0));
        assertThrows(IllegalArgumentException.class, () -> testowePoleZakładu.zaznacz(50));
    }

    @Test
    void zamieńNaZakładPowinnoZwrócićWyjątekGdyPoleNiepoprawne()
    {
        testowePoleZakładu.zaznacz(1);

        assertThrows(NieważnePoleZakładu.class, () -> testowePoleZakładu.zamieńNaZakład());
    }

    @Test
    void zamieńNaZakładPowinnoPrawidłowoZamienićPoleNaZakład() throws NieważnePoleZakładu
    {
        List<Integer> oczekiwane = List.of(1, 4, 8, 10, 20, 49);

        for (int liczba : oczekiwane)
        {
            testowePoleZakładu.zaznacz(liczba);
        }

        Zakład zakład = testowePoleZakładu.zamieńNaZakład();

        assertIterableEquals(oczekiwane, zakład.liczby());
    }

    @Test
    void zamieńNaZakładPowinnoNieMiećPowtórzonychLiczb() throws NieważnePoleZakładu
    {
        List<Integer> oczekiwane = List.of(1, 4, 8, 10, 20, 49, 49);

        for (int liczba : oczekiwane)
        {
            testowePoleZakładu.zaznacz(liczba);
        }

        Zakład zakład = testowePoleZakładu.zamieńNaZakład();

        assertIterableEquals(new TreeSet<>(oczekiwane), zakład.liczby());
    }
}
