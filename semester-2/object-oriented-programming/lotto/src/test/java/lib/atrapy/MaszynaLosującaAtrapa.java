package lib.atrapy;

import lotto.finanse.Pieniądze;
import lotto.losowania.Losowanie;
import lotto.losowania.MaszynaLosująca;
import lotto.losowania.NadchodząceLosowanie;

import java.util.Map;
import java.util.Set;

public class MaszynaLosującaAtrapa implements MaszynaLosująca
{
    @Override
    public Losowanie przeprowadźLosowanie(NadchodząceLosowanie doPrzeprowadzenia)
    {
        return new Losowanie(doPrzeprowadzenia.numer(), Set.of(), Map.of(), Pieniądze.daj(1000));
    }
}
