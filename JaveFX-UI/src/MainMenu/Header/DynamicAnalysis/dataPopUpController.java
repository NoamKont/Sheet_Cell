package MainMenu.Header.DynamicAnalysis;

import MainMenu.Header.HeaderComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class dataPopUpController {
    @FXML
    private TextField MinValue;

    @FXML
    private TextField maxValue;

    @FXML
    private TextField stepSize;

    private HeaderComponentController headerComponentController;

    private Stage popupStage;

    public void setHeaderComponentController(HeaderComponentController headerComponentController) {
        this.headerComponentController = headerComponentController;
    }
    @FXML
    void analysisBtnPress(ActionEvent event) throws Exception {
        if(!MinValue.getText().isEmpty() && !maxValue.getText().isEmpty() && !stepSize.getText().isEmpty()){
            int min = Integer.parseInt(MinValue.getText());
            int max = Integer.parseInt(maxValue.getText());
            int step = Integer.parseInt(stepSize.getText());
            if(min < max){
                headerComponentController.DynamicAnalysis(min, max, step, popupStage);
            }
        }

    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }
}
