package lotto.gracze;

import lotto.finanse.Pieniądze;
import lotto.zakłady.Blankiet;
import lotto.zakłady.PoleZakładu;
import lotto.zarządzanie.Kolektura;

import java.util.List;
import java.util.Set;

public class GraczStałoliczbowy extends GraczStały
{
    private static final int CZĘSTOTLIWOŚĆ_UDZIAŁU = 10;
    private final Set<Integer> ulubioneLiczby;

    public GraczStałoliczbowy(List<Kolektura> ulubioneKolektury, Osoba osoba, Pieniądze stanKonta, Set<Integer> ulubioneLiczby)
    {
        super(ulubioneKolektury, osoba, stanKonta, CZĘSTOTLIWOŚĆ_UDZIAŁU);
        this.ulubioneLiczby = ulubioneLiczby;
    }

    @Override
    protected Blankiet wypełnijBlankiet()
    {
        Blankiet blankiet = new Blankiet();
        PoleZakładu pole = blankiet.poleZakładu(1);
        for (int liczba : ulubioneLiczby)
        {
            pole.zaznacz(liczba);
        }
        blankiet.zaznaczIlośćLosowań(10);
        return blankiet;
    }
}
