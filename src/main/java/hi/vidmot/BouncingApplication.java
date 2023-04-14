package hi.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Application fyrir leikinn. Gerir senu og les inn fxml skrá. Setur svið og sýnir það.
 * Kallað á aðferð fyrir virkni á örvatökkum
 */
public class BouncingApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BouncingApplication.class.getResource("bouncing-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("BounceDown!");

        BouncingController ctrl = fxmlLoader.getController();
        ctrl.orvatakkar(scene);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
