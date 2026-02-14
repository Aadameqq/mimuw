package lotto.finanse;

import java.util.Objects;

public class Pieniądze implements Comparable<Pieniądze>
{
    private final long złotówki;
    private final int grosze;
    private static Pieniądze zero;

    public static Pieniądze daj(long złotówki, int grosze)
    {
        if (grosze >= 100)
        {
            throw new IllegalArgumentException("Liczba groszy muszą być mniejsze niż 100");
        }
        if (złotówki < 0 || grosze < 0)
        {
            throw new IllegalArgumentException("Kwota nie może być ujemna");
        }

        return new Pieniądze(złotówki, grosze);
    }

    public static Pieniądze daj(long złotówki)
    {
        return new Pieniądze(złotówki);
    }

    public static Pieniądze zero()
    {
        if (zero == null)
        {
            zero = new Pieniądze();
        }
        return zero;
    }

    private Pieniądze(long złotówki, int grosze)
    {
        this.złotówki = złotówki + (grosze / 100) - (grosze < 0 ? 1 : 0);
        this.grosze = grosze % 100 + (grosze < 0 ? 100 : 0);

        if (this.złotówki < 0)
        {
            throw new IllegalArgumentException("Kwota nie może być ujemna");
        }
    }

    private Pieniądze(long złotówki)
    {
        this(złotówki, 0);
    }

    private Pieniądze()
    {
        this(0, 0);
    }

    public Pieniądze suma(Pieniądze inne)
    {
        return new Pieniądze(złotówki + inne.złotówki, grosze + inne.grosze);
    }

    public Pieniądze iloczyn(int skalar)
    {
        return new Pieniądze(złotówki * skalar, grosze * skalar);
    }

    public Pieniądze procent(int procent)
    {
        return new Pieniądze(złotówki * procent, grosze * procent).podzieloneNa(100);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(złotówki).append(" zł");

        if (grosze != 0)
        {
            sb.append(" ").append(grosze).append(" gr");
        }
        return sb.toString();
    }

    public Pieniądze różnica(Pieniądze inne)
    {
        return new Pieniądze(złotówki - inne.złotówki, grosze - inne.grosze);
    }

    public boolean czyZero()
    {
        return this.equals(zero);
    }

    public Pieniądze podzieloneNa(int dzielnik)
    {
        if (dzielnik < 0)
        {
            throw new ArithmeticException("Dzielenie przez liczbę niedodatnią");
        }

        int dodatkoweGrosze = (int) (((złotówki * 100) / dzielnik) % 100);
        return new Pieniądze(złotówki / dzielnik, grosze / dzielnik + dodatkoweGrosze);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Pieniądze pieniądze)) return false;
        return złotówki == pieniądze.złotówki && grosze == pieniądze.grosze;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(złotówki, grosze);
    }

    @Override
    public int compareTo(Pieniądze pieniądze)
    {
        if (złotówki == pieniądze.złotówki)
        {
            return Integer.compare(grosze, pieniądze.grosze);
        }
        return Long.compare(złotówki, pieniądze.złotówki);
    }
}
