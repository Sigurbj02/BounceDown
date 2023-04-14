package hi.vidmot;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

/**
 * dialog fyrir stigatöflu, extendar Alert (þarf ekki að skila neinu)
 */
public class StigataflaDialog extends Alert {

    private ButtonType tilBaka = new ButtonType("Til baka", ButtonBar.ButtonData.CANCEL_CLOSE);//gerð hnapps í dialog

    /**
     * smiðurinn fyrir klasann. Tekur inn titil og lista sem er sýndur í ListView. Setur útlitið
     *
     * @param listaSkyring - Strengur, titillinn
     * @param listi        - Vaktanlegur listi, sýndur í ListView
     */
    public StigataflaDialog(String listaSkyring, ObservableList<String> listi) {
        super(AlertType.NONE);
        ListView<String> listView = new ListView<>(listi);
        listView.setStyle("-fx-font-family: consolas;");
        getDialogPane().setHeaderText(listaSkyring);
        getDialogPane().setContent(listView);
        getDialogPane().getButtonTypes().add(tilBaka);
        listView.setFocusTraversable(false);

    }
}
