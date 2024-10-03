package client.component.dashboard.sideBar.request;

import body.permission.PermissionInfo;
import client.component.dashboard.DashboardController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PermissionRequestController {

    @FXML
    private Button acceptBtn;

    @FXML
    private Button denyBtn;

    @FXML
    private Text userNameFirstLatterLabel;

    @FXML
    private Label userNameLabel;

    private String sheetName;
    private PermissionInfo.Permissions userPermission;


    @FXML
    void acceptBtnPressed(ActionEvent event) {
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.ADD_PERMISSION)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .addQueryParameter("permission", userPermission.toString())
                .addQueryParameter("status", "APPROVED")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if(response.isSuccessful()) {
                    System.out.println("Request send successful");
                }
                else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error in sending request");
                        alert.setContentText(responseBody);
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Response is Failed");
            }
        });
    }

    @FXML
    void denyBtnPressed(ActionEvent event) {
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.ADD_PERMISSION)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .addQueryParameter("permission", userPermission.toString())
                .addQueryParameter("status", "REJECTED")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if(response.isSuccessful()) {
                    System.out.println("Request send successful");
                }
                else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error in sending request");
                        alert.setContentText(responseBody);
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Response is Failed");
            }
        });
    }

    public void setUserNameLabel(String userName) {
        userNameLabel.setText(userName);
        userNameFirstLatterLabel.setText(userName.substring(0,1).toUpperCase());
    }
    public void setUserPermission(PermissionInfo.Permissions permissionType) {
        this.userPermission = permissionType;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
