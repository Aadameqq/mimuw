package lib.losowania;

import lotto.finanse.Pieniądze;
import lotto.losowania.KalkulatorNagród;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KalkulatorNagródTest
{
    private final Pieniądze testowaPula = Pieniądze.daj(1_000_000);
    private KalkulatorNagród testowyKalkulator;

    @Test
    void kumulacjaPowinnaZwrócićZeroGdyKtośWygrałNagrodęPierwszegoStopnia()
    {
        Map<Integer, Integer> ilościWygranych = Map.of(1, 1);

        KalkulatorNagród kalkulator = utwórzTestowyKalkulator(ilościWygranych);

        assertEquals(Pieniądze.zero(), kalkulator.kumulacja());
    }

    @Test
    void kumulacjaPowinnaByćRównaPuliPierwszegoStopniaGdyBrakZwycięzców()
    {
        Map<Integer, Integer> ilościWygranych = Map.of(1, 0);

        KalkulatorNagród kalkulator = utwórzTestowyKalkulator(ilościWygranych);

        assertEquals(kalkulator.wygranaPierwszegoStopnia().pula(), kalkulator.kumulacja());
    }

    @Test
    void pulaPierwszegoStopniaPowinnaByćWyrównanaDoDwóchMilionówGdyMniejszaNiżDwaMiliony()
    {
        KalkulatorNagród kalkulator = utwórzTestowyKalkulator(Pieniądze.daj(8_000_000));

        assertEquals(Pieniądze.daj(2_000_000), kalkulator.wygranaPierwszegoStopnia().pula());
    }

    @Test
    void pulaPierwszegoStopniaPowinnaByćPrawidłowoObliczonaGdyNiemniejszaNiżDwaMiliony()
    {
        KalkulatorNagród kalkulator = utwórzTestowyKalkulator(Pieniądze.daj(10_000_000));

        assertEquals(Pieniądze.daj(2_244_000), kalkulator.wygranaPierwszegoStopnia().pula());
    }

    @Test
    void pulaPierwszegoStopniaPowinnaUwzględniaćKumulacjęDopieroPoWyrównaniuDoDwóchMilionów()
    {
        KalkulatorNagród kalkulator = utwórzTestowyKalkulator(Pieniądze.daj(1_000_000), Pieniądze.daj(3_000_000));

        assertEquals(Pieniądze.daj(5_000_000), kalkulator.wygranaPierwszegoStopnia().pula());
    }

    @Test
    void pulaDrugiegoStopniaPowinnaByćPrawidłowoObliczona()
    {
        KalkulatorNagród kalkulator = utwórzTestowyKalkulator(Pieniądze.daj(10_000_000));

        assertEquals(Pieniądze.daj(408_000), kalkulator.wygranaDrugiegoStopnia().pula());
    }

    @Test
    void pulaCzwartegoStopniaPowinnaByćPrawidłowoObliczona()
    {
        KalkulatorNagród kalkulator = utwórzTestowyKalkulator(Map.of(4, 123));

        assertEquals(Pieniądze.daj(2952), kalkulator.wygranaCzwartegoStopnia().pula());
    }

    @Test
    void pulaTrzeciegoStopniaPowinnaByćPrawidłowoObliczona()
    {
        int pulaPierwszegoStopnia = 224_400;
        int pulaDrugiegoStopnia = 40_800;
        int pulaCzwartegoStopnia = 2_952;
        int suma = pulaPierwszegoStopnia + pulaDrugiegoStopnia + pulaCzwartegoStopnia;
        KalkulatorNagród kalkulator = utwórzTestowyKalkulator(Map.of(3, 0, 4, 123));

        assertEquals(Pieniądze.daj(510_000 - suma), kalkulator.wygranaTrzeciegoStopnia().pula());
    }

    @Test
    void pojedyńczaWygranaTrzeciegoStopniaPowinnaByćWyrównanaGdyMniejszaNiżMinimalnaKwota()
    {
        KalkulatorNagród kalkulator = utwórzTestowyKalkulator(Map.of(3, 6720, 4, 123));

        assertEquals(Pieniądze.daj(36), kalkulator.wygranaTrzeciegoStopnia().pojedyńczaWygrana());
    }

    private KalkulatorNagród utwórzTestowyKalkulator(Map<Integer, Integer> ilościWygranych)
    {
        return new KalkulatorNagród(testowaPula, ilościWygranych, Pieniądze.zero());
    }

    private KalkulatorNagród utwórzTestowyKalkulator(Pieniądze pula)
    {
        return utwórzTestowyKalkulator(pula, Pieniądze.zero());
    }

    private KalkulatorNagród utwórzTestowyKalkulator(Pieniądze pula, Pieniądze kumulacja)
    {
        Map<Integer, Integer> ilościWygranych = Map.of(1, 0, 2, 0, 3, 0, 4, 0);
        return new KalkulatorNagród(pula, ilościWygranych, kumulacja);
    }

}
