package hi.vidmot;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;

/**
 * TextInputDialog til að setja inn nafn leikmanns.
 */
public class NafnDialog extends TextInputDialog {
    /**
     * smiðurinn fyrir dialoginn. Nafn sem sett er inn getur ekki verið lengra en leyfð lengd. setur útlit
     *
     * @param nafn       - Strengur, nafn sem hefur verið bætt við áður
     * @param leyfdLengd - heiltala, leyfð lengd nafns
     */
    public NafnDialog(String nafn, int leyfdLengd) {
        setTitle("Nýr leikmaður");
        setHeaderText("Hvað heitir þú?");
        setContentText("Nafn: ");

        getEditor().setText(nafn);
        getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= leyfdLengd) {
                return change;
            } else {
                return null;
            }
        }));
    }
}
