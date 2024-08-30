package MainMenu.Example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MyController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea desccriptionTextArea;

    @FXML
    private Button clickMeButton;

    @FXML
    private CheckBox dareCheckBox;

    @FXML
    void buttonClicked(ActionEvent event) {
        desccriptionTextArea.setText(desccriptionTextArea.getText() + " <START added by button> " + nameTextField.getText() + " <END added by button> ");
    }

    @FXML
    void checkboxChecked(ActionEvent event) {
        desccriptionTextArea.setDisable(dareCheckBox.isSelected());
    }

}
