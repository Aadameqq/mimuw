package lotto.zarządzanie;

import lotto.losowania.Losowanie;
import lotto.losowania.MaszynaLosująca;
import lotto.losowania.NadchodząceLosowanie;
import lotto.powiadomienia.Informator;
import lotto.zakłady.Zakład;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class HarmonogramLosowańImpl implements HarmonogramLosowań
{
    private final Informator informator;
    private final MaszynaLosująca maszynaLosująca;
    private final Map<Integer, Losowanie> przeprowadzoneLosowania = new HashMap<>();
    private final TreeMap<Integer, NadchodząceLosowanie> nadchodząceLosowania = new TreeMap<>();

    public HarmonogramLosowańImpl(Informator informator, MaszynaLosująca maszynaLosująca)
    {
        this.informator = informator;
        this.maszynaLosująca = maszynaLosująca;
        for (int nr = 1; nr <= 10; nr++)
        {
            nadchodząceLosowania.put(nr, new NadchodząceLosowanie(nr));
        }
    }

    public void przeprowadźLosowanie()
    {
        int numer = nadchodząceLosowania.firstKey();
        NadchodząceLosowanie nadchodząceLosowanie = nadchodząceLosowania.get(numer);

        informator.poinformujONadchodzącymLosowaniu(nadchodząceLosowanie);

        Losowanie losowanie = maszynaLosująca.przeprowadźLosowanie(nadchodząceLosowanie);

        nadchodząceLosowania.remove(numer);
        przeprowadzoneLosowania.put(numer, losowanie);

        informator.poinformujOZakończonymLosowaniu(losowanie);

        zaplanujNastępne();

        NadchodząceLosowanie kolejne = nadchodząceLosowania.get(nadchodząceLosowania.firstKey());
        kolejne.ustawKumulację(losowanie.kumulacjaDoKolejnego());
    }

    private void zaplanujNastępne()
    {
        int numer = nadchodząceLosowania.lastKey() + 1;
        nadchodząceLosowania.put(numer, new NadchodząceLosowanie(numer));
    }

    public Optional<Losowanie> losowanie(int nr)
    {
        if (!przeprowadzoneLosowania.containsKey(nr))
        {
            return Optional.empty();
        }
        return Optional.of(przeprowadzoneLosowania.get(nr));
    }

    public Optional<NadchodząceLosowanie> nadchodzące(int nr)
    {
        if (!nadchodząceLosowania.containsKey(nr))
        {
            return Optional.empty();
        }
        return Optional.of(nadchodząceLosowania.get(nr));
    }

    @Override
    public void dodajDoPuli(int nr, Zakład zakład)
    {
        NadchodząceLosowanie losowanie = nadchodząceLosowania.get(nr);
        losowanie.dodajDoPuli(zakład);
    }

    @Override
    public int numerNajbliższegoLosowania()
    {
        return nadchodząceLosowania.firstKey();
    }
}
