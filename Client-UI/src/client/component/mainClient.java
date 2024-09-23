package client.component;

import client.component.main.MainMenu.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class mainClient extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Live Example");

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("main/MainMenu/clientApp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        AppController controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 1010, 680);

        controller.setScene(scene);
        controller.setStage(primaryStage);

        scene.getStylesheets().add(getClass().getResource("main/MainMenu/resources/defaultTheme/default.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        controller.loadLoginPage();
    }

    public static void main(String[] args) {
        launch(mainClient.class);
    }

}