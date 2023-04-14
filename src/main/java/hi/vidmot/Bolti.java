package hi.vidmot;

import javafx.scene.image.ImageView;

/**
 * sérhæfður klasi sem erfir frá (extends) ImageView og implementar Leikhlutur interface. Fyrir boltann sem er notaður
 */
public class Bolti extends ImageView implements Leikhlutur {
    private Pallur boltaPallur;

    //smiður, kallar á FXML_Lestur til að lesa fxml skrá
    public Bolti() {
        FXML_Lestur.lesa(this, "bolti-view.fxml");
    }

    //getterar og setterar
    public Pallur getBoltaPallur() {
        return boltaPallur;
    }

    public void setBoltaPallur(Pallur boltaPallur) {
        this.boltaPallur = boltaPallur;
    }

    /**
     * Hreyfir boltann í þá stefnu sem hann vísar. Hann kemst ekki lengra ef hann er kominn alveg til hliðar á leikborðinu
     */
    public void afram() {
        double stefna = this.getRotate();
        Leikbord leikbord = (Leikbord) this.getParent();

        if (stefna == 0 && this.layoutXProperty().get() < leikbord.getWidth() - this.getFitWidth()) {
            this.layoutXProperty().set(layoutXProperty().get() + 5);
        } else if (stefna == 180 && this.layoutXProperty().get() > 0) {
            this.layoutXProperty().set(layoutXProperty().get() - 5);
        } else if (stefna == 270) {
            this.layoutYProperty().set(layoutYProperty().get() + 10);
        }
    }
}
