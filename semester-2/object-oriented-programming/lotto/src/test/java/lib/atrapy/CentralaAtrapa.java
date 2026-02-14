package lib.atrapy;

import lotto.finanse.Pieniądze;
import lotto.losowania.Losowanie;
import lotto.zakłady.Kupon;
import lotto.zakłady.Zakład;
import lotto.zarządzanie.Centrala;
import lotto.zarządzanie.Kolektura;

import java.util.List;
import java.util.Optional;

public class CentralaAtrapa implements Centrala
{
    private Kolektura dodanaKolektura;
    private Kupon kupionyKupon;
    private Kupon zwracanyKupon;
    private Pieniądze wypłacanaNagroda;

    public void ustawZwracanyKupon(Kupon kupon)
    {
        this.zwracanyKupon = kupon;
    }

    public void ustawWypłacanąNagrodę(Pieniądze wypłacanaNagroda)
    {
        this.wypłacanaNagroda = wypłacanaNagroda;
    }

    public Kupon kupionyKupon()
    {
        return kupionyKupon;
    }

    @Override
    public Pieniądze fundusze()
    {
        return null;
    }

    @Override
    public void przyjmijPieniądzeZaKupon(Kupon kupon)
    {
        kupionyKupon = kupon;
    }

    @Override
    public Kupon utwórzKupon(int ilośćLosowań, Kolektura kolektura)
    {
        if (zwracanyKupon != null)
        {
            return zwracanyKupon;
        }
        return new Kupon(1, kolektura, 1, ilośćLosowań, new GeneratorAtrapa());
    }

    public Kupon utwórzKupon(int ilośćLosowań)
    {
        return utwórzKupon(ilośćLosowań, new KolekturaAtrapa());
    }

    @Override
    public void losuj()
    {

    }

    @Override
    public Optional<Losowanie> losowanie(int nr)
    {
        return Optional.empty();
    }

    @Override
    public void dodajKolekturę(Kolektura kolektura)
    {
        dodanaKolektura = kolektura;
    }

    @Override
    public List<Kolektura> kolektury()
    {
        return null;
    }

    @Override
    public Pieniądze wypłaćNagrodę(Zakład zakład, int nrLosowania)
    {
        return wypłacanaNagroda;
    }

    @Override
    public void przyjmijPieniądze(Pieniądze pieniądze)
    {

    }

    public Kolektura dodanaKolektura()
    {
        return dodanaKolektura;
    }
}
