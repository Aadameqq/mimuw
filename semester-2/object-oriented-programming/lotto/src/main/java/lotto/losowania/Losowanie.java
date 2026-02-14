package lotto.losowania;

import lotto.finanse.Pieniądze;
import lotto.zakłady.Zakład;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Losowanie
{
    private final int nrLosowania;
    private final Set<Integer> liczby;
    private final Map<Integer, Wygrana> wygrane;
    private final Pieniądze kumulacjaDoKolejnegoLosowania;

    public Losowanie(int nrLosowania, Collection<Integer> liczby, Map<Integer, Wygrana> wygrane, Pieniądze kumulacjaDoKolejnegoLosowania)
    {
        this.nrLosowania = nrLosowania;
        this.liczby = new TreeSet<>(liczby);
        this.wygrane = wygrane;
        this.kumulacjaDoKolejnegoLosowania = kumulacjaDoKolejnegoLosowania;
    }

    public int numer()
    {
        return nrLosowania;
    }

    public static int obliczStopieńNagrody(Zakład zakład, Set<Integer> liczby)
    {
        int ilośćWspólnych = 0;

        for (int liczba : zakład.liczby())
        {
            if (liczby.contains(liczba))
            {
                ilośćWspólnych++;
            }
        }

        if (ilośćWspólnych <= 2)
        {
            return 0;
        }

        return 6 - ilośćWspólnych + 1;
    }

    public Pieniądze obliczNagrodę(Zakład zakład)
    {
        int stopień = obliczStopieńNagrody(zakład, liczby);

        if (stopień == 0)
        {
            return Pieniądze.zero();
        }

        Wygrana wygrana = wygrane.get(stopień);
        return wygrana.pojedyńczaWygrana();
    }

    public Pieniądze kumulacjaDoKolejnego()
    {
        return kumulacjaDoKolejnegoLosowania;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Losowanie nr ").append(nrLosowania).append("\n").append("Wyniki: ");

        for (int liczba : liczby)
        {
            if (liczba < 10)
            {
                sb.append(" ");
            }
            sb.append(liczba).append(" ");
        }

        sb.append("\n");

        for (int stopień : wygrane.keySet())
        {
            Wygrana wygrana = wygrane.get(stopień);
            sb.append("Nagroda stopnia ").append(stopień).append(":\n")
                    .append(" - ilość wygranych: ").append(wygrana.ilośćWygranych())
                    .append("\n - kwota: ").append(wygrana.pojedyńczaWygrana())
                    .append("\n - pula: ").append(wygrana.pula()).append("\n");
        }
        sb.append("Kumulacja do kolejnego: ").append(kumulacjaDoKolejnego());

        return sb.toString();
    }
}
