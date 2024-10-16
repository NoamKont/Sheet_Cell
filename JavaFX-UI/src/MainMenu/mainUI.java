package MainMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class mainUI extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Live Example");

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("app.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        AppController controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 1010, 680);

        controller.setScene(scene);

        scene.getStylesheets().add(getClass().getResource("resources/defaultTheme/default.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(mainUI.class);
    }
}