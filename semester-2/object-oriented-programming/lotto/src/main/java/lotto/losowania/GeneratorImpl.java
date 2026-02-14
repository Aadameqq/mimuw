package lotto.losowania;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class GeneratorImpl implements Generator
{
    private final Random random = new Random();

    public int wylosujLiczbę(int min, int maks)
    {
        return random.nextInt(maks - min + 1) + min;
    }

    public Set<Integer> wylosujSzóstkę()
    {
        Set<Integer> wylosowane = new TreeSet<>();
        while (wylosowane.size() < 6)
        {
            wylosowane.add(wylosujLiczbę(1, 49));
        }
        return wylosowane;
    }
}
