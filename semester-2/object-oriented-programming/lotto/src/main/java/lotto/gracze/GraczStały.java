package lotto.gracze;

import lotto.finanse.NiewystarczająceŚrodkiPieniężne;
import lotto.finanse.Pieniądze;
import lotto.losowania.NadchodząceLosowanie;
import lotto.zakłady.Blankiet;
import lotto.zakłady.Kupon;
import lotto.zarządzanie.Kolektura;

import java.util.List;

public abstract class GraczStały extends Gracz
{
    private final List<Kolektura> ulubioneKolektury;
    private int indeksObecnejKolektury = 0;
    private final int częstotliwośćUdziałuWLosowaniach;
    private int nrOstatniegoLosowania = -1;

    protected GraczStały(List<Kolektura> ulubioneKolektury, Osoba osoba, Pieniądze stanKonta, int częstotliwośćUdziałuWLosowaniach)
    {
        super(osoba, stanKonta);
        this.ulubioneKolektury = ulubioneKolektury;
        this.częstotliwośćUdziałuWLosowaniach = częstotliwośćUdziałuWLosowaniach;
    }

    protected abstract Blankiet wypełnijBlankiet();

    @Override
    public void zareagujNaNadchodząceLosowanie(NadchodząceLosowanie losowanie)
    {
        Kolektura kolektura = ulubioneKolektury.get(indeksObecnejKolektury++);
        if (indeksObecnejKolektury >= ulubioneKolektury.size())
        {
            indeksObecnejKolektury = 0;
        }

        if (!czyBierzeUdział(losowanie.numer())) return;

        nrOstatniegoLosowania = losowanie.numer();
        Blankiet blankiet = wypełnijBlankiet();

        try
        {
            Kupon kupon = kolektura.kup(blankiet, this);
            kupony.add(kupon);
        }
        catch (NiewystarczająceŚrodkiPieniężne e)
        {
        }
    }

    private boolean czyBierzeUdział(int nrLosowania)
    {
        if (nrOstatniegoLosowania == -1)
        {
            return true;
        }
        int różnica = nrLosowania - nrOstatniegoLosowania;
        return różnica == częstotliwośćUdziałuWLosowaniach;
    }
}
