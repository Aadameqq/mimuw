package lotto.gracze;

import lotto.finanse.Pieniądze;
import lotto.zakłady.Blankiet;
import lotto.zarządzanie.Kolektura;

import java.util.List;

public class GraczStałoblankietowy extends GraczStały
{
    private final Blankiet ulubionyBlankiet;

    public GraczStałoblankietowy(List<Kolektura> ulubioneKolektury, Osoba osoba, Pieniądze stanKonta, Blankiet ulubionyBlankiet, int częstotliwośćUdziału)
    {
        super(ulubioneKolektury, osoba, stanKonta, częstotliwośćUdziału);
        this.ulubionyBlankiet = ulubionyBlankiet;
    }

    @Override
    protected Blankiet wypełnijBlankiet()
    {
        return ulubionyBlankiet;
    }
}
