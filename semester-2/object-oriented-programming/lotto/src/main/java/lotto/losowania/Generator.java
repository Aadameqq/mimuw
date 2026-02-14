package lotto.losowania;

import java.util.Set;

public interface Generator
{
    int wylosujLiczbę(int min, int maks);

    Set<Integer> wylosujSzóstkę();
}
