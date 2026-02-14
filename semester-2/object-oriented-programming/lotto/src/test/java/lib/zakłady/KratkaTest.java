package lib.zakłady;

import lotto.zakłady.Kratka;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KratkaTest
{
    Kratka testKratka;

    @BeforeEach
    void skonfiguruj()
    {
        testKratka = new Kratka();
    }

    @Test
    public void powinnaByćNiezaznaczonaPoStworzeniu()
    {
        assertFalse(testKratka.czyZaznaczona());
    }

    @Test
    public void powinnaByćZaznaczonaPoZaznaczeniu()
    {
        testKratka.zaznacz();
        assertTrue(testKratka.czyZaznaczona());
    }

    @Test
    public void powinnaByćNadalZaznaczonaPoPonownymZaznaczeniu()
    {
        testKratka.zaznacz();
        testKratka.zaznacz();
        assertTrue(testKratka.czyZaznaczona());
    }

    @Test
    public void powinnaZwrócićWyjątekGdyTworzonaZWartościąOBłędnejDługości()
    {
        assertThrows(IllegalArgumentException.class, () -> new Kratka("123"));
    }

    @Test
    public void powinnaNieZwracaćWyjątkuGdyTworzonaZWartościąOPoprawnejDługości()
    {
        assertDoesNotThrow(() -> new Kratka("12"));
    }

    @Test
    void toStringPowinienWyświetlićPusteMiejsceGdyBrakWartości()
    {
        Kratka pusta = new Kratka();

        assertEquals("[    ] ", pusta.toString());
    }

    @Test
    void toStringPowinienWyświetlićWyrównanąWartośćGdyWartośćZaKrótkaIWłączonoWyrównywanie()
    {
        Kratka pusta = new Kratka("1");

        assertEquals("[  1 ] ", pusta.toString());
    }

    @Test
    void toStringPowinienWyświetlićNiewyrównanąWartośćGdyWartośćZaKrótkaIWyłączonoWyrównywanie()
    {
        Kratka pusta = new Kratka("1", false);

        assertEquals("[ 1 ] ", pusta.toString());
    }

    @Test
    void toStringPowinienPrawidłowoWyświetlićWartośćNiewymagającąWyrównania()
    {
        Kratka pusta = new Kratka("12");

        assertEquals("[ 12 ] ", pusta.toString());
    }
}
