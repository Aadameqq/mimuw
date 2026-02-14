package lotto.finanse;

public class BudżetPaństwa
{
    private Pieniądze kwotaPodatków = Pieniądze.zero();
    private Pieniądze kwotaSubwencji = Pieniądze.zero();

    public void pobierzSubwencję(Pieniądze kwota)
    {
        kwotaSubwencji = kwotaSubwencji.suma(kwota);
    }

    public void przyjmijPodatek(Pieniądze kwota)
    {
        kwotaPodatków = kwotaPodatków.suma(kwota);
    }

    public Pieniądze łącznaKwotaPodatków()
    {
        return kwotaPodatków;
    }

    public Pieniądze łącznaKwotaSubwencji()
    {
        return kwotaSubwencji;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Łączna kwota podatków: ")
                .append(łącznaKwotaPodatków())
                .append("\n")
                .append("Łączna kwota subwencji: ")
                .append(łącznaKwotaSubwencji());
        return sb.toString();
    }
}
