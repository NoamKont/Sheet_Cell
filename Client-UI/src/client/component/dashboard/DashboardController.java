package client.component.dashboard;

import client.component.main.MainMenu.AppController;
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
    private TableView<SheetInfo> availableSheets;

    @FXML
    private TableView<?> permissionTable;

    @FXML
    private BorderPane bodyComponent;

    @FXML
    private TableColumn<SheetInfo, String> ownerCol;

    @FXML
    private TableColumn<SheetInfo, String> permissionCol;

    @FXML
    private TableColumn<SheetInfo, String> sheetNameCol;

    @FXML
    private TableColumn<SheetInfo, String> sheetSizeCol;

    private final BooleanProperty autoUpdate = new SimpleBooleanProperty(true);
    @FXML
    public void initialize() {
        // Set up the columns
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("sheetOwner"));
        sheetNameCol.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        sheetSizeCol.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));
        permissionCol.setCellValueFactory(new PropertyValueFactory<>("permission"));


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
                this::updateSheetsList);
        Timer timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setAppMainController(AppController appController) {
        this.appController = appController;
    }

}
