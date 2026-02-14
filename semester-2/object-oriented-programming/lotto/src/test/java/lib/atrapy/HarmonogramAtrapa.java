package lib.atrapy;

import lotto.losowania.Losowanie;
import lotto.losowania.NadchodząceLosowanie;
import lotto.zakłady.Zakład;
import lotto.zarządzanie.HarmonogramLosowań;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class HarmonogramAtrapa implements HarmonogramLosowań
{
    private Map<Integer, Integer> puleLosowań = new TreeMap<>();
    private Losowanie zwracaneLosowanie;

    public void ustawZwracaneLosowanie(Losowanie losowanie)
    {
        this.zwracaneLosowanie = losowanie;
    }

    public Map<Integer, Integer> puleLosowań()
    {
        return puleLosowań;
    }


    @Override
    public void przeprowadźLosowanie()
    {

    }

    @Override
    public Optional<Losowanie> losowanie(int nr)
    {
        if (zwracaneLosowanie == null)
            return Optional.empty();
        if (zwracaneLosowanie.numer() == nr)
            return Optional.of(zwracaneLosowanie);
        return Optional.empty();
    }

    @Override
    public void dodajDoPuli(int nr, Zakład zakład)
    {
        if (!puleLosowań.containsKey(nr))
        {
            puleLosowań.put(nr, 0);
        }
        puleLosowań.put(nr, puleLosowań.get(nr) + 1);
    }

    @Override
    public int numerNajbliższegoLosowania()
    {
        return 0;
    }

    @Override
    public Optional<NadchodząceLosowanie> nadchodzące(int nr)
    {
        return Optional.empty();
    }
}
