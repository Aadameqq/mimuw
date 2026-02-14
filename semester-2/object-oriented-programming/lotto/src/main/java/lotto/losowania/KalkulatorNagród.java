package lotto.losowania;

import lotto.finanse.Pieniądze;

import java.util.Map;

public class KalkulatorNagród
{
    private static final int PROCENT_NA_PIERWSZEGO_STOPNIA = 44;
    private static final int PROCENT_NA_DRUGIEGO_STOPNIA = 8;
    private final Pieniądze doPodziału;
    private final Pieniądze kumulacja;
    private final Map<Integer, Integer> ilościWygranych;

    public KalkulatorNagród(Pieniądze pula, Map<Integer, Integer> ilościWygranych, Pieniądze kumulacja)
    {
        int PROCENT_PRZEZNACZONY_NA_NAGRODY = 51;
        doPodziału = pula.procent(PROCENT_PRZEZNACZONY_NA_NAGRODY);
        this.kumulacja = kumulacja;
        this.ilościWygranych = ilościWygranych;
    }

    public Wygrana wygranaPierwszegoStopnia()
    {
        return new Wygrana(
                1,
                ilościWygranych.get(1),
                nagrodaPierwszegoStopnia(),
                pulaPierwszegoStopnia()
        );
    }

    public Wygrana wygranaDrugiegoStopnia()
    {
        return new Wygrana(
                2,
                ilościWygranych.get(2),
                nagrodaDrugiegoStopnia(),
                pulaDrugiegoStopnia()
        );
    }

    public Wygrana wygranaTrzeciegoStopnia()
    {
        return new Wygrana(
                3,
                ilościWygranych.get(3),
                nagrodaTrzeciegoStopnia(),
                pulaTrzeciegoStopnia()
        );
    }

    public Wygrana wygranaCzwartegoStopnia()
    {
        return new Wygrana(
                4,
                ilościWygranych.get(4),
                nagrodaCzwartegoStopnia(),
                pulaCzwartegoStopnia()
        );
    }


    private Pieniądze nagrodaPierwszegoStopnia()
    {
        int ilośćWygranych = ilościWygranych.get(1);

        if (ilośćWygranych == 0)
        {
            return Pieniądze.zero();
        }

        Pieniądze pula = pulaPierwszegoStopnia();

        return pula.podzieloneNa(ilośćWygranych);
    }

    private Pieniądze pulaDrugiegoStopnia()
    {
        return doPodziału.procent(PROCENT_NA_DRUGIEGO_STOPNIA);
    }

    private Pieniądze nagrodaDrugiegoStopnia()
    {
        int ilośćWygranych = ilościWygranych.get(2);

        if (ilośćWygranych == 0)
        {
            return Pieniądze.zero();
        }

        return pulaDrugiegoStopnia().podzieloneNa(ilośćWygranych);
    }

    private Pieniądze pulaCzwartegoStopnia()
    {
        int ilośćWygranychCzwartegoStopnia = ilościWygranych.get(4);
        Pieniądze nagrodaCzwartegoStopnia = nagrodaCzwartegoStopnia();
        return nagrodaCzwartegoStopnia.iloczyn(ilośćWygranychCzwartegoStopnia);
    }

    private Pieniądze pulaTrzeciegoStopnia()
    {
        return doPodziału.procent(100 - (PROCENT_NA_PIERWSZEGO_STOPNIA + PROCENT_NA_DRUGIEGO_STOPNIA))
                .różnica(pulaCzwartegoStopnia());
    }

    private Pieniądze nagrodaTrzeciegoStopnia()
    {
        int ilośćWygranych = ilościWygranych.get(3);

        if (ilośćWygranych == 0)
        {
            return Pieniądze.zero();
        }

        Pieniądze pula = pulaTrzeciegoStopnia();

        Pieniądze pojedynczaWygrana = pula.podzieloneNa(ilośćWygranych);

        Pieniądze minimalnaWygrana = Pieniądze.daj(36);

        if (pojedynczaWygrana.compareTo(minimalnaWygrana) <= 0)
        {
            return minimalnaWygrana;
        }

        return pojedynczaWygrana;
    }

    private Pieniądze nagrodaCzwartegoStopnia()
    {
        int ilośćWygranych = ilościWygranych.get(4);

        if (ilośćWygranych == 0)
        {
            return Pieniądze.zero();
        }

        return Pieniądze.daj(24);
    }


    private Pieniądze pulaPierwszegoStopnia()
    {
        Pieniądze pula = doPodziału.procent(PROCENT_NA_PIERWSZEGO_STOPNIA);
        Pieniądze minimalnaPula = Pieniądze.daj(2_000_000);
        if (pula.compareTo(minimalnaPula) <= 0)
        {
            pula = minimalnaPula;
        }
        return pula.suma(kumulacja);
    }

    public Pieniądze kumulacja()
    {
        int STOPIEŃ = 1;
        int ilośćWygranych = ilościWygranych.get(STOPIEŃ);

        if (ilośćWygranych == 0)
        {
            return pulaPierwszegoStopnia();
        }

        return Pieniądze.zero();
    }

}
