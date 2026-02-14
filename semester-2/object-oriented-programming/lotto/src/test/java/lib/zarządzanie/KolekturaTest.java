package lib.zarządzanie;

import lib.atrapy.CentralaAtrapa;
import lib.atrapy.GeneratorAtrapa;
import lib.atrapy.GraczAtrapa;
import lotto.finanse.NiewystarczająceŚrodkiPieniężne;
import lotto.finanse.Pieniądze;
import lotto.zakłady.*;
import lotto.zarządzanie.Kolektura;
import lotto.zarządzanie.KolekturaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KolekturaTest
{
    private CentralaAtrapa atrapaCentrali;
    private Kolektura kolektura;
    private final int nrKolektury = 3;
    private GraczAtrapa atrapaGracza;
    private Blankiet poprawnyBlankiet;
    private Kupon testowyKupon;
    private final int ilośćLosowańWBlankiecie = 5;

    @BeforeEach
    void skonfiguruj() throws NieważnePoleZakładu
    {
        atrapaCentrali = new CentralaAtrapa();

        poprawnyBlankiet = new Blankiet();
        PoleZakładu pole1 = poprawnyBlankiet.poleZakładu(1);
        pole1.zaznacz(1);
        pole1.zaznacz(2);
        pole1.zaznacz(3);
        pole1.zaznacz(4);
        pole1.zaznacz(5);
        pole1.zaznacz(6);

        poprawnyBlankiet.zaznaczIlośćLosowań(ilośćLosowańWBlankiecie);
        testowyKupon = atrapaCentrali.utwórzKupon(ilośćLosowańWBlankiecie);
        testowyKupon.dodajZakład(poprawnyBlankiet.poleZakładu(1).zamieńNaZakład());

        atrapaGracza = new GraczAtrapa();
        kolektura = new KolekturaImpl(atrapaCentrali, nrKolektury, new GeneratorAtrapa());
    }

    @Test
    void PowinnaZarejestrowaćSięWCentraliGdyTworzona()
    {
        assertEquals(nrKolektury, atrapaCentrali.dodanaKolektura().numer());
    }

    @Test
    void kupPowinnoWygenerowaćPoprawnyKuponNaPodstawieBlankietuPomijającNiepoprawnePola() throws NiewystarczająceŚrodkiPieniężne
    {
        atrapaGracza.przyjmijPieniądze(Pieniądze.daj(1_000_000));

        Blankiet blankiet = new Blankiet();
        PoleZakładu pole1 = blankiet.poleZakładu(1);
        pole1.anuluj();

        PoleZakładu pole2 = blankiet.poleZakładu(2);
        pole2.zaznacz(1);
        pole2.zaznacz(2);
        pole2.zaznacz(4);
        pole2.zaznacz(5);
        pole2.zaznacz(6);
        pole2.zaznacz(7);

        PoleZakładu pole3 = blankiet.poleZakładu(3);

        pole3.zaznacz(6);

        int ilośćLosowań = 5;
        blankiet.zaznaczIlośćLosowań(ilośćLosowań);

        Kupon kupon = kolektura.kup(blankiet, atrapaGracza);

        assertEquals(ilośćLosowań, kupon.ilośćLosowań());
        assertEquals(1, kupon.ilośćZakładów());
    }

    @Test
    void kupPowinnoZwrócićWyjątekGdyGraczNieMaWystarczającychŚrodków()
    {
        Pieniądze stanKonta = testowyKupon.cena().różnica(Pieniądze.daj(0, 1));
        atrapaGracza.przyjmijPieniądze(stanKonta);

        assertThrows(NiewystarczająceŚrodkiPieniężne.class, () -> kolektura.kup(poprawnyBlankiet, atrapaGracza));
    }

    @Test
    void kupPowinnoZwrócićKuponGdyGraczMaWystarczająceŚrodki()
    {
        Pieniądze stanKonta = testowyKupon.cena();
        atrapaGracza.przyjmijPieniądze(stanKonta);

        assertDoesNotThrow(() -> kolektura.kup(poprawnyBlankiet, atrapaGracza));
    }

    @Test
    void kupPowinnoPobraćOdGraczaPieniądzeGdyMaWystarczająceŚrodki() throws NiewystarczająceŚrodkiPieniężne
    {
        Pieniądze stanKonta = Pieniądze.daj(3_000);
        atrapaGracza.przyjmijPieniądze(stanKonta);

        kolektura.kup(poprawnyBlankiet, atrapaGracza);

        Pieniądze oczekiwanyStanKonta = stanKonta.różnica(testowyKupon.cena());

        assertEquals(oczekiwanyStanKonta, atrapaGracza.stanKonta());
    }

    @Test
    void kupPowinnoPoinformowaćCentralęOKuponie() throws NiewystarczająceŚrodkiPieniężne
    {
        atrapaCentrali.ustawZwracanyKupon(testowyKupon);
        atrapaGracza.przyjmijPieniądze(Pieniądze.daj(3_000));

        kolektura.kup(poprawnyBlankiet, atrapaGracza);

        assertNotNull(atrapaCentrali.kupionyKupon());
        assertEquals(testowyKupon.id(), atrapaCentrali.kupionyKupon().id());
    }

    @Test
    void wypłaćNagrodęPowinnoZwrócićWyjątekINicNieWypłacaćGdyKuponNieIstnieje()
    {
        atrapaCentrali.ustawWypłacanąNagrodę(Pieniądze.daj(1_000_000));


        assertThrows(KuponNieIstnieje.class, () -> kolektura.wypłaćNagrodę(testowyKupon, atrapaGracza));
        assertEquals(Pieniądze.zero(), atrapaGracza.stanKonta());
    }

    @Test
    void wypłaćNagrodęPowinnoZwrócićWyjątekINicNieWypłacaćGdyKuponZostałJużWypłacony() throws NiewystarczająceŚrodkiPieniężne, KuponNieIstnieje, KuponUżytyPonownie
    {
        atrapaGracza.przyjmijPieniądze(Pieniądze.daj(1_000));
        atrapaCentrali.ustawWypłacanąNagrodę(Pieniądze.daj(1_000_000));
        Kupon kupon = kolektura.kup(poprawnyBlankiet, atrapaGracza);

        kolektura.wypłaćNagrodę(kupon, atrapaGracza);

        atrapaGracza.wyczyśćKonto();

        assertThrows(KuponUżytyPonownie.class, () -> kolektura.wypłaćNagrodę(kupon, atrapaGracza));
        assertEquals(Pieniądze.zero(), atrapaGracza.stanKonta());
    }

    @Test
    void wypłaćNagrodęPowinnoWypłacićNagrodęZaKażdeLosowanieIZakładWKuponie() throws NiewystarczająceŚrodkiPieniężne, KuponNieIstnieje, KuponUżytyPonownie
    {
        PoleZakładu pole2 = poprawnyBlankiet.poleZakładu(2);
        pole2.zaznacz(1);
        pole2.zaznacz(2);
        pole2.zaznacz(3);
        pole2.zaznacz(4);
        pole2.zaznacz(5);
        pole2.zaznacz(6);

        atrapaGracza.przyjmijPieniądze(Pieniądze.daj(1_000));
        Pieniądze nagrodaZaZakład = Pieniądze.daj(1_000_000);
        atrapaCentrali.ustawWypłacanąNagrodę(nagrodaZaZakład);

        Kupon kupon = kolektura.kup(poprawnyBlankiet, atrapaGracza);
        atrapaGracza.wyczyśćKonto();

        kolektura.wypłaćNagrodę(kupon, atrapaGracza);

        int ilośćLosowań = 2;
        Pieniądze oczekiwanaNagroda = nagrodaZaZakład.iloczyn(ilośćLosowańWBlankiecie).iloczyn(ilośćLosowań);

        assertEquals(oczekiwanaNagroda, atrapaGracza.stanKonta());
    }
}
