package lib.gracze;

import lotto.finanse.Pieniądze;
import lotto.gracze.GraczStały;
import lotto.gracze.Osoba;
import lotto.zakłady.Blankiet;
import lotto.zarządzanie.Kolektura;

import java.util.List;

public class TestowyGraczStały extends GraczStały
{
    public TestowyGraczStały(List<Kolektura> ulubioneKolektury, Osoba osoba, Pieniądze stanKonta, int częstotliwośćUdziałuWLosowaniach)
    {
        super(ulubioneKolektury, osoba, stanKonta, częstotliwośćUdziałuWLosowaniach);
    }

    @Override
    protected Blankiet wypełnijBlankiet()
    {
        return new Blankiet();
    }
}
