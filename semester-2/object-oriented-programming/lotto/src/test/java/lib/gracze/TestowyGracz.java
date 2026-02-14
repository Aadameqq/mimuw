package lib.gracze;

import lotto.finanse.Pieniądze;
import lotto.gracze.Gracz;
import lotto.gracze.Osoba;
import lotto.losowania.NadchodząceLosowanie;
import lotto.zakłady.Kupon;

public class TestowyGracz extends Gracz
{
    protected TestowyGracz(Osoba osoba, Pieniądze stanKonta)
    {
        super(osoba, stanKonta);
    }

    public void ustawKupon(Kupon kupon)
    {
        kupony.add(kupon);
    }

    @Override
    public void zareagujNaNadchodząceLosowanie(NadchodząceLosowanie losowanie)
    {
    }
}
