package lib.gracze;

import lib.atrapy.GeneratorAtrapa;
import lib.atrapy.KolekturaAtrapa;
import lotto.finanse.Pieniądze;
import lotto.gracze.Osoba;
import lotto.losowania.Losowanie;
import lotto.losowania.Wygrana;
import lotto.zakłady.Kupon;
import lotto.zakłady.Zakład;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GraczTest
{
    private TestowyGracz testowyGracz;
    private KolekturaAtrapa atrapaKolektury;
    private final Set<Integer> zwycięskieLiczby = Set.of(1, 2, 3, 4, 5, 6);
    private final int nrTestowegoLosowania = 6;
    private Losowanie testoweLosowanie;

    @BeforeEach
    void skonfiguruj()
    {
        testowyGracz = new TestowyGracz(new Osoba("a", "b", "c"), Pieniądze.daj(200));
        atrapaKolektury = new KolekturaAtrapa();

        Map<Integer, Wygrana> wygrane = Map.of(
                1, new Wygrana(1, 1, Pieniądze.daj(1000), Pieniądze.zero()),
                2, new Wygrana(1, 1, Pieniądze.daj(1000), Pieniądze.zero())
        );

        testoweLosowanie = new Losowanie(nrTestowegoLosowania, zwycięskieLiczby, wygrane, Pieniądze.zero());
    }

    @Test
    void dajPieniądzePowinnoOdpowiednioZmniejszyćStanKonta()
    {
        Pieniądze pieniądze = Pieniądze.daj(50);
        testowyGracz.dajPieniądze(pieniądze);
        assertEquals(Pieniądze.daj(150), testowyGracz.stanKonta());
    }

    @Test
    void przyjmijPieniądzePowinnoOdpowiednioZwiększyćStanKonta()
    {
        Pieniądze pieniądze = Pieniądze.daj(50);
        testowyGracz.przyjmijPieniądze(pieniądze);
        assertEquals(Pieniądze.daj(250), testowyGracz.stanKonta());
    }

    @Test
    void zareagujNaZakończoneLosowaniePowinnoNieWypłacaćKuponuGdyKuponMaJeszczeLosowania()
    {
        Kupon kupon = utwórzTestowyKupon(3, 5, zwycięskieLiczby);

        testowyGracz.zareagujNaZakończoneLosowanie(testoweLosowanie);

        assertTrue(atrapaKolektury.wypłaconeKupony().isEmpty());
    }

    @Test
    void zareagujNaZakończoneLosowaniePowinnoNieWypłacaćKuponuGdyKuponNicNieWygrał()
    {
        Kupon kupon = utwórzTestowyKupon(nrTestowegoLosowania, 1, Set.of(7, 8, 9, 10, 11, 12));

        testowyGracz.zareagujNaZakończoneLosowanie(testoweLosowanie);

        assertTrue(atrapaKolektury.wypłaconeKupony().isEmpty());
    }

    @Test
    void zareagujNaZakończoneLosowaniePowinnoWypłacaćKuponGdyPrzynajmniejJedenZakładCośWygrał()
    {
        Kupon kupon = utwórzTestowyKupon(nrTestowegoLosowania, 1, zwycięskieLiczby);
        Zakład zakład = new Zakład(Set.of(6, 7, 8, 9, 10, 11));
        kupon.dodajZakład(zakład);

        testowyGracz.zareagujNaZakończoneLosowanie(testoweLosowanie);

        assertTrue(atrapaKolektury.wypłaconeKupony().contains(kupon));
    }

    @Test
    void zareagujNaZakończoneLosowaniePowinnoNiePróbowaćWypłacaćKuponPowtórnie()
    {
        Kupon kupon = utwórzTestowyKupon(nrTestowegoLosowania, 1, zwycięskieLiczby);

        testowyGracz.zareagujNaZakończoneLosowanie(testoweLosowanie);
        testowyGracz.zareagujNaZakończoneLosowanie(testoweLosowanie);

        assertEquals(1, atrapaKolektury.wypłaconeKupony().size());
    }

    @Test
    void zareagujNaZakończoneLosowaniePowinnoWypłacićTylkoZwycięskieKupony()
    {
        Kupon kuponWygrana = utwórzTestowyKupon(nrTestowegoLosowania, 1, zwycięskieLiczby);
        Kupon kuponMniejszaWygrana = utwórzTestowyKupon(nrTestowegoLosowania, 1, Set.of(10, 2, 3, 4, 5, 6));
        Kupon kuponPrzegrana = utwórzTestowyKupon(nrTestowegoLosowania, 1, Set.of(7, 8, 9, 10, 11, 12));

        testowyGracz.zareagujNaZakończoneLosowanie(testoweLosowanie);

        assertIterableEquals(List.of(kuponWygrana, kuponMniejszaWygrana), atrapaKolektury.wypłaconeKupony());
    }

    private Kupon utwórzTestowyKupon(int nrLosowania, int ilośćLosowań, Set<Integer> liczby)
    {
        Zakład zakład = new Zakład(liczby);
        Kupon kupon = new Kupon(0, atrapaKolektury, nrLosowania, ilośćLosowań, new GeneratorAtrapa());
        kupon.dodajZakład(zakład);
        testowyGracz.ustawKupon(kupon);
        return kupon;
    }

}
