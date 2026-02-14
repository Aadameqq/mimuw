package lotto.losowania;

import lotto.finanse.Pieniądze;
import lotto.zakłady.Zakład;

import java.util.ArrayList;
import java.util.List;

public class NadchodząceLosowanie
{
    private final int nrLosowania;
    private Pieniądze pulaNagród;
    private final List<Zakład> zakłady = new ArrayList<>();
    private Pieniądze kumulacja = Pieniądze.zero();

    public NadchodząceLosowanie(int nrLosowania, Pieniądze pulaNagród)
    {
        this.nrLosowania = nrLosowania;
        this.pulaNagród = pulaNagród;
    }

    public NadchodząceLosowanie(int nrLosowania)
    {
        this(nrLosowania, Pieniądze.zero());
    }

    public void dodajDoPuli(Zakład zakład)
    {
        pulaNagród = pulaNagród.suma(Zakład.przychód());
        zakłady.add(zakład);
    }

    public void ustawKumulację(Pieniądze kumulacja)
    {
        this.kumulacja = kumulacja;
    }

    public int numer()
    {
        return nrLosowania;
    }

    public Pieniądze pulaNagród()
    {
        return pulaNagród;
    }

    public Iterable<Zakład> zakłady()
    {
        return zakłady;
    }

    public Pieniądze kumulacja()
    {
        return kumulacja;
    }
}
