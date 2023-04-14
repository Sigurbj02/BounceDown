package hi.vinnsla;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

/**
 * vinnsluklasi leiksins. Inniheldur aðferðir og fleira tengt stigum, hraða og notanda
 */
public class Leikur {
    private IntegerProperty stig = new SimpleIntegerProperty(0);//vaktanlegt gildi fyrir stig
    private IntegerProperty hradi = new SimpleIntegerProperty(1);//vaktanleggt gildi fyrir hraða
    private final int LEYFD_NAFNALENGD = 20; //fjöldi stafa sem mega vera í nafni
    private final int BIL = 4; //stærð bils sem á að vera á milli nafns og stiga á lista
    private StringProperty nafn = new SimpleStringProperty("");//ath hvort það þurfi að hafa StringProperty
    private ObservableList<String> sidustuLeikir = FXCollections.observableArrayList();//listi yfir síðustu leiki í tímaröð
    private ObservableList<String> toppListi = FXCollections.observableArrayList();//listi yfir hæðstu stig

    //smiður sem bætir við listener fyrir hraða til að auka hann
    public Leikur() {
        addListener();
    }

    //getterar og setterar
    public void setNafn(String nafn) {
        this.nafn.set(nafn);
    }

    public int getLEYFD_NAFNALENGD() {
        return LEYFD_NAFNALENGD;
    }

    public ObservableList<String> getToppListi() {
        return toppListi;
    }

    public ObservableList<String> getSidustuLeikir() {
        return sidustuLeikir;
    }


    //properties til að vakta gildi
    public IntegerProperty hradiProperty() {
        return hradi;
    }

    public IntegerProperty stigProperty() {
        return stig;
    }

    public StringProperty nafnProperty() {
        return nafn;
    }

    /**
     * bætir við listener á hraða
     */
    private void addListener() {
        stigProperty().addListener((observable, oldValue, newValue) -> {
            if (stig.get() % 100 == 0 && stig.get() != 0) {
                aukaHrada();
            }
        });
    }

    /**
     * setur nafn og stig á rétt form fyrir listana
     *
     * @param nafn - Strengur, nafn
     * @param stig - Heiltala, stig
     * @return - Strengur með nafni og stigum sem á að bæta á lista
     */
    private String formatFyrirLista(String nafn, int stig) {
        String skil = nafn;
        int lengd = nafn.length();//ath hvort .etta er nákvæmlega rétt
        for (int i = lengd; i < LEYFD_NAFNALENGD + BIL; i++) {
            skil = skil.concat(" ");
        }
        skil = skil + stig;
        return skil;
    }

    /**
     * aðferð til að bæta leik á listana. Upplýsingar settar á rétt form. Leik bætt við aftast á lista yfir
     * síðustu leiki. Ef það eru nú þegar 10 leikir á listanum er þeim elsta eytt og nýji settur aftast
     * Leik bætt við á réttan stað í topplistanum. Ef listinn er fullur er lægstu stigum eitt út
     * ef þau eru lægri en nýju stigin, og nýja leiknum bætt við
     *
     * @param nafn - Strengur, nafn leikmanns
     * @param stig - heiltala, stigafjöldi
     */
    public void baetaVidLeik(String nafn, int stig) {
        String formatted = formatFyrirLista(nafn, stig);
        if (sidustuLeikir.size() < 10) {
            sidustuLeikir.add(formatted);
        } else {
            sidustuLeikir.remove(0);
            sidustuLeikir.add(formatted);
        }
        if (toppListi.size() < 10) {
            toppListi.add(formatted);
            toppListi.sort(comparator.reversed());
        } else {
            int laegstuStig = Integer.parseInt(toppListi.get(9).substring(LEYFD_NAFNALENGD + BIL));
            if (stig > laegstuStig) {
                toppListi.remove(9);
                toppListi.add(formatted);
                toppListi.sort(comparator.reversed());
            }
        }
    }

    /**
     * Comparator fyrir stigin, sem byrja í sætinu leyfð nafnalengd+bil.
     */
    private Comparator<String> comparator = Comparator.comparing(s -> {
        String digits = s.substring(LEYFD_NAFNALENGD + BIL);
        return Integer.parseInt(digits);
    });


    /**
     * eykur hraðann um einn (gildi hraðans)
     */
    public void aukaHrada() {
        hradi.set(hradi.get() + 1);
    }

    /**
     * stillir stig og hraða upp á nýtt fyrir nýjan leik
     */
    public void leikLokid() {
        stig.set(0);
        hradi.set(1);
    }


    /**
     * stigin hækkuð um 1
     */
    public void haekkaStigin() {
        stig.setValue(stig.get() + 1);
    }
}
