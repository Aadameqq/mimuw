package lotto.losowania;

import lotto.finanse.Pieniądze;

public class Wygrana
{
    private final int stopieńWygranej;
    private final int ilośćWygranych;
    private final Pieniądze pojedyńczaWygrana;
    private final Pieniądze pula;

    public Wygrana(int stopieńWygranej, int ilośćWygranych, Pieniądze pojedyńczaWygrana, Pieniądze pula)
    {
        this.stopieńWygranej = stopieńWygranej;
        this.ilośćWygranych = ilośćWygranych;
        this.pojedyńczaWygrana = pojedyńczaWygrana;
        this.pula = pula;
    }

    public int stopieńWygranej()
    {
        return stopieńWygranej;
    }

    public int ilośćWygranych()
    {
        return ilośćWygranych;
    }

    public Pieniądze pojedyńczaWygrana()
    {
        return pojedyńczaWygrana;
    }

    public Pieniądze pula()
    {
        return pula;
    }
}
