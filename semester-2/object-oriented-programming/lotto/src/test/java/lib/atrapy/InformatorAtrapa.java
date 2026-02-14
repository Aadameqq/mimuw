package lib.atrapy;

import lotto.losowania.Losowanie;
import lotto.losowania.NadchodząceLosowanie;
import lotto.powiadomienia.Informator;
import lotto.powiadomienia.Obserwator;

import java.util.ArrayList;
import java.util.List;

public class InformatorAtrapa implements Informator
{

    private List<NadchodząceLosowanie> nadchodzące = new ArrayList<>();
    private List<Losowanie> przeprowadzone = new ArrayList<>();

    @Override
    public void zasubskrybuj(Obserwator obserwator)
    {

    }

    @Override
    public void poinformujONadchodzącymLosowaniu(NadchodząceLosowanie losowanie)
    {
        nadchodzące.add(losowanie);
    }

    @Override
    public void poinformujOZakończonymLosowaniu(Losowanie losowanie)
    {
        przeprowadzone.add(losowanie);
    }

    public List<NadchodząceLosowanie> nadchodzące()
    {
        return nadchodzące;
    }

    public List<Losowanie> przeprowadzone()
    {
        return przeprowadzone;
    }
}
