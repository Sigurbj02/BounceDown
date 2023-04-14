package hi.vidmot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Sérhæfður klasi fyrir leikborð leiksins. Leikborðið inniheldur Bolta og Palla og extendar Pane
 */
public class Leikbord extends Pane {
    @FXML
    private Bolti bolti; //boltinn sem er notaður

    @FXML
    private Pane fxLeikbord; //Rót leikborðsins, Pane

    private ObservableList<Pallur> pallar = FXCollections.observableArrayList(); //vaktanlegur listi af pöllum á leikborðinu


    /**
     * smiður sem kallar á klasann FXML_Lestur til að lesa inn fxml skrá og þess háttar, og setur pallana
     */
    public Leikbord() {
        FXML_Lestur.lesa(this, "leikbord-view.fxml");
        setPallar();
    }

    /**
     * Setur alla palla á leikborðinu í observable lista
     */
    public void setPallar() {
        for (Node n : fxLeikbord.getChildren()) {
            if (n instanceof Pallur) {
                pallar.add((Pallur) n);
            }
        }
    }

    public Bolti getBolti() {
        return bolti;
    }

    public ObservableList<Pallur> getPallar() {
        return pallar;
    }

    /**
     * færir alla palla á leikborði upp
     */
    public void aframPallar() {
        for (Pallur p : pallar) {
            p.afram();
            athugaBoltiAPalli(p);
        }
    }

    /**
     * athugaer hvort boltinn hafi náð neðri jaðri leikborðsins
     *
     * @return - skilar true ef boltinn er neðst, annars false
     */
    public boolean erBoltiABotni() {
        return getBolti().layoutYProperty().get() > fxLeikbord.getHeight();
    }

    /**
     * setur boltann á skjáinn á þann stað sem hann á að vera á þegar nýr leikur hefst.
     */
    public void upphafsstillaBoltiLocation() {
        bolti.layoutXProperty().set(fxLeikbord.getWidth() / 2);
        bolti.layoutYProperty().set(50);
    }

    /**
     * setur pallana á skjáinn á þá staði sem þeir eiga að vera á þegar nýr leikur hefst
     */
    public void upphafsstillaPalla() {
        double heild = fxLeikbord.getHeight() + 100;
        int fjoldiPalla = pallar.size();
        double bil = heild / fjoldiPalla;

        for (int i = 0; i < fjoldiPalla; i++) {
            pallar.get(i).layoutYProperty().set(i * bil);
            pallar.get(i).layoutXProperty().set(Math.random() * (fxLeikbord.getWidth()) - pallar.get(i).getWidth());
        }
    }

    /**
     * Ef boltinn er á palli færist boltinn um x ás ef stefnan er vinstri eða hægri. Ef boltinn er ekki á
     * palli þá færist hann um eitt skref til hægri eða vinstri eftir stefnu og stefnan er svo sett sem niður
     */
    public void afram() {
        if (bolti.getBoltaPallur() != null) {
            if (bolti.getRotate() == 0 || bolti.getRotate() == 180) {
                bolti.afram();
            }
        } else if (bolti.getBoltaPallur() == null) {
            bolti.afram();
            bolti.setRotate(Stefna.NIDUR.getGradur());
        }
    }

    /**
     * athugar hvort bolti skarist við ákveðinn pall, skilar true eða false
     *
     * @param p - pallur sem athugað er með skörun
     * @return - sanngildi þess hvort bolti og pallur skarast
     */
    private boolean boltiSkarast(Pallur p) {
        return bolti.getBoundsInParent().intersects(p.getBoundsInParent());
    }

    /**
     * athugar hvort bolti rekst á pallinn p. Ef boltinn var að komast á pallinn er hann settur á
     * pallinn. Ef boltinn "rekst ekki" lengur á pallinn en var á pallinum, hentu þá boltanum af pallinum
     * Ef bolti er á palli getur hann í sumum tilfellum "klifrað" upp á pallinn við hliðina á honum
     * (it's a feature, not a bug)
     *
     * @param p - Pallur sem athugað er með
     */
    public void athugaBoltiAPalli(Pallur p) {
        if (boltiSkarast(p) && bolti.getBoltaPallur() == null && bolti.layoutYProperty().get() > -(bolti.getFitHeight())) {
            setjaBoltaAPall(p);
        } else if (bolti.getBoltaPallur() != null && !boltiSkarast(p)) {
            hendaBoltaAfPalli();
        } else if (bolti.layoutYProperty().get() < -(bolti.getFitHeight()) && bolti.getBoltaPallur() != null) {
            hendaBoltaAfPalli();
        }
    }

    /**
     * tengir pall p við bolta með því að setja tilviksbreytu í Bolti
     * bindur y gildið á palli p við yProperty á Bolti hlutnum
     *
     * @param p - Pallur sem boltinn er á
     */
    public void setjaBoltaAPall(Pallur p) {
        bolti.setBoltaPallur(p);
        bolti.layoutYProperty().bind(p.layoutYProperty().subtract(bolti.getFitHeight() - 5));
    }

    /**
     * fjarlægir binding milli bolta og pallsins sem hann var á, og setur tengdan pall sem null
     */
    public void hendaBoltaAfPalli() {
        bolti.layoutYProperty().unbind();
        bolti.setBoltaPallur(null);
    }
}
