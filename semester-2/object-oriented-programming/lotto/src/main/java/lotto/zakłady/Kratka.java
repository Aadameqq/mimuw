package lotto.zakłady;

public class Kratka
{
    private final String wartość;
    private boolean czyZaznaczona = false;

    public Kratka(String wartość, boolean czyWyrównać)
    {
        if (wartość.length() > 2)
        {
            throw new IllegalArgumentException("Wartość kratki nie może być dłuższa niż 2 znaki.");
        }

        if (czyWyrównać)
        {
            while (wartość.length() < 2)
            {
                wartość = " " + wartość;
            }
        }
        this.wartość = wartość;
    }

    public Kratka(String wartość)
    {
        this(wartość, true);
    }

    public Kratka()
    {
        this("", true);
    }

    public void zaznacz()
    {
        czyZaznaczona = true;
    }

    public boolean czyZaznaczona()
    {
        return czyZaznaczona;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("[ ");
        if (czyZaznaczona)
        {
            sb.append("--");
        }
        else
        {
            sb.append(wartość);
        }
        sb.append(" ] ");

        return sb.toString();
    }
}
