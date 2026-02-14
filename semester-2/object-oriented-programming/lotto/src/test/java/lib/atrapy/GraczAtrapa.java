package lib.atrapy;

import lotto.finanse.Pieniądze;
import lotto.gracze.Gracz;
import lotto.gracze.Osoba;
import lotto.losowania.NadchodząceLosowanie;

public class GraczAtrapa extends Gracz
{
    public GraczAtrapa()
    {
        super(new Osoba("a", "b", "c"), Pieniądze.zero());
    }

    @Override
    public void zareagujNaNadchodząceLosowanie(NadchodząceLosowanie losowanie)
    {

    }

    public void wyczyśćKonto()
    {
        dajPieniądze(stanKonta());
    }
}
