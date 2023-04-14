package hi.vidmot;

import javafx.scene.shape.Rectangle;

/**
 * Sérhæfður klasi sem extendar Rectangle og erfir frá Leikhlutur, gerir palla sem koma á leikborðið
 */
public class Pallur extends Rectangle implements Leikhlutur {

    //smiður, les úr fxml skrá o.fl.
    public Pallur() {
        FXML_Lestur.lesa(this, "pallur-view.fxml");
    }

    /**
     * afram útfært fyrir palla, færir pallana upp. Ef pallur er kominn upp fyrir er hann látinn koma
     * aftur niðri, en á slembistað á x-ás
     */
    public void afram() {
        Leikbord leikbord = (Leikbord) getParent();
        double tempY = this.getLayoutY() - 5;
        this.setLayoutY(tempY);
        if (tempY <= -(this.getHeight())) {
            this.setLayoutX(Math.random() * (leikbord.getWidth() - this.getWidth()));
            this.setLayoutY(leikbord.getHeight() + 100);
        }
    }
}
