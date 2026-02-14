package lotto.zarządzanie;

import lotto.finanse.NiewystarczająceŚrodkiPieniężne;
import lotto.gracze.Gracz;
import lotto.zakłady.Blankiet;
import lotto.zakłady.Kupon;
import lotto.zakłady.KuponNieIstnieje;
import lotto.zakłady.KuponUżytyPonownie;

public interface Kolektura
{

    Kupon kup(Blankiet blankiet, Gracz gracz) throws NiewystarczająceŚrodkiPieniężne;

    Kupon kupLosowy(int ilośćZakładów, int ilośćLosowań, Gracz gracz) throws NiewystarczająceŚrodkiPieniężne;

    void wypłaćNagrodę(Kupon kupon, Gracz gracz) throws KuponNieIstnieje, KuponUżytyPonownie;

    int numer();
}
