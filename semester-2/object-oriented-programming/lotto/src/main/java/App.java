import lotto.finanse.BudżetPaństwa;
import lotto.finanse.Pieniądze;
import lotto.gracze.*;
import lotto.losowania.Generator;
import lotto.losowania.GeneratorImpl;
import lotto.losowania.MaszynaLosująca;
import lotto.losowania.MaszynaLosującaImpl;
import lotto.powiadomienia.Informator;
import lotto.powiadomienia.InformatorImpl;
import lotto.zakłady.Blankiet;
import lotto.zakłady.PoleZakładu;
import lotto.zarządzanie.*;

import java.util.ArrayList;
import java.util.List;

public class App
{
    private static final Generator generator = new GeneratorImpl();

    public static void main(String[] args)
    {
        BudżetPaństwa budżetPaństwa = new BudżetPaństwa();
        Informator informator = new InformatorImpl();
        MaszynaLosująca maszynaLosująca = new MaszynaLosującaImpl(new GeneratorImpl());
        HarmonogramLosowań harmonogram = new HarmonogramLosowańImpl(informator, maszynaLosująca);
        Centrala centrala = new CentralaImpl(harmonogram, new GeneratorImpl(), budżetPaństwa);

        for (int nrKolektury = 1; nrKolektury <= 10; nrKolektury++)
        {
            Kolektura kolektura = new KolekturaImpl(centrala, nrKolektury, new GeneratorImpl());
        }

        for (Kolektura kolektura : centrala.kolektury())
        {
            for (int i = 0; i < 20; i++)
            {
                Gracz minimalista = new GraczMinimalista(
                        kolektura,
                        losowaOsoba(),
                        Pieniądze.daj(1000)
                );
                informator.zasubskrybuj(minimalista);

                Gracz losowy = new GraczLosowy(
                        centrala.kolektury(), losowaOsoba(),
                        Pieniądze.daj(generator.wylosujLiczbę(1, 999_999))
                );
                informator.zasubskrybuj(losowy);
            }
        }

        for (int i = 0; i < 200; i++)
        {
            List<Kolektura> kolektury = new ArrayList<>();

            for (int j = 0; j <= 5; j++)
            {
                Kolektura losowa = centrala
                        .kolektury()
                        .get(
                                generator.wylosujLiczbę(0, centrala.kolektury().size() - 1)
                        );
                kolektury.add(losowa);
            }

            Gracz stałoliczbowy = new GraczStałoliczbowy(
                    kolektury,
                    losowaOsoba(),
                    Pieniądze.daj(5000),
                    generator.wylosujSzóstkę()
            );

            informator.zasubskrybuj(stałoliczbowy);

            Blankiet blankiet = new Blankiet();

            wypełnijPoleLosowo(blankiet.poleZakładu(1));
            wypełnijPoleLosowo(blankiet.poleZakładu(2));

            blankiet.zaznaczIlośćLosowań(generator.wylosujLiczbę(1, 10));

            Gracz stałoblankietowy = new GraczStałoblankietowy(
                    kolektury,
                    losowaOsoba(),
                    Pieniądze.daj(5000),
                    blankiet,
                    generator.wylosujLiczbę(1, 10)
            );
            informator.zasubskrybuj(stałoblankietowy);
        }

        for (int i = 0; i < 20; i++)
        {
            centrala.losuj();
        }

        for (int i = 1; i <= 20; i++)
        {
            System.out.println(centrala.losowanie(i).get());
            System.out.println();
        }

        System.out.println(budżetPaństwa);
    }

    private static void wypełnijPoleLosowo(PoleZakładu pole)
    {
        for (int liczba : generator.wylosujSzóstkę())
        {
            pole.zaznacz(liczba);
        }
    }

    private static final Osoba[] osoby = {
            new Osoba("Andrzej", "Kmicic", "81010112345"),
            new Osoba("Aleksandra", "Billewiczówna", "82020223456"),
            new Osoba("Michał", "Wołodyjowski", "83030334567"),
            new Osoba("Bogusław", "Radziwiłł", "84040445678"),
            new Osoba("Tadeusz", "Soplica", "87070778901"),
            new Osoba("Zofia", "Horeszkówna", "88080889012"),
            new Osoba("Jacek", "Soplica", "89090990123"),
            new Osoba("Gerwazy", "Rębajło", "90010101234")
    };

    public static Osoba losowaOsoba()
    {
        return osoby[generator.wylosujLiczbę(0, osoby.length - 1)];
    }

}