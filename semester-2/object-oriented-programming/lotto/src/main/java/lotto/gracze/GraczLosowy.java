package lotto.gracze;

import lotto.finanse.NiewystarczająceŚrodkiPieniężne;
import lotto.finanse.Pieniądze;
import lotto.losowania.Generator;
import lotto.losowania.GeneratorImpl;
import lotto.losowania.NadchodząceLosowanie;
import lotto.zakłady.Kupon;
import lotto.zarządzanie.Kolektura;

import java.util.List;

public class GraczLosowy extends Gracz
{
    private final List<Kolektura> listaKolektur;
    private final Generator generator = new GeneratorImpl();

    public GraczLosowy(List<Kolektura> listaKolektur, Osoba osoba, Pieniądze stanKonta)
    {
        super(osoba, stanKonta);
        this.listaKolektur = listaKolektur;
    }

    @Override
    public void zareagujNaNadchodząceLosowanie(NadchodząceLosowanie losowanie)
    {
        int indeksKolektury = generator.wylosujLiczbę(0, listaKolektur.size() - 1);
        Kolektura wybranaKolektura = listaKolektur.get(indeksKolektury);

        int liczbaKuponów = generator.wylosujLiczbę(1, 100);

        for (int i = 0; i < liczbaKuponów; i++)
        {
            int liczbaZakładów = generator.wylosujLiczbę(1, 8);
            int liczbaLosowań = generator.wylosujLiczbę(1, 10);

            try
            {
                Kupon kupon = wybranaKolektura.kupLosowy(liczbaZakładów, liczbaLosowań, this);
                kupony.add(kupon);
            }
            catch (NiewystarczająceŚrodkiPieniężne e)
            {
            }
        }
    }
}
