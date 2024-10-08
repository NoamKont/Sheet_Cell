package client.component.login;




import client.component.main.MainMenu.AppController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import client.util.Constants;
import client.util.http.HttpClientUtil;

import java.io.IOException;

public class LoginController {

    @FXML
    public TextField userNameTextField;

    private AppController mainController;

    @FXML
    public void initialize() {
    }

    @FXML
    private  void loginBtnPressed(ActionEvent event) {

        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login failed");
            alert.setContentText("User name is empty. You can't login with empty user name");
            alert.showAndWait();

            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                        .parse(Constants.LOGIN_PAGE)
                        .newBuilder()
                        .addQueryParameter("username", userName)
                        .build()
                        .toString();

        System.out.println("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Login failed");
                                alert.setContentText(e.getMessage());
                                alert.showAndWait();
                            });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Login failed");
                        alert.setContentText(responseBody);
                        alert.showAndWait();
                    });
                } else {
                    Platform.runLater(() -> {
                        String s = userName.substring(0, 1).toUpperCase() + userName.substring(1).toLowerCase();
                        mainController.setUsername(s);
                        mainController.switchToDashboard();
                    });
                }
            }
        });
    }

    @FXML
    void exitBtnPressed(ActionEvent event) {
        Platform.exit();
    }

    public void setAppMainController(AppController MainController) {
        this.mainController = MainController;
    }
}
