package MainMenu.SideBar.Command;


import MainMenu.AppController;
import UIbody.UISheet;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;


public class CommandComponentController implements Initializable {

    @FXML
    private Text chosenColumnRow;

    @FXML
    private Spinner<Integer> thicknessSpinner;

    @FXML
    private Spinner<Integer> widthSpinner;

    @FXML
    private ComboBox<String> alignmentBox;

    private AppController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // alignment box
        ObservableList<String> options =
                FXCollections.observableArrayList( "Left", "Center", "Right" );
        alignmentBox.setItems(options);

//        // column width picker
//        widthSpinner.valueProperty()
//                .addListener((observable, oldValue, newValue) -> mainController.changeWidth(newValue));
//
//        SpinnerValueFactory<Integer> widthValueFactory =
//                new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 200, 100, 1);
//        widthSpinner.setValueFactory(widthValueFactory);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void alignmentSelectionListener(ActionEvent event) {
        int selectedIndex = alignmentBox.getSelectionModel().getSelectedIndex();
        switch (selectedIndex) {
            case 0:
                mainController.alignCells(javafx.geometry.Pos.CENTER_LEFT);
                break;
            case 1:
                mainController.alignCells(Pos.CENTER);
                break;
            case 2:
                mainController.alignCells(Pos.CENTER_RIGHT);
                break;
        }
    }
    public Text getChosenColumnRow() {
        return chosenColumnRow;
    }
}

