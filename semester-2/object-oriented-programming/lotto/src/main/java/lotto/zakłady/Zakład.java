package lotto.zakłady;

import lotto.finanse.Pieniądze;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class Zakład
{
    private final Set<Integer> liczby;
    private final static Pieniądze podatek = Pieniądze.daj(0, 60);
    private final static Pieniądze cena = Pieniądze.daj(3);

    public Zakład(Collection<Integer> liczby)
    {
        if (liczby.size() != 6)
        {
            throw new IllegalArgumentException("Zakład musi zawierać dokładnie 6 liczb.");
        }
        this.liczby = new TreeSet<>(liczby);
    }

    public Iterable<Integer> liczby()
    {
        return liczby;
    }

    public static Pieniądze podatek()
    {
        return podatek;
    }

    public static Pieniądze cena()
    {
        return cena;
    }

    public static Pieniądze przychód()
    {
        return cena.różnica(podatek);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int liczba : liczby)
        {
            if (liczba <= 9)
            {
                sb.append(" ");
            }
            sb.append(liczba).append(" ");
        }
        return sb.toString();
    }
}
