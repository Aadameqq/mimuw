package lotto.zarządzanie;

import lotto.finanse.NiewystarczająceŚrodkiPieniężne;
import lotto.finanse.Pieniądze;
import lotto.gracze.Gracz;
import lotto.losowania.Generator;
import lotto.zakłady.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KolekturaImpl implements Kolektura
{
    private final Generator generator;
    private final Map<String, Kupon> kupony = new HashMap<>();
    private final Map<String, Kupon> zrealizowane = new HashMap<>();
    private final Centrala centrala;
    private final int nrKolektury;

    public KolekturaImpl(Centrala centrala, int nrKolektury, Generator generator)
    {
        this.generator = generator;
        this.centrala = centrala;
        this.nrKolektury = nrKolektury;
        centrala.dodajKolekturę(this);
    }

    public Kupon kup(Blankiet blankiet, Gracz gracz) throws NiewystarczająceŚrodkiPieniężne
    {
        Kupon kupon = centrala.utwórzKupon(blankiet.ilośćLosowań(), this);

        for (PoleZakładu pole : blankiet.polaZakładów())
        {
            try
            {
                kupon.dodajZakład(pole.zamieńNaZakład());
            }
            catch (NieważnePoleZakładu e)
            {
            }
        }

        pobierzOpłatęOrazZarejestrujKupon(kupon, gracz);
        return kupon;
    }


    public Kupon kupLosowy(int ilośćZakładów, int ilośćLosowań, Gracz gracz) throws NiewystarczająceŚrodkiPieniężne
    {
        Kupon kupon = centrala.utwórzKupon(ilośćLosowań, this);

        for (int i = 0; i < ilośćZakładów; i++)
        {
            Zakład zakład = wygenerujLosowyZakład();
            kupon.dodajZakład(zakład);
        }

        pobierzOpłatęOrazZarejestrujKupon(kupon, gracz);
        return kupon;
    }

    public void wypłaćNagrodę(Kupon kupon, Gracz gracz) throws KuponUżytyPonownie, KuponNieIstnieje
    {
        if (!kupony.containsKey(kupon.id()))
        {
            if (zrealizowane.containsKey(kupon.id()))
            {
                throw new KuponUżytyPonownie();
            }
            throw new KuponNieIstnieje();
        }

        Kupon znaleziony = kupony.get(kupon.id());

        for (int nrLosowania : znaleziony.numeryLosowań())
        {
            for (Zakład zakład : znaleziony.zakłady())
            {
                Pieniądze nagroda = centrala.wypłaćNagrodę(zakład, nrLosowania);

                if (nagroda.czyZero())
                {
                    continue;
                }

                gracz.przyjmijPieniądze(nagroda);
            }
        }

        kupony.remove(kupon.id());
        zrealizowane.put(kupon.id(), kupon);
    }

    @Override
    public int numer()
    {
        return nrKolektury;
    }

    private void pobierzOpłatęOrazZarejestrujKupon(Kupon kupon, Gracz gracz) throws NiewystarczająceŚrodkiPieniężne
    {
        Pieniądze stanKonta = gracz.stanKonta();
        if (stanKonta.compareTo(kupon.cena()) < 0)
        {
            throw new NiewystarczająceŚrodkiPieniężne();
        }
        gracz.dajPieniądze(kupon.cena());
        centrala.przyjmijPieniądzeZaKupon(kupon);

        kupony.put(kupon.id(), kupon);
    }

    private Zakład wygenerujLosowyZakład()
    {
        Set<Integer> listaLiczb = generator.wylosujSzóstkę();
        return new Zakład(listaLiczb);
    }

}
