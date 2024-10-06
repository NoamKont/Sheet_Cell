package client.component.dashboard.sideBar.addRequest;

import client.component.dashboard.sideBar.DashCommandsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class addRequestController implements Initializable {

    @FXML
    private CheckBox readerBox;

    @FXML
    private Button sendRequestBtn;

    @FXML
    private ComboBox<String> sheetList;

    @FXML
    private CheckBox writerBox;

    private DashCommandsController dashCommandsController;

    private Stage popupStage;

    @FXML
    void sendRequestBtnPressed(ActionEvent event) {
        String sheetName = sheetList.getValue();
        String permissionType = writerBox.isSelected() ? "WRITER" : "READER";
        String status = "PENDING";
        dashCommandsController.sendPermissionRequest(sheetName, permissionType, status);
        popupStage.close();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        writerBox.setOnAction(e -> {
            if (writerBox.isSelected()) {
                readerBox.setSelected(false);
            }
        });
        readerBox.setOnAction(e -> {
            if (readerBox.isSelected()) {
                writerBox.setSelected(false);
            }
        });
        sendRequestBtn.disableProperty().bind(sheetList.valueProperty().isNull().or(readerBox.selectedProperty().not().and(writerBox.selectedProperty().not())));
    }
    public void setDashCommandsController(DashCommandsController dashCommandsController) {
        this.dashCommandsController = dashCommandsController;
        List<String> sheetNames = dashCommandsController.getSheetNames();
        setSheetList(sheetNames);
    }
    public void setSheetList(List<String> sheetNames) {
        sheetList.getItems().addAll(sheetNames);
    }

    public void setStage(Stage popupStage) {
        this.popupStage = popupStage;
    }
}
