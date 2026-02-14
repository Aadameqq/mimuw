package lib.atrapy;

import lotto.finanse.NiewystarczająceŚrodkiPieniężne;
import lotto.gracze.Gracz;
import lotto.zakłady.Blankiet;
import lotto.zakłady.Kupon;
import lotto.zarządzanie.Kolektura;

import java.util.ArrayList;
import java.util.List;

public class KolekturaAtrapa implements Kolektura
{
    private List<Kupon> wypłacone = new ArrayList<>();
    private List<Blankiet> kupione = new ArrayList<>();
    private int numer = 0;

    public void ustawNumer(int numer)
    {
        this.numer = numer;
    }

    @Override
    public Kupon kup(Blankiet blankiet, Gracz gracz) throws NiewystarczająceŚrodkiPieniężne
    {
        kupione.add(blankiet);
        return new Kupon(1, this, 1, 1, new GeneratorAtrapa());
    }

    @Override
    public Kupon kupLosowy(int ilośćZakładów, int ilośćLosowań, Gracz gracz) throws NiewystarczająceŚrodkiPieniężne
    {
        return null;
    }

    @Override
    public void wypłaćNagrodę(Kupon kupon, Gracz gracz)
    {
        wypłacone.add(kupon);
    }

    @Override
    public int numer()
    {
        return numer;
    }

    public List<Kupon> wypłaconeKupony()
    {
        return wypłacone;
    }

    public List<Blankiet> kupioneBlankiety()
    {
        return kupione;
    }

    public void wyczyść()
    {
        wypłacone.clear();
        kupione.clear();
    }
}
