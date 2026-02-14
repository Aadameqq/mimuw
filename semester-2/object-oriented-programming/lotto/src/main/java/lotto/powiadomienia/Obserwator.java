package lotto.powiadomienia;

import lotto.losowania.Losowanie;
import lotto.losowania.NadchodząceLosowanie;

public interface Obserwator
{
    void zareagujNaNadchodząceLosowanie(NadchodząceLosowanie losowanie);

    void zareagujNaZakończoneLosowanie(Losowanie losowanie);
}
