package lib.zakłady;

import lotto.zakłady.Blankiet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BlankietTest
{
    private Blankiet blankiet;

    @BeforeEach
    void skonfiguruj()
    {
        blankiet = new Blankiet();
    }

    @Test
    void ilośćLosowańPowinnaByćRównaJedenGdyNieZaznaczonoŻadnej()
    {
        assertEquals(1, blankiet.ilośćLosowań());
    }

    @Test
    void ilośćLosowańPowinnaByćRównaZaznaczonejIlości()
    {
        blankiet.zaznaczIlośćLosowań(5);
        assertEquals(5, blankiet.ilośćLosowań());
    }

    @Test
    void ilośćLosowańPowinnaByćRównaNajwiększejZaznaczonejIlościGdyZaznaczonoWiele()
    {
        blankiet.zaznaczIlośćLosowań(5);
        blankiet.zaznaczIlośćLosowań(9);
        blankiet.zaznaczIlośćLosowań(1);
        assertEquals(9, blankiet.ilośćLosowań());
        blankiet.zaznaczIlośćLosowań(10);
        assertEquals(10, blankiet.ilośćLosowań());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            0, 11
    })
    void zaznaczIlośćLosowańPowinnoZwrócićWyjątekGdyZaznaczonoNieprawidłowąIlośćLosowań(int ilość)
    {
        assertThrows(IllegalArgumentException.class, () -> blankiet.zaznaczIlośćLosowań(ilość));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            4, 1, 8
    })
    void poleZakładuPowinnoZwrócićPoleZPrawidłowymNumerem(int numer)
    {
        assertEquals(numer, blankiet.poleZakładu(numer).numer());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            0, 9
    })
    void poleZakładuPowinnoZwrócićWyjątekGdyPodanoNieprawidłowyNumerPola(int numer)
    {
        assertThrows(IllegalArgumentException.class, () -> blankiet.poleZakładu(numer));
    }
}
