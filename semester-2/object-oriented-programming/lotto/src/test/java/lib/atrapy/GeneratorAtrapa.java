package lib.atrapy;

import lotto.losowania.Generator;

import java.util.Set;

public class GeneratorAtrapa implements Generator
{
    private int zwracanaLiczba = 50;
    private Set<Integer> zwracanaSzóstka = Set.of(1, 2, 3, 4, 5, 6);

    public int zwracanaLiczba()
    {
        return zwracanaLiczba;
    }

    public Set<Integer> zwracanaSzóstka()
    {
        return zwracanaSzóstka;
    }

    public void ustawZwracanąLiczbę(int liczba)
    {
        this.zwracanaLiczba = liczba;
    }

    public void ustawZwracanąSzóstkę(Set<Integer> szóstka)
    {
        this.zwracanaSzóstka = szóstka;
    }

    @Override
    public int wylosujLiczbę(int min, int maks)
    {
        return zwracanaLiczba;
    }

    @Override
    public Set<Integer> wylosujSzóstkę()
    {
        return zwracanaSzóstka;
    }
}
