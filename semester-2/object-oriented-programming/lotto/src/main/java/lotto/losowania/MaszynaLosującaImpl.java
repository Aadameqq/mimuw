package lotto.losowania;

import lotto.zakłady.Zakład;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MaszynaLosującaImpl implements MaszynaLosująca
{
    private final Generator generator;

    public MaszynaLosującaImpl(Generator generator)
    {
        this.generator = generator;
    }

    @Override
    public Losowanie przeprowadźLosowanie(NadchodząceLosowanie doPrzeprowadzenia)
    {
        Set<Integer> liczby = generator.wylosujSzóstkę();

        Map<Integer, Integer> ilościWygranych = new HashMap<>();

        for (int stopień = 1; stopień <= 4; stopień++)
        {
            ilościWygranych.put(stopień, 0);
        }

        for (Zakład zakład : doPrzeprowadzenia.zakłady())
        {
            int stopień = Losowanie.obliczStopieńNagrody(zakład, liczby);
            if (stopień > 0)
            {
                int ilość = ilościWygranych.get(stopień);
                ilościWygranych.put(stopień, ++ilość);
            }
        }

        KalkulatorNagród kalkulator = new KalkulatorNagród(doPrzeprowadzenia.pulaNagród(), ilościWygranych, doPrzeprowadzenia.kumulacja());

        Map<Integer, Wygrana> wygrane = new HashMap<>();

        wygrane.put(1, kalkulator.wygranaPierwszegoStopnia());
        wygrane.put(2, kalkulator.wygranaDrugiegoStopnia());
        wygrane.put(3, kalkulator.wygranaTrzeciegoStopnia());
        wygrane.put(4, kalkulator.wygranaCzwartegoStopnia());

        return new Losowanie(doPrzeprowadzenia.numer(), liczby, wygrane, kalkulator.kumulacja());
    }
}
