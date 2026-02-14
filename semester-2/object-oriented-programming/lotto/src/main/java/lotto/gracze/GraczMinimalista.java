package lotto.gracze;

import lotto.finanse.NiewystarczająceŚrodkiPieniężne;
import lotto.finanse.Pieniądze;
import lotto.losowania.NadchodząceLosowanie;
import lotto.zakłady.Kupon;
import lotto.zarządzanie.Kolektura;

public class GraczMinimalista extends Gracz
{
    private final Kolektura ulubionaKolektura;

    public GraczMinimalista(Kolektura ulubionaKolektura, Osoba osoba, Pieniądze stanKonta)
    {
        super(osoba, stanKonta);
        this.ulubionaKolektura = ulubionaKolektura;
    }

    @Override
    public void zareagujNaNadchodząceLosowanie(NadchodząceLosowanie losowanie)
    {
        try
        {
            Kupon kupon = ulubionaKolektura.kupLosowy(1, 1, this);
            kupony.add(kupon);
        }
        catch (NiewystarczająceŚrodkiPieniężne e)
        {
        }
    }
}
