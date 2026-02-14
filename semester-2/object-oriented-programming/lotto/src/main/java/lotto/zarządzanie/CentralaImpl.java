package lotto.zarządzanie;

import lotto.finanse.BudżetPaństwa;
import lotto.finanse.Pieniądze;
import lotto.losowania.Generator;
import lotto.losowania.Losowanie;
import lotto.zakłady.Kupon;
import lotto.zakłady.Zakład;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CentralaImpl implements Centrala
{
    private final HarmonogramLosowań harmonogram;
    private final Generator generator;
    private final BudżetPaństwa budżetPaństwa;
    private Pieniądze fundusze = Pieniądze.zero();
    private int nrKuponu = 1;
    private final List<Kolektura> kolektury = new ArrayList<>();

    public CentralaImpl(HarmonogramLosowań harmonogram, Generator generator, BudżetPaństwa budżetPaństwa)
    {
        this.harmonogram = harmonogram;
        this.generator = generator;
        this.budżetPaństwa = budżetPaństwa;
    }

    public Pieniądze fundusze()
    {
        return fundusze;
    }

    public void przyjmijPieniądzeZaKupon(Kupon kupon)
    {
        fundusze = fundusze.suma(kupon.przychód());
        budżetPaństwa.przyjmijPodatek(kupon.podatek());
        for (int numer : kupon.numeryLosowań())
        {
            for (Zakład zakład : kupon.zakłady())
            {
                harmonogram.dodajDoPuli(numer, zakład);
            }
        }
    }

    @Override
    public Kupon utwórzKupon(int ilośćLosowań, Kolektura kolektura)
    {
        return new Kupon(
                nrKuponu++,
                kolektura,
                harmonogram.numerNajbliższegoLosowania(),
                ilośćLosowań,
                generator
        );
    }

    public void losuj()
    {
        harmonogram.przeprowadźLosowanie();
    }

    public Optional<Losowanie> losowanie(int nr)
    {
        return harmonogram.losowanie(nr);
    }

    public void dodajKolekturę(Kolektura kolektura)
    {
        kolektury.add(kolektura);
    }

    public List<Kolektura> kolektury()
    {
        return kolektury;
    }

    public Pieniądze wypłaćNagrodę(Zakład zakład, int nrLosowania)
    {
        Optional<Losowanie> losowanie = losowanie(nrLosowania);

        if (losowanie.isEmpty())
        {
            return Pieniądze.zero();
        }

        Pieniądze nagroda = losowanie.get().obliczNagrodę(zakład);

        if (nagroda.czyZero())
        {
            return Pieniądze.zero();
        }

        if (nagroda.compareTo(fundusze) > 0)
        {
            dobierzBrakująceFundusze(nagroda);
        }

        fundusze = fundusze.różnica(nagroda);

        return odejmijPodatek(nagroda);
    }

    @Override
    public void przyjmijPieniądze(Pieniądze pieniądze)
    {
        fundusze = fundusze.suma(pieniądze);
    }

    private Pieniądze odejmijPodatek(Pieniądze nagroda)
    {
        Pieniądze minimalnaKwotaOpodatkowana = Pieniądze.daj(2280);
        int PROCENT_PODATKU = 10;

        if (minimalnaKwotaOpodatkowana.compareTo(nagroda) > 0)
        {
            return nagroda;
        }

        Pieniądze podatek = nagroda.procent(PROCENT_PODATKU);
        budżetPaństwa.przyjmijPodatek(nagroda.procent(PROCENT_PODATKU));
        return nagroda.różnica(podatek);
    }

    private void dobierzBrakująceFundusze(Pieniądze nagroda)
    {
        Pieniądze brakujące = nagroda.różnica(fundusze);
        budżetPaństwa.pobierzSubwencję(brakujące);
        fundusze = fundusze.suma(brakujące);
    }
}
