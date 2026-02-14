package lib.gracze;

import lib.atrapy.KolekturaAtrapa;
import lotto.finanse.Pieniądze;
import lotto.gracze.Osoba;
import lotto.losowania.NadchodząceLosowanie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraczStałyTest
{
    private TestowyGraczStały gracz1;
    private TestowyGraczStały gracz2;
    private KolekturaAtrapa atrapaKolektury1;
    private KolekturaAtrapa atrapaKolektury2;
    private KolekturaAtrapa atrapaKolektury3;
    private List<KolekturaAtrapa> kolektury;
    private final int testowaCzęstotliwość = 4;

    @BeforeEach
    void skonfiguruj()
    {
        atrapaKolektury1 = new KolekturaAtrapa();
        atrapaKolektury2 = new KolekturaAtrapa();
        atrapaKolektury3 = new KolekturaAtrapa();
        this.kolektury = List.of(atrapaKolektury1, atrapaKolektury2, atrapaKolektury3);

        gracz1 = new TestowyGraczStały(List.of(atrapaKolektury1), new Osoba("a", "b", "c"), Pieniądze.zero(), testowaCzęstotliwość);
        gracz2 = new TestowyGraczStały(List.of(atrapaKolektury1, atrapaKolektury2, atrapaKolektury3), new Osoba("a", "b", "c"), Pieniądze.zero(), 1);
    }

    @Test
    void powinienWziąćUdziałWPierwszymPoUtworzeniuLosowaniu()
    {
        gracz1.zareagujNaNadchodząceLosowanie(new NadchodząceLosowanie(7));

        assertFalse(atrapaKolektury1.kupioneBlankiety().isEmpty());
    }

    @Test
    void powinienWziąśćUdziałDopieroWCzwartymLosowaniuPoJegoPierwszym()
    {
        int pierwsze = 7;
        for (int i = pierwsze; i <= pierwsze + testowaCzęstotliwość - 1; i++)
        {
            gracz1.zareagujNaNadchodząceLosowanie(new NadchodząceLosowanie(i));
        }

        assertEquals(1, atrapaKolektury1.kupioneBlankiety().size());
        gracz1.zareagujNaNadchodząceLosowanie(new NadchodząceLosowanie(pierwsze + testowaCzęstotliwość));
        assertEquals(2, atrapaKolektury1.kupioneBlankiety().size());
    }

    @Test
    void powinienDokonywaćZakupuWKolejnychKolekturach()
    {
        sprawdźCykl(1);
        sprawdźCykl(2);
    }

    private void sprawdźCykl(int nr)
    {
        for (int i = 1; i <= 3; i++)
        {
            gracz2.zareagujNaNadchodząceLosowanie(new NadchodząceLosowanie(i + nr * 3 - 1));
            KolekturaAtrapa obecna = kolektury.get(i - 1);
            assertFalse(obecna.kupioneBlankiety().isEmpty());
            for (KolekturaAtrapa kolektura : kolektury)
            {
                if (kolektura == obecna)
                {
                    continue;
                }
                assertTrue(kolektura.kupioneBlankiety().isEmpty());
            }
            obecna.wyczyść();
        }
    }

}
