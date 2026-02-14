package lotto.zakłady;

import java.util.ArrayList;
import java.util.List;

public class PoleZakładu
{
    private final static int ILOŚĆ_LICZB = 49;
    private final int numer;
    private final List<Kratka> kratki = new ArrayList<>();
    private final List<Integer> liczby = new ArrayList<>();
    private final Kratka kratkaAnulowania = new Kratka();
    private int ilośćZaznaczonych = 0;

    public PoleZakładu(int numer)
    {
        this.numer = numer;
        for (int nr = 1; nr <= ILOŚĆ_LICZB; nr++)
        {
            kratki.add(new Kratka(String.valueOf(nr)));
        }
    }

    public int numer()
    {
        return numer;
    }

    public void zaznacz(int liczba)
    {
        if (liczba < 1 || liczba > ILOŚĆ_LICZB)
        {
            throw new IllegalArgumentException("Liczba musi być z zakresu od 1 do " + ILOŚĆ_LICZB);
        }

        Kratka kratka = kratki.get(liczba - 1);
        if (kratka.czyZaznaczona()) return;

        ilośćZaznaczonych++;
        kratka.zaznacz();
        liczby.add(liczba);
    }

    public void anuluj()
    {
        kratkaAnulowania.zaznacz();
    }

    public int ilośćZaznaczonych()
    {
        return ilośćZaznaczonych;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(numer).append("\n");

        for (int indeks = 0; indeks < ILOŚĆ_LICZB; indeks++)
        {
            sb.append(kratki.get(indeks));
            if ((indeks + 1) % 10 == 0 || indeks == ILOŚĆ_LICZB - 1)
            {
                sb.append("\n");
            }
        }
        sb.append(kratkaAnulowania);
        sb.append(" anuluj\n");
        return sb.toString();
    }

    public boolean czyAnulowany()
    {
        return kratkaAnulowania.czyZaznaczona();
    }

    public boolean czyPoprawne()
    {
        return !czyAnulowany() && ilośćZaznaczonych() == 6;
    }

    public boolean czyZaznaczone(int liczba)
    {
        if (liczba < 1 || liczba > ILOŚĆ_LICZB)
        {
            return false;
        }
        return kratki.get(liczba - 1).czyZaznaczona();
    }

    public Zakład zamieńNaZakład() throws NieważnePoleZakładu
    {
        if (!czyPoprawne())
        {
            throw new NieważnePoleZakładu();
        }
        return new Zakład(liczby);
    }
}
