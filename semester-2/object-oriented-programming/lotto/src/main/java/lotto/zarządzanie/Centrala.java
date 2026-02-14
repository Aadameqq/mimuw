package lotto.zarządzanie;

import lotto.finanse.Pieniądze;
import lotto.losowania.Losowanie;
import lotto.zakłady.Kupon;
import lotto.zakłady.Zakład;

import java.util.List;
import java.util.Optional;

public interface Centrala
{
    Pieniądze fundusze();

    void przyjmijPieniądzeZaKupon(Kupon kupon);

    Kupon utwórzKupon(int ilośćLosowań, Kolektura kolektura);

    void losuj();

    Optional<Losowanie> losowanie(int nr);

    void dodajKolekturę(Kolektura kolektura);

    List<Kolektura> kolektury();

    Pieniądze wypłaćNagrodę(Zakład zakład, int nrLosowania);

    void przyjmijPieniądze(Pieniądze pieniądze);
}
