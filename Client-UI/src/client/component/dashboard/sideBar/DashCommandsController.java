package client.component.dashboard.sideBar;

import client.component.dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class DashCommandsController implements Initializable {

    @FXML
    private Button viewSheetBtn;

    private DashboardController dashController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void viewSheetPressed(ActionEvent event) {
        //String sheetName = dashController.getSelectedSheetName();
        dashController.sheetChosen();
    }

    public void setDashController(DashboardController dashController) {
        this.dashController = dashController;
    }
}
