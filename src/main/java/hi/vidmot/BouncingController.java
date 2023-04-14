package hi.vidmot;

import hi.vinnsla.Leikur;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Optional;

/**
 * controller fyrir leikinn. Hefur samskipti við viðmótið, inniheldur handlera og setur stílklasa þegar við á
 */
public class BouncingController {
    @FXML
    public Leikbord fxLeikbord; //leikborðið, sérhæfður component. inniheldur bolta og palla

    @FXML
    private Label fxStig; //label sem sýnir stigafjölda í rauntíma

    @FXML
    private Label fxHradi; //label sem sýnir hraða í rauntíma

    @FXML
    private Label fxNafn; //label sem sýnir nafn leikmanns ef það hefur verið skráð

    private Leikur leikur;//hlutur af vinnsluklasanum Leikur

    private final HashMap<KeyCode, Stefna> map = new HashMap<>();//hakkatafla fyrir lyklaborð

    private Timeline timeline;//timeline fyrir leikinn

    private final int UPPHAFSHRADI = 50;//duration, hraði í upphafi

    private String validThema = "bleikt";//það þema sem er valið í byrjun

    private boolean leikurIGangi = false; //segir til um hvort það er leikur í gangi í augnablikinu

    /**
     * upphafsstillir leikur hlutinn, gerir viðeigandi bindings og listener, upphafsstillir þema leikborðs
     * og hefur leikinn
     */
    public void initialize() {
        leikur = new Leikur();//gerir nýjan leik
        geraBindings();
        hefjaLeik();
        setjaThema(validThema, "");
        hradaListener();
    }

    /**
     * gerir bindings til að sýna stigafjölda, hraða leiks og nafn leikmanns í viðmótinu
     */
    private void geraBindings() {
        fxStig.textProperty().bind(new SimpleStringProperty("Stig: ").concat(leikur.stigProperty().asString()));//bindur stigin við viðmótið
        fxHradi.textProperty().bind(new SimpleStringProperty("x").concat(leikur.hradiProperty().asString()));
        fxNafn.textProperty().bind(leikur.nafnProperty());
    }

    /**
     * leikjalykkjan sett upp. KeyFrame og TimeLine sett upp, með leikjaskrefum
     * Bolti færður, pallar færðir, stig uppfærð, athugað hvort leikur eigi að klárast
     */
    public void hefjaLeik() {
        KeyFrame k = new KeyFrame(Duration.millis(UPPHAFSHRADI),
                e -> {
                    fxLeikbord.afram();
                    fxLeikbord.aframPallar();
                    leikur.haekkaStigin();
                    if (fxLeikbord.erBoltiABotni()) {
                        leikLokid("Bolti féll niður");
                    }
                });
        makeAndSetTimeLine(k);
        leikurIGangi = true;
    }

    /**
     * gerir timeline fyrir leikinn og hefur hana
     *
     * @param k - Keyframe sem er notaður
     */
    private void makeAndSetTimeLine(KeyFrame k) {
        timeline = new Timeline(k);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * setur listener sem breytir rate á timeline eftir því sem hraðinn breytist
     */
    private void hradaListener() {
        leikur.hradiProperty().addListener((obs, oldVal, newVal) -> {
            timeline.setRate(1 + (newVal.doubleValue()) / 10);
        });
    }


    /**
     * aðferð fyrir leiklok. Stöðvar timeline, hendir bolta af palli. Sýnir dialog fyrir leiklok
     *
     * @param skilabod - Strengur, ástæða fyrir leiklokum
     */
    private void leikLokid(String skilabod) {
        int stig = leikur.stigProperty().get();
        leikur.leikLokid();
        timeline.stop();
        leikurIGangi = false;
        fxLeikbord.hendaBoltaAfPalli();
        Platform.runLater(() -> synaAlert(skilabod, leikur.nafnProperty().get(), stig));
    }


    /**
     * hefur nýjan leik. Byrjar timeline aftur og upphafsstillir stöðu bolta og palls á leikborðinu
     */
    public void nyrLeikur() {
        timeline.play();
        fxLeikbord.upphafsstillaBoltiLocation();
        fxLeikbord.upphafsstillaPalla();
        leikurIGangi = true;
    }


    /**
     * Sýnir dialog sem spyr notanda hvort hann vilji leika annan leik.
     * Hefur nýjan leik ef svo er
     *
     * @param skilabod  - Strengur, ástæða fyrir leiklokum
     * @param leikmadur - Strengur, nafn leikmanns. Tómur strengur ef ekkert nafn hefur verið skráð
     * @param stig      - heiltala, stigafjöldi
     */
    public void synaAlert(String skilabod, String leikmadur, int stig) {
        LeiklokDialog a = new LeiklokDialog(skilabod, leikmadur, stig, leikur.getLEYFD_NAFNALENGD());
        Optional<Triple<String, Boolean, ButtonType>> svar = a.showAndWait();
        if (svar.isPresent()) {
            if (svar.get().getSecond()) {
                leikur.baetaVidLeik(svar.get().getFirst(), stig);
                leikur.setNafn(svar.get().getFirst());
            }
            if (svar.get().getThird() == a.getNyrLeikurTakki()) {
                nyrLeikur();
            }
        }
    }

    /**
     * aðferð til að tengja örvatakka svo hægt sé að nota þá
     *
     * @param s - sena leiksins
     */
    public void orvatakkar(Scene s) {
        map.put(KeyCode.LEFT, Stefna.VINSTRI);
        map.put(KeyCode.RIGHT, Stefna.HAEGRI);
        s.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            try {
                this.fxLeikbord.getBolti().setRotate(map.get(event.getCode()).getGradur());
                fxLeikbord.afram();
            } catch (Exception e) {
                event.consume();
            }
        });
    }

    /**
     * prófunaraðferð fyrir færslu á boltanum. Ekki notað
     */
    public void testBolti() {
        fxLeikbord.getBolti().setRotate(Stefna.HAEGRI.getGradur());
        for (int i = 0; i < 50; i++) {
            fxLeikbord.getBolti().afram();
        }
    }

    /**
     * prófunaraðferð fyrir færslu á pöllum. Ekki notað
     */
    public void testPallar() {
        for (int i = 0; i < 50; i++) {
            fxLeikbord.aframPallar();
        }
    }

    /**
     * setur þema á palla
     *
     * @param setja - nýja þemað sem á að setja á
     * @param taka  - gamla þemað sem á að taka af
     */
    private void themaAPalla(String setja, String taka) {
        for (Node n : fxLeikbord.getPallar()) {
            n.getStyleClass().remove("pallar-" + taka);
            n.getStyleClass().add("pallar-" + setja);
        }
    }

    /**
     * setur þema á bakgrunn
     *
     * @param setja - nýja þemað sem á að setja á
     * @param taka  - gamla þemað sem á að taka af
     */
    private void themaABakgrunn(String setja, String taka) {
        fxLeikbord.getStyleClass().remove("leikbord-" + taka);
        fxLeikbord.getStyleClass().add("leikbord-" + setja);
    }

    /**
     * setur þema á labels
     *
     * @param setja - nýja þemað sem á að setja á
     * @param taka  - gamla þemað sem á að taka af
     */
    private void themaALabels(String setja, String taka) {
        fxNafn.getStyleClass().remove("labels-" + taka);
        fxNafn.getStyleClass().add("labels-" + setja);
        fxHradi.getStyleClass().remove("labels-" + taka);
        fxHradi.getStyleClass().add("labels-" + setja);
        fxStig.getStyleClass().remove("labels-" + taka);
        fxStig.getStyleClass().add("labels-" + setja);
    }

    /**
     * setur þema á ramma
     *
     * @param setja - nýja þemað sem á að setja á
     * @param taka  - gamla þemað sem á að taka af
     */
    private void themaARamma(String setja, String taka) {
        BorderPane root = (BorderPane) fxLeikbord.getParent();
        for (Node n : root.getChildren()) {
            if (n.getId() != null && n.getId().equals("efriRammi") || n.getId().equals("nedriRammi")) {
                n.getStyleClass().remove("umgjord-" + taka);
                n.getStyleClass().add("umgjord-" + setja);
            }
        }
    }

    /**
     * kallar á aðferðir til að setja þema á ýmsa hluta viðmótsins
     *
     * @param setja - nýja þemað sem á að setja á
     * @param taka  - gamla þemað sem á að taka af
     */
    public void setjaThema(String setja, String taka) {
        themaAPalla(setja, taka);
        themaABakgrunn(setja, taka);
        themaALabels(setja, taka);
        themaARamma(setja, taka);
    }


    /**
     * aðferð sem birtir dialog með mismunandi upplýsingum. Hefur ekki sér klasa. Tekur inn tvo strengi
     *
     * @param skilabod - Strengur, megintexti dialogsins
     * @param titill   - Strengur, titill dialogsins
     */
    public void birtaUppl(String skilabod, String titill) {
        Alert uppl = new Alert(Alert.AlertType.NONE, "", ButtonType.OK);
        uppl.setContentText(skilabod);
        uppl.initStyle(StageStyle.UNDECORATED);
        uppl.getDialogPane().getStylesheets().add(getClass().getResource("bouncingball.css").toExternalForm());
        uppl.getDialogPane().getStyleClass().add("dialog");
        uppl.setHeaderText(titill);
        uppl.showAndWait();
    }


    /**
     * atburðahandler sem opnar dialog með listview af leikjum með hæðstu stigin
     *
     * @param actionEvent - menuitem sem ýtt var á
     */
    public void opnaTopplistaHandler(ActionEvent actionEvent) {
        StigataflaDialog tafla = new StigataflaDialog("Hæðstu stig", leikur.getToppListi());
        tafla.showAndWait();
    }

    /**
     * atburðahandler sem opnar dialog með listview af síðustu leikjum
     *
     * @param actionEvent - menuitem sem ýtt var á
     */
    public void opnaSidustuLeikiListaHandler(ActionEvent actionEvent) {
        StigataflaDialog tafla = new StigataflaDialog("Síðustu leikir", leikur.getSidustuLeikir());
        tafla.showAndWait();
    }

    /**
     * atburðahandler sem breytir þemanu á leikborðinu
     *
     * @param event - menuitemið sem er valið
     */
    public void veljaThemaHandler(Event event) {
        String thema = ((MenuItem) event.getSource()).getId();
        setjaThema(thema, validThema);
        validThema = thema;
    }

    /**
     * atburðahandler sem opnar dialog til að setja inn nafn leikmanns
     *
     * @param actionEvent - menuitemið valið
     */
    public void leikmadurHandler(ActionEvent actionEvent) {
        TextInputDialog t = new NafnDialog(leikur.nafnProperty().get(), leikur.getLEYFD_NAFNALENGD());
        Optional<String> name = t.showAndWait();
        name.ifPresent(s -> leikur.setNafn(s));
    }

    /**
     * atburðahandler sem byrjar nýjan leik, og klárar núverandi leik ef hann er enn í gangi
     *
     * @param actionEvent - menuitemið valið
     */
    public void nyrLeikurHandler(ActionEvent actionEvent) {
        if (!leikurIGangi) {
            nyrLeikur();
        } else leikLokid("Leikmaður hætti leik");
    }

    /**
     * Atburðahandler sem opnar dialog sem hefur upplýsingar um leikinn
     *
     * @param actionEvent - menuitemið valið
     */
    public void umLeikinnHandler(ActionEvent actionEvent) {
        birtaUppl(Skilabod.UM_LEIKINN.getTexti(), Skilabod.UM_LEIKINN.getTitill());
    }

    /**
     * Atburðahandler sem opnar dialog sem hefur upplýsingar um uppfylltar kröfur
     *
     * @param actionEvent - menuitemið valið
     */
    public void uppfylltarKrofurHandler(ActionEvent actionEvent) {
        birtaUppl(Skilabod.UPPFYLLTAR_KROFUR.getTexti(), Skilabod.UPPFYLLTAR_KROFUR.getTitill());
    }

    /**
     * Atburðahandler sem opnar dialog með ósk um einkun
     *
     * @param actionEvent - menuitemið valið
     */
    public void oskUmEinkunHandler(ActionEvent actionEvent) {
        birtaUppl(Skilabod.OSK_UM_EINKUN.getTexti(), Skilabod.OSK_UM_EINKUN.getTitill());
    }

    /**
     * Atburðahandler sem birtir dialog með leikreglum
     *
     * @param actionEvent - menuitemið valið
     */
    public void leikreglurHandler(ActionEvent actionEvent) {
        birtaUppl(Skilabod.LEIKREGLUR.getTexti(), Skilabod.LEIKREGLUR.getTitill());
    }

    /**
     * atburðahandler sem lokar leiknum
     *
     * @param actionEvent - menuitemið valið
     */
    public void lokaHandler(ActionEvent actionEvent) {
        Platform.exit();
    }
}
