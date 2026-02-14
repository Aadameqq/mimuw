package lotto.zakłady;

import java.util.ArrayList;
import java.util.List;

public class Blankiet
{
    private static final int ILOŚĆ_PÓL = 8;
    private static final int ILOŚĆ_LOSOWAŃ = 10;
    private final List<PoleZakładu> polaZakładu = new ArrayList<>();
    private final List<Kratka> ilościLosowań = new ArrayList<>();
    private int największaIlośćLosowań = 1;

    public Blankiet()
    {
        for (int i = 0; i < ILOŚĆ_PÓL; i++)
        {
            polaZakładu.add(new PoleZakładu(i + 1));
        }

        for (int i = 0; i < ILOŚĆ_LOSOWAŃ; i++)
        {
            ilościLosowań.add(new Kratka(String.valueOf(i + 1), false));
        }
    }

    public PoleZakładu poleZakładu(int numer)
    {
        if (numer < 1 || numer > ILOŚĆ_PÓL)
        {
            throw new IllegalArgumentException("Numer pola zakładu musi być z zakresu od 1 do " + ILOŚĆ_PÓL);
        }
        return polaZakładu.get(numer - 1);
    }

    public Iterable<PoleZakładu> polaZakładów()
    {
        return polaZakładu;
    }

    public void zaznaczIlośćLosowań(int liczba)
    {
        if (liczba < 1 || liczba > ILOŚĆ_LOSOWAŃ)
        {
            throw new IllegalArgumentException("Liczba losowań musi być z zakresu od 1 do " + ILOŚĆ_LOSOWAŃ);
        }
        ilościLosowań.get(liczba - 1).zaznacz();
        największaIlośćLosowań = Math.max(największaIlośćLosowań, liczba);
    }

    public int ilośćLosowań()
    {
        return największaIlośćLosowań;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ILOŚĆ_PÓL; i++)
        {
            sb.append(polaZakładu.get(i));
        }
        sb.append("Liczba losowań: ");

        for (int i = 0; i < ILOŚĆ_LOSOWAŃ; i++)
        {
            sb.append(ilościLosowań.get(i));
        }
        return sb.toString();
    }
}
