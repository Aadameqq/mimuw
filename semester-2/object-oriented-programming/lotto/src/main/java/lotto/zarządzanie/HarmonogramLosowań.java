package lotto.zarządzanie;

import lotto.losowania.Losowanie;
import lotto.losowania.NadchodząceLosowanie;
import lotto.zakłady.Zakład;

import java.util.Optional;

public interface HarmonogramLosowań
{
    void przeprowadźLosowanie();

    Optional<Losowanie> losowanie(int nr);

    void dodajDoPuli(int nr, Zakład zakład);

    int numerNajbliższegoLosowania();

    Optional<NadchodząceLosowanie> nadchodzące(int nr);
}
