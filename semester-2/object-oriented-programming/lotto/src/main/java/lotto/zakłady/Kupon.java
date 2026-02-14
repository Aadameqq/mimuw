package lotto.zakłady;

import lotto.finanse.Pieniądze;
import lotto.losowania.Generator;
import lotto.zarządzanie.Kolektura;

import java.util.ArrayList;
import java.util.List;

public class Kupon
{
    private final String id;
    private final List<Zakład> zakłady = new ArrayList<>();
    private final List<Integer> numeryLosowań = new ArrayList<>();
    private final Kolektura kolektura;
    private final Generator generator;

    public Kupon(int nrKuponu, Kolektura kolektura, int nrLosowania, int ilośćLosowań, Generator generator)
    {
        this.kolektura = kolektura;
        this.generator = generator;
        id = wygenerujIdentyfikator(nrKuponu, kolektura.numer());
        for (int nr = nrLosowania; nr < nrLosowania + ilośćLosowań; nr++)
        {
            numeryLosowań.add(nr);
        }
    }

    private String wygenerujIdentyfikator(int nrKuponu, int nrKolektury)
    {
        int min = 100_000_000;
        int maks = 1_000_000_000;
        int losowa = generator.wylosujLiczbę(min, maks);

        String SEPARATOR = "-";
        StringBuilder sb = new StringBuilder();
        sb.append(nrKuponu).append(SEPARATOR)
                .append(nrKolektury)
                .append(SEPARATOR)
                .append(losowa)
                .append(SEPARATOR)
                .append(policzSumęKontrolną(sb.toString()));

        return sb.toString();
    }

    private int policzSumęKontrolną(String id)
    {
        int suma = 0;
        for (char znak : id.toCharArray())
        {
            if (Character.isDigit(znak))
            {
                suma += Character.getNumericValue(znak);
            }
        }
        return suma % 100;
    }

    public void dodajZakład(Zakład zakład)
    {
        zakłady.add(zakład);
    }

    public int ilośćZakładów()
    {
        return zakłady.size();
    }

    public Zakład zakład(int numer)
    {
        return zakłady.get(numer - 1);
    }

    public Iterable<Zakład> zakłady()
    {
        return zakłady;
    }

    public int ilośćLosowań()
    {
        return numeryLosowań.size();
    }

    public Iterable<Integer> numeryLosowań()
    {
        return numeryLosowań;
    }

    public String id()
    {
        return id;
    }

    public Pieniądze cena()
    {
        return Zakład.cena().iloczyn(ilośćLosowań()).iloczyn(zakłady.size());
    }

    public Pieniądze podatek()
    {
        return Zakład.podatek().iloczyn(ilośćLosowań()).iloczyn(zakłady.size());
    }

    public Pieniądze przychód()
    {
        return cena().różnica(podatek());
    }

    public Kolektura kolektura()
    {
        return kolektura;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("KUPON NR ")
                .append(id)
                .append("\n");
        for (int i = 0; i < zakłady.size(); i++)
        {
            sb.append(i + 1).append(": ").append(zakłady.get(i)).append("\n");
        }
        sb.append("LICZBA LOSOWAŃ: ").append(numeryLosowań.size()).append("\n");
        sb.append("NUMERY LOSOWAŃ:\n ");
        for (int numerLosowania : numeryLosowań)
        {
            sb.append(numerLosowania).append(" ");
        }
        sb.append("\n");
        sb.append("CENA: ").append(cena());
        return sb.toString();
    }
}
