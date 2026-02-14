package lotto.gracze;

public class Osoba
{
    private final String imię;
    private final String nazwisko;
    private final String pesel;

    public Osoba(String imię, String nazwisko, String pesel)
    {
        this.imię = imię;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
    }

    public String imię()
    {
        return imię;
    }

    public String nazwisko()
    {
        return nazwisko;
    }

    public String pesel()
    {
        return pesel;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s (%s)", imię, nazwisko, pesel);
    }
}
