package lib.zakłady;

import lib.atrapy.GeneratorAtrapa;
import lib.atrapy.KolekturaAtrapa;
import lotto.finanse.Pieniądze;
import lotto.zakłady.Kupon;
import lotto.zakłady.Zakład;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class KuponTest
{
    private GeneratorAtrapa atrapaGeneratora;
    private Kupon testowyKupon;
    private Kupon testowyKupon2;
    private final int nrKuponu = 1;
    private final int nrKolektury = 2;
    private final int nrLosowania = 3;
    private final int ilośćLosowań = 4;
    private final int losowa = 200_000_123;

    @BeforeEach
    void skonfiguruj()
    {
        atrapaGeneratora = new GeneratorAtrapa();
        atrapaGeneratora.ustawZwracanąLiczbę(losowa);

        KolekturaAtrapa atrapaKolektury = new KolekturaAtrapa();
        atrapaKolektury.ustawNumer(nrKolektury);

        testowyKupon = new Kupon(nrKuponu, atrapaKolektury, nrLosowania, ilośćLosowań, atrapaGeneratora);

        testowyKupon2 = new Kupon(nrKuponu, atrapaKolektury, nrLosowania, ilośćLosowań, atrapaGeneratora);
        Zakład zakład = new Zakład(List.of(1, 2, 3, 4, 5, 6));
        testowyKupon2.dodajZakład(zakład);
        testowyKupon2.dodajZakład(zakład);
    }

    @Test
    void idPowinnoMiećPrawidłowyFormat()
    {
        String oczekiwane = "1-2-" + losowa + "-11";
        assertEquals(oczekiwane, testowyKupon.id());
    }

    @Test
    void powinnoUtworzyćKuponZPoprawnąListąLosowań()
    {
        assertIterableEquals(List.of(3, 4, 5, 6), testowyKupon.numeryLosowań());
        assertEquals(4, testowyKupon.ilośćLosowań());
    }

    @Test
    void ilośćZakładówPowinnoZwrócićZeroDlaNowegoKuponu()
    {
        assertEquals(0, testowyKupon.ilośćZakładów());
    }

    @Test
    void dodajZakładPowinnoZwiększyćIlośćKuponów()
    {
        Zakład zakład = new Zakład(List.of(1, 2, 3, 4, 5, 6));

        testowyKupon.dodajZakład(zakład);

        assertEquals(1, testowyKupon.ilośćZakładów());
    }

    @Test
    void dodajZakładPowinnoDodaćZakładPodOdpowiednimNumerem()
    {
        Zakład zakład = new Zakład(List.of(1, 2, 3, 4, 5, 6));

        testowyKupon.dodajZakład(zakład);

        assertEquals(zakład, testowyKupon.zakład(1));
    }

    @Test
    void cenaPowinnoZwrócićCenęZakładuPrzemnożonąPrzezIlośćLosowańIZakładów()
    {
        Pieniądze oczekiwane = Zakład.cena().iloczyn(ilośćLosowań * 2);

        assertEquals(oczekiwane, testowyKupon2.cena());
    }

    @Test
    void podatekPowinnoZwrócićPodatekZaZakładPrzemnożonyPrzezIlośćLosowańIZakładów()
    {
        Pieniądze oczekiwane = Zakład.podatek().iloczyn(ilośćLosowań * 2);

        assertEquals(oczekiwane, testowyKupon2.podatek());
    }

    @Test
    void przychódPowinnoZwrócićPrzychódZZakładuPrzemnożonyPrzezIlośćLosowańIZakładów()
    {
        Pieniądze oczekiwane = Zakład.przychód().iloczyn(ilośćLosowań * 2);

        assertEquals(oczekiwane, testowyKupon2.przychód());
    }

}
