package lotto.powiadomienia;

import lotto.losowania.Losowanie;
import lotto.losowania.NadchodząceLosowanie;

import java.util.ArrayList;
import java.util.List;

public class InformatorImpl implements Informator
{
    private final List<Obserwator> obserwatorzy = new ArrayList<>();

    public void zasubskrybuj(Obserwator obserwator)
    {
        obserwatorzy.add(obserwator);
    }

    public void poinformujONadchodzącymLosowaniu(NadchodząceLosowanie losowanie)
    {
        for (Obserwator obserwator : obserwatorzy)
        {
            obserwator.zareagujNaNadchodząceLosowanie(losowanie);
        }
    }

    public void poinformujOZakończonymLosowaniu(Losowanie losowanie)
    {
        for (Obserwator obserwator : obserwatorzy)
        {
            obserwator.zareagujNaZakończoneLosowanie(losowanie);
        }
    }
}
