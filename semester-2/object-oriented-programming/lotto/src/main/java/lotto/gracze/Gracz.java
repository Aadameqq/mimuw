package lotto.gracze;

import lotto.finanse.Pieniądze;
import lotto.losowania.Losowanie;
import lotto.powiadomienia.Obserwator;
import lotto.zakłady.Kupon;
import lotto.zakłady.KuponNieIstnieje;
import lotto.zakłady.KuponUżytyPonownie;
import lotto.zakłady.Zakład;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Gracz implements Obserwator
{
    protected final List<Kupon> kupony = new ArrayList<>();
    private Pieniądze stanKonta;
    private final Osoba osoba;

    protected Gracz(Osoba osoba, Pieniądze stanKonta)
    {
        this.osoba = osoba;
        this.stanKonta = stanKonta;
    }

    public Pieniądze stanKonta()
    {
        return stanKonta;
    }

    public void dajPieniądze(Pieniądze pieniądze)
    {
        stanKonta = stanKonta.różnica(pieniądze);
    }

    public void przyjmijPieniądze(Pieniądze pieniądze)
    {
        stanKonta = stanKonta.suma(pieniądze);
    }

    @Override
    public void zareagujNaZakończoneLosowanie(Losowanie losowanie)
    {
        Iterator<Kupon> it = kupony.iterator();
        while (it.hasNext())
        {
            Kupon kupon = it.next();
            if (!czyOdebrać(kupon, losowanie.numer())) continue;
            if (!czyKuponWygrał(losowanie, kupon)) continue;

            try
            {
                kupon.kolektura().wypłaćNagrodę(kupon, this);
            }
            catch (KuponNieIstnieje e)
            {
            }
            catch (KuponUżytyPonownie e)
            {
            }
            
            it.remove();
        }
    }

    private static boolean czyKuponWygrał(Losowanie losowanie, Kupon kupon)
    {
        for (Zakład zakład : kupon.zakłady())
        {
            if (!losowanie.obliczNagrodę(zakład).czyZero())
            {
                return true;
            }
        }
        return false;
    }

    protected boolean czyOdebrać(Kupon kupon, int nrObecnego)
    {
        for (int numer : kupon.numeryLosowań())
        {
            if (nrObecnego < numer)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(osoba).append("\n")
                .append("Stan konta: ").append(stanKonta).append("\n")
                .append("Kupony: ");

        if (kupony.isEmpty())
        {
            sb.append("brak");
        }
        else
        {
            int numer = 1;
            for (Kupon kupon : kupony)
            {
                sb.append("\n").append(numer++).append(". ").append(kupon.id());
            }
        }
        return sb.toString();
    }
}
