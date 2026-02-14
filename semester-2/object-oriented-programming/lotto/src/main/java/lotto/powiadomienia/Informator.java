package lotto.powiadomienia;

import lotto.losowania.Losowanie;
import lotto.losowania.NadchodząceLosowanie;

public interface Informator
{
    void zasubskrybuj(Obserwator obserwator);

    void poinformujONadchodzącymLosowaniu(NadchodząceLosowanie losowanie);

    void poinformujOZakończonymLosowaniu(Losowanie losowanie);
}
