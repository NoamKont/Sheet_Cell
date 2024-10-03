package client.component.dashboard;

import client.component.dashboard.sideBar.DashCommandsController;
import client.component.main.MainMenu.AppController;
import body.permission.PermissionInfo;
import body.Sheets.SheetInfo;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static body.permission.PermissionInfo.Permissions.*;
import static body.permission.PermissionInfo.Status.PENDING;
import static client.util.Constants.REFRESH_RATE;

public class DashboardController {

    private AppController appController;

    @FXML
    private Label usernameLabel;

    @FXML
    private TableView<PermissionInfo> permissionTable;

    @FXML
    private TableColumn<PermissionInfo, String> usernameCol;

    @FXML
    private TableColumn<PermissionInfo, String> perTypeCol;

    @FXML
    private TableColumn<PermissionInfo, String> perStatusCol;

    @FXML
    private BorderPane bodyComponent;

    @FXML
    private TableView<SheetInfo> availableSheets;

    @FXML
    private TableColumn<SheetInfo, String> ownerCol;

    @FXML
    private TableColumn<SheetInfo, PermissionInfo.Permissions> permissionCol;

    @FXML
    private TableColumn<SheetInfo, String> sheetNameCol;

    @FXML
    private TableColumn<SheetInfo, String> sheetSizeCol;

    private final BooleanProperty autoUpdate = new SimpleBooleanProperty(true);

    @FXML private GridPane commandsComponent;
    @FXML private DashCommandsController commandsComponentController;

    private Timer timer;
    private TimerTask listRefresher;

    private StringProperty selectedSheetName = new SimpleStringProperty();

    @FXML
    public void initialize() {
        // Set up the columns
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("sheetOwner"));
        sheetNameCol.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        sheetSizeCol.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));
        permissionCol.setCellValueFactory(new PropertyValueFactory<>("userPermission"));

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        perTypeCol.setCellValueFactory(new PropertyValueFactory<>("permissionType"));
        perStatusCol.setCellValueFactory(new PropertyValueFactory<>("permissionStatus"));

         if(commandsComponent != null){
            commandsComponentController.setDashController(this);
        }

         availableSheets.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
             if (newSelection != null) {
                 selectedSheetName.setValue(newSelection.getSheetName());
                 updatePermissionsList(newSelection.getAllUsersPermissionInfo());
                 if(newSelection.getUserPermission().equals(NO_PERMISSION.toString())||newSelection.getUserStatus().equals(PENDING.toString())){
                     commandsComponentController.getViewSheetBtn().setDisable(true);
                 }else {
                     commandsComponentController.getViewSheetBtn().setDisable(false);
                 }
             }
         });
    }

    @FXML
    void uploadFilePressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            System.out.println("File selected: " + selectedFile.getName());
            appController.createSheet(selectedFile.getAbsolutePath());
        }
    }

    private void updatePermissionsList(List<PermissionInfo> permissionsInfo) {
        Platform.runLater(() -> {
            ObservableList<PermissionInfo> items = permissionTable.getItems();
            items.clear();
            items.addAll(permissionsInfo);
        });
    }

    private void updateSheetsList(List<SheetInfo> sheetsInfo) {
        Platform.runLater(() -> {
            ObservableList<SheetInfo> items = availableSheets.getItems();
            items.clear();
            items.addAll(sheetsInfo);

        });
    }
    private void updateRequestsList(List<PermissionInfo> requestsInfo) {
        Platform.runLater(() -> {
            commandsComponentController.clearRequests();
            for(PermissionInfo permissionInfo : requestsInfo){
                try {
                    commandsComponentController.addRequest(permissionInfo);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void startListRefresher() {
        listRefresher = new SheetsListRefresher(
                autoUpdate,
                this::updateSheetsList,
                this::updateRequestsList,
                availableSheets.getItems(),
                appController.getUsername());
        timer = new Timer();
        timer.schedule(listRefresher, 0, REFRESH_RATE);
    }

    public void sheetChosen() {
        String selectedSheetName = getSelectedSheetName();
        appController.switchToSheetView(selectedSheetName);
        cancelTimerTask();

    }

    public String getSelectedSheetName() {
        return availableSheets.getSelectionModel().getSelectedItem().getSheetName();
    }

    public void setAppMainController(AppController appController) {
        this.appController = appController;
    }

    public TableView<SheetInfo> getAvailableSheets() {
        return availableSheets;
    }

    public void cancelTimerTask() {
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }


    public void requestBtnPressed() {
        //TODO its hard coded for now to "READER","Pending"

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.ADD_PERMISSION)
                .newBuilder()
                .addQueryParameter("sheetName", selectedSheetName.get())
                .addQueryParameter("permission", "READER")
                .addQueryParameter("status", "PENDING")
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
}
