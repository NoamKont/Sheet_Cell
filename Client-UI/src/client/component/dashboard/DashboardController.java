package client.component.dashboard;

import client.component.dashboard.sideBar.DashCommandsController;
import client.component.main.MainMenu.AppController;
import body.permission.PermissionInfo;
import client.component.main.UIbody.SheetInfo;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private TableColumn<SheetInfo, String> permissionCol;

    @FXML
    private TableColumn<SheetInfo, String> sheetNameCol;

    @FXML
    private TableColumn<SheetInfo, String> sheetSizeCol;

    private final BooleanProperty autoUpdate = new SimpleBooleanProperty(true);

    @FXML private VBox commandsComponent;
    @FXML private DashCommandsController commandsComponentController;

    @FXML
    public void initialize() {
        // Set up the columns
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("sheetOwner"));
        sheetNameCol.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        sheetSizeCol.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));
        permissionCol.setCellValueFactory(new PropertyValueFactory<>("permission"));

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        perTypeCol.setCellValueFactory(new PropertyValueFactory<>("permissionType"));
        perStatusCol.setCellValueFactory(new PropertyValueFactory<>("permissionStatus"));

         if(commandsComponent != null){
            commandsComponentController.setDashController(this);
        }
         availableSheets.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
             if (newSelection != null) {
                 updatePermissionsList(newSelection.getPermissionInfo());
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
    public void startListRefresher() {
        TimerTask listRefresher = new SheetsListRefresher(
                autoUpdate,
                this::updateSheetsList,
                availableSheets.getItems(),
                appController.getUsername());
        Timer timer = new Timer();
        timer.schedule(listRefresher, 0, REFRESH_RATE);
    }

    public void sheetChosen() {
        String selectedSheetName = getSelectedSheetName();
        appController.switchToSheetView(selectedSheetName);
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
}
