package client.component.dashboard;

import client.component.main.MainMenu.AppController;
import client.component.main.UIbody.SheetInfo;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

public class DashboardController {

    private AppController appController;

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


    @FXML
    public void initialize() {
        // Set up the columns
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("sheetOwner"));
        sheetNameCol.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        sheetSizeCol.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));
        permissionCol.setCellValueFactory(new PropertyValueFactory<>("permission"));


    }
    private void updateSheetsList(List<SheetInfo> sheetsInfo) {
        Platform.runLater(() -> {
            ObservableList<SheetInfo> items = availableSheets.getItems();
            items.clear();
            items.addAll(sheetsInfo);
        });
    }

    public void setAppMainController(AppController appController) {
        this.appController = appController;
    }

}
