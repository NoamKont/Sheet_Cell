package MainMenu.SideBar.Range;

import MainMenu.AppController;
import UIbody.UISheet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class RangeComponentController implements Initializable {

    private AppController mainController;

    @FXML
    private TextField BottomRight;

    @FXML
    private TextField TopLeft;

    @FXML
    private TextField rangeNameBox;

    @FXML
    private Button addRange;

    @FXML
    private Button deleteRangeBtn;

    @FXML
    private Button clearOptionBtn;

    @FXML
    private ListView<String> rangeListView;

    @FXML
    void addRangeToSheet(ActionEvent event) {
        try{
            mainController.addRangeToSheet(rangeNameBox.getText(),TopLeft.getText(), BottomRight.getText());
            System.out.println("Add Range " + rangeNameBox.getText());
            rangeListView.getItems().add(rangeNameBox.getText());

        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        finally {
            TopLeft.clear();
            BottomRight.clear();
            rangeNameBox.clear();
        }
    }

    @FXML
    void clearOptionChoosen(ActionEvent event) {
        rangeListView.getSelectionModel().clearSelection();
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void bindModuleToUI(UISheet uiSheet) {
            uiSheet.rangeCellsProperty().addListener((observable, oldValue, newValue) -> {
                rangeListView.getItems().clear();
                rangeListView.getItems().addAll(newValue);

            });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rangeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                deleteRangeBtn.setDisable(false);
                mainController.setSelectedRange(newValue);
            }
            else{
                deleteRangeBtn.setDisable(true);
            }

        });

    }
}
