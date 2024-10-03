package client.component.dashboard.sideBar;

import body.impl.CoordinateImpl;
import body.permission.PermissionInfo;
import client.component.dashboard.DashboardController;
import client.component.dashboard.sideBar.request.PermissionRequestController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.SheetDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static client.util.Constants.GSON_INSTANCE;

public class DashCommandsController implements Initializable {

    @FXML
    private Button requestBtn;

    @FXML
    private GridPane requestsGrid;

    @FXML
    private Button viewSheetBtn;

    private DashboardController dashController;

    private  int numberOfRequests = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewSheetBtn.setDisable(true);
    }

    @FXML
    void requestBtnPressed(ActionEvent event) {
        dashController.requestBtnPressed();

    }

    @FXML
    void viewSheetPressed(ActionEvent event) {
        dashController.sheetChosen();
    }

    public void setDashController(DashboardController dashController) {
        this.dashController = dashController;
        //viewSheetBtn.disableProperty().bind(dashController.getAvailableSheets().getSelectionModel().selectedItemProperty().isNull());
    }

    public Button getViewSheetBtn() {
        return viewSheetBtn;
    }

    public void addRequest(PermissionInfo permissionInfo) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("request/PermissionRequest.fxml");
        fxmlLoader.setLocation(url);
        HBox root = fxmlLoader.load(url.openStream());
        PermissionRequestController permissionRequestController = fxmlLoader.getController();
        permissionRequestController.setUserNameLabel(permissionInfo.getUsername());
        permissionRequestController.setSheetName(permissionInfo.getSheetName());
        permissionRequestController.setUserPermission(permissionInfo.getPermissionType());
        GridPane.setRowIndex(root, numberOfRequests);
        requestsGrid.getChildren().add(root);
        numberOfRequests++;

    }

    public void clearRequests() {
        requestsGrid.getChildren().clear();
        numberOfRequests = 0;
    }
}
