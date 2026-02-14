package lib.zarządzanie;

import lib.atrapy.GeneratorAtrapa;
import lib.atrapy.HarmonogramAtrapa;
import lib.atrapy.KolekturaAtrapa;
import lotto.finanse.BudżetPaństwa;
import lotto.finanse.Pieniądze;
import lotto.losowania.Losowanie;
import lotto.losowania.Wygrana;
import lotto.zakłady.Kupon;
import lotto.zakłady.Zakład;
import lotto.zarządzanie.Centrala;
import lotto.zarządzanie.CentralaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CentralaTest
{
    private HarmonogramAtrapa atrapaHarmonogramu;
    private Centrala testowaCentrala;
    private BudżetPaństwa testowyBudżet;
    private Kupon testowyKupon;
    private final Set<Integer> wygrywająceLiczby = Set.of(7, 8, 9, 10, 11, 12);

    @BeforeEach
    void skonfiguruj()
    {
        testowyBudżet = new BudżetPaństwa();
        atrapaHarmonogramu = new HarmonogramAtrapa();
        testowaCentrala = new CentralaImpl(atrapaHarmonogramu, new GeneratorAtrapa(), testowyBudżet);

        testowyKupon = new Kupon(1, new KolekturaAtrapa(), 4, 5, new GeneratorAtrapa());
        for (int i = 0; i < 4; i++)
        {
            testowyKupon.dodajZakład(new Zakład(Set.of(1, 2, 3, 4, 5, 6 + i)));
        }
    }

    @Test
    void przyjmijPieniądzeZaKuponPowinnoZwiększyćFinanseCentraliOPrzychódZKuponu()
    {
        testowaCentrala.przyjmijPieniądzeZaKupon(testowyKupon);
        assertEquals(testowyKupon.przychód(), testowaCentrala.fundusze());
    }

    @Test
    void przyjmijPieniądzeZaKuponPowinnoZapłacićPodatek()
    {
        testowaCentrala.przyjmijPieniądzeZaKupon(testowyKupon);
        assertEquals(testowyKupon.podatek(), testowyBudżet.łącznaKwotaPodatków());
    }

    @Test
    void przyjmijPieniądzeZaKuponPowinnoDodaćKażdyZakładDoPuliKażdegoLosowania()
    {
        testowaCentrala.przyjmijPieniądzeZaKupon(testowyKupon);

        Map<Integer, Integer> pule = atrapaHarmonogramu.puleLosowań();

        assertEquals(testowyKupon.ilośćLosowań(), pule.size());

        for (int numer : testowyKupon.numeryLosowań())
        {
            assertTrue(pule.containsKey(numer));
            assertEquals(4, pule.get(numer));
        }
    }

    @Test
    void wypłaćNagrodęPowinnoZwrócićZeroGdyLosowanieODanymNumerzeNieIstnieje()
    {
        assertEquals(Pieniądze.zero(), testowaCentrala.wypłaćNagrodę(new Zakład(Set.of(1, 2, 3, 4, 5, 6)), 1000));
    }

    @Test
    void wypłaćNagrodęPowinnoNieOdejmowaćPodatkuGdyWygranaMniejszaNiżMinimalnaOpodatkowana()
    {
        Pieniądze wygrana = Pieniądze.daj(2279);
        ustawTestoweLosowanie(wygrana);

        assertEquals(wygrana, testowaCentrala.wypłaćNagrodę(new Zakład(wygrywająceLiczby), 1));
    }

    @Test
    void wypłaćNagrodęPowinnoOdjąćPodatekIPrzekazaćGoDoBudżetuPaństwaGdyWygranaNiemniejszaNiżMinimalnaOpodatkowana()
    {
        ustawTestoweLosowanie(Pieniądze.daj(2280));

        assertEquals(Pieniądze.daj(2052), testowaCentrala.wypłaćNagrodę(new Zakład(wygrywająceLiczby), 1));
        assertEquals(Pieniądze.daj(228), testowyBudżet.łącznaKwotaPodatków());
    }

    @Test
    void wypłaćNagrodęPowinnoPomniejszyćFinanseCentraliOWygranąZPodatkiem()
    {
        testowaCentrala.przyjmijPieniądze(Pieniądze.daj(10_000));

        ustawTestoweLosowanie(Pieniądze.daj(3000));
        testowaCentrala.wypłaćNagrodę(new Zakład(wygrywająceLiczby), 1);

        assertEquals(Pieniądze.daj(7000), testowaCentrala.fundusze());
    }

    @Test
    void wypłaćNagrodęPowinnoPobraćSubwencjęNaBrakującąKwotęGdyFunduszeCentraliNiewystarczające()
    {
        testowaCentrala.przyjmijPieniądze(Pieniądze.daj(1000));

        ustawTestoweLosowanie(Pieniądze.daj(3000));
        testowaCentrala.wypłaćNagrodę(new Zakład(wygrywająceLiczby), 1);

        assertEquals(Pieniądze.daj(2000), testowyBudżet.łącznaKwotaSubwencji());
    }

    private void ustawTestoweLosowanie(Pieniądze kwotaWygranej)
    {
        Wygrana wygrana = new Wygrana(1, 1, kwotaWygranej, Pieniądze.zero());

        Losowanie losowanie = new Losowanie(1, wygrywająceLiczby, Map.of(1, wygrana), Pieniądze.zero());

        atrapaHarmonogramu.ustawZwracaneLosowanie(losowanie);
    }
}
