package hi.vidmot;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

/**
 * Klasi fyrir dialog sem birtist í lok leiks. Úr dialognum fæst þrenna (triple) af String, Boolean og Buttontype.
 * Strengurinn inniheldur nafn leikmanns, boolean segir til um hvort ýtt hafi verið á Bæta við takkann og Buttontype
 * segir til um hvorn takkanna í buttonbar ýtt var á
 */
public class LeiklokDialog extends Dialog<Triple<String, Boolean, ButtonType>> {
    private ButtonType nyrLeikurTakki = new ButtonType("Nýr Leikur", ButtonBar.ButtonData.OTHER);//gerð takka sem gerir nýjan leik
    private ButtonType tilBakaTakki = new ButtonType("Til baka", ButtonBar.ButtonData.OTHER);//gerð takka sem fer til baka
    private Button baetaVidTakki = new Button("Bæta við");//takki til að bæta við stigum.
    private TextField nafnReitur = new TextField();//reitur sem hægt er að skrifa nafn í
    private boolean sendaInnStig = false;//rökbreyta sem segir hvort búið er að senda inn stig
    private Label nafnLabel = new Label(); //label sem segir hvar nafnið kemur
    private Label stigLabel = new Label();//label með upplýsingum um stig

    /**
     * smiður fyrir klasann. Kallar á fjórar aðferðir
     *
     * @param skilabod   - Strengur, skilaboð sem á að birta í dialoginum
     * @param nafn       - Strengur, nafn leikmanns, getur verið tómur strengur
     * @param stig       - heiltala, stigafjöldi
     * @param leyfdLengd - heiltala, leyfð lengd nafns
     */
    public LeiklokDialog(String skilabod, String nafn, int stig, int leyfdLengd) {
        geraUtlit(skilabod, nafn, stig);
        geraTextFormatter(leyfdLengd);
        geraEventFilter();
        setResultConverter();
    }

    /**
     * gerir útlitið á dialoginn
     *
     * @param skilabod - Strengur, skilaboð (ástæða fyrir leiklokum)
     * @param nafn     - Strengur, nafn leikmanns
     * @param stig     - heiltala, stigafjöldi
     */
    private void geraUtlit(String skilabod, String nafn, int stig) {
        DialogPane p = new DialogPane();
        nafnLabel.textProperty().set("Nafn: ");
        stigLabel.textProperty().set("Stig: " + stig);
        baetaVidTakki.disableProperty().bind(nafnReitur.textProperty().isEmpty());

        initStyle(StageStyle.UNDECORATED);
        VBox v = new VBox();
        nafnReitur.textProperty().set(nafn);
        v.getChildren().add(new HBox(nafnLabel, nafnReitur));
        v.getChildren().add(stigLabel);
        v.getChildren().add(baetaVidTakki);
        setHeaderText(skilabod);
        getDialogPane().setContent(v);
        p.getButtonTypes().addAll(nyrLeikurTakki, tilBakaTakki);
        getDialogPane().getButtonTypes().addAll(nyrLeikurTakki, tilBakaTakki);
    }

    /**
     * gerir eventfilter fyrir það þegar ýtt er á takka til að bæta við stigum.
     * Gerir takkann virkan/óvirkan eftir því sem við á
     */
    private void geraEventFilter() {
        baetaVidTakki.addEventFilter(ActionEvent.ACTION, (Event event) -> {
            if (!sendaInnStig && !nafnReitur.textProperty().getValue().equals("")) {
                baetaVidTakki.disableProperty().unbind();
                sendaInnStig = true;
                baetaVidTakki.setDisable(true);
            }
        });
    }

    /**
     * gerir formatter svo að það sé ekki hægt að slá inn endalaust í reitinn
     *
     * @param leyfdLengd - hámarksfjöldi stafa í reitnum
     */
    private void geraTextFormatter(int leyfdLengd) {
        nafnReitur.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= leyfdLengd) {
                return change;
            } else {
                return null;
            }
        }));
    }

    /**
     * setur resultconverter fyrir skilagildi dialogsins
     */
    private void setResultConverter() {
        javafx.util.Callback<ButtonType, Triple<String, Boolean, ButtonType>> tripleResultConverter = param -> {
            if (param == nyrLeikurTakki) {
                return new Triple<>(nafnReitur.textProperty().get(), sendaInnStig, nyrLeikurTakki);
            } else if (param == tilBakaTakki) {
                return new Triple<>(nafnReitur.textProperty().get(), sendaInnStig, tilBakaTakki);
            } else return null;
        };
        setResultConverter(tripleResultConverter);
    }

    //getter
    public ButtonType getNyrLeikurTakki() {
        return nyrLeikurTakki;
    }
}

