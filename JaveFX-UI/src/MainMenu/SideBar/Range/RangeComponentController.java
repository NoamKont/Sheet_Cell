package MainMenu.SideBar.Range;

import MainMenu.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RangeComponentController {
    private AppController mainController;

    @FXML
    private TextField BottomRight;

    @FXML
    private TextField TopLeft;

    @FXML
    private Button addRange;

    @FXML
    void addRangeToSheet(ActionEvent event) {
        System.out.println("Add Range");
        mainController.addRangeToSheet("test",TopLeft.getText(), BottomRight.getText());
        TopLeft.clear();
        BottomRight.clear();
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}
