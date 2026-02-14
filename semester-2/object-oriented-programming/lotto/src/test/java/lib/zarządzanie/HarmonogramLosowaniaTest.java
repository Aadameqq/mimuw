package lib.zarządzanie;

import lib.atrapy.InformatorAtrapa;
import lib.atrapy.MaszynaLosującaAtrapa;
import lotto.finanse.Pieniądze;
import lotto.losowania.Losowanie;
import lotto.losowania.NadchodząceLosowanie;
import lotto.zakłady.Zakład;
import lotto.zarządzanie.HarmonogramLosowań;
import lotto.zarządzanie.HarmonogramLosowańImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class HarmonogramLosowaniaTest
{
    private HarmonogramLosowań testowyHarmonogram;
    private InformatorAtrapa atrapaInformatora;

    @BeforeEach
    void skonfiguruj()
    {
        atrapaInformatora = new InformatorAtrapa();
        testowyHarmonogram = new HarmonogramLosowańImpl(atrapaInformatora, new MaszynaLosującaAtrapa());
    }

    @Test
    void numerNajbliższegoLosowaniaPowinienNaPoczątkuZwracaćJeden()
    {
        assertEquals(1, testowyHarmonogram.numerNajbliższegoLosowania());
    }

    @Test
    void numerNajbliższegoLosowaniaPowinienSięZmienićPoPrzeprowadzeniuLosowania()
    {
        testowyHarmonogram.przeprowadźLosowanie();
        assertEquals(2, testowyHarmonogram.numerNajbliższegoLosowania());
    }

    @Test
    void losowaniePowinnoZwrócićLosowanieODanymNumerzePoJegoPrzeprowadzeniu()
    {
        testowyHarmonogram.przeprowadźLosowanie();

        Losowanie losowanie = testowyHarmonogram.losowanie(1).get();

        assertEquals(1, losowanie.numer());
    }

    @Test
    void losowaniePowinnoNieZwracaćLosowaniaODanymNumerzePrzedJegoPrzeprowadzeniem()
    {
        assertTrue(testowyHarmonogram.losowanie(1).isEmpty());
    }

    @Test
    void nadchodzącePowinnoZwrócićNadchodząceLosowanieODanymNumerze()
    {
        NadchodząceLosowanie losowanie = testowyHarmonogram.nadchodzące(1).get();
        assertEquals(1, losowanie.numer());
    }

    @Test
    void nadchodzącePowinnoNieZwracaćLosowaniaKtóreZostałoPrzeprowadzone()
    {
        testowyHarmonogram.przeprowadźLosowanie();

        assertTrue(testowyHarmonogram.nadchodzące(1).isEmpty());
    }

    @Test
    void powinnoPoczątkowoZaplanowaćDziesięćNajbliższychLosowań()
    {
        for (int i = 1; i <= 10; i++)
        {
            assertTrue(testowyHarmonogram.nadchodzące(i).isPresent());
        }
        assertTrue(testowyHarmonogram.nadchodzące(11).isEmpty());
    }

    @Test
    void dodajDoPuliPowinnoDodaćZakładDoPuli()
    {
        testowyHarmonogram.dodajDoPuli(2, new Zakład(Set.of(1, 2, 3, 4, 5, 6)));
        NadchodząceLosowanie losowanie = testowyHarmonogram.nadchodzące(2).get();
        assertNotEquals(Pieniądze.zero(), losowanie.pulaNagród());
    }

    @Test
    void przeprowadźLosowaniePowinnoZaplanowaćKolejneLosowanie()
    {
        testowyHarmonogram.przeprowadźLosowanie();
        assertTrue(testowyHarmonogram.nadchodzące(11).isPresent());
        assertTrue(testowyHarmonogram.nadchodzące(12).isEmpty());
    }

    @Test
    void przeprowadźLosowaniePowinnoPrzenieśćKumulacjęDoKolejnegoLosowania()
    {
        testowyHarmonogram.przeprowadźLosowanie();

        NadchodząceLosowanie nadchodząceLosowanie = testowyHarmonogram.nadchodzące(2).get();

        assertEquals(Pieniądze.daj(1000), nadchodząceLosowanie.kumulacja());
    }

    @Test
    void przeprowadźLosowaniePowinnoPoinformowaćONadchodzącymIZakończonymLosowaniu()
    {
        testowyHarmonogram.przeprowadźLosowanie();

        assertEquals(1, atrapaInformatora.nadchodzące().size());
        assertEquals(1, atrapaInformatora.nadchodzące().get(0).numer());
        assertEquals(1, atrapaInformatora.przeprowadzone().size());
        assertEquals(1, atrapaInformatora.przeprowadzone().get(0).numer());
    }
}
