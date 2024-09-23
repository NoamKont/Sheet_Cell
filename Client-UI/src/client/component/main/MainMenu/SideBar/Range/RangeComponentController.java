package client.component.main.MainMenu.SideBar.Range;


import client.component.main.MainMenu.AppController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

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
            mainController.addRangeToSheet(rangeNameBox.getText(),TopLeft.getText(), BottomRight.getText());
            TopLeft.clear();
            BottomRight.clear();
            rangeNameBox.clear();
    }

    @FXML
    void clearOptionChoosen(ActionEvent event) {
        rangeListView.getSelectionModel().clearSelection();
        mainController.setSelectedRange(null);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void deleteRangeAction(ActionEvent event) {
        mainController.deleteRangeFromSheet(rangeListView.getSelectionModel().getSelectedItem());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rangeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                deleteRangeBtn.setDisable(false);
            }
            else{
                deleteRangeBtn.setDisable(true);
            }

        });

    }

    @FXML
    void selectedRangePressed(MouseEvent event) {
        mainController.setSelectedRange(rangeListView.getSelectionModel().getSelectedItem());
    }
    public void addRangeToList(String key) {
        rangeListView.getItems().add(key);
        rangeListView.getSelectionModel().clearSelection();
    }

    public void deleteRangeFromList(String key) {
        rangeListView.getItems().remove(key);
        rangeListView.getSelectionModel().clearSelection();
    }

    public ListView<String> getRangeListView(){
        return rangeListView;
    }
}
