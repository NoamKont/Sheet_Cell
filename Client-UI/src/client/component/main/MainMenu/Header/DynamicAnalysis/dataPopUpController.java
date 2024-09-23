package client.component.main.MainMenu.Header.DynamicAnalysis;


import client.component.main.MainMenu.Header.HeaderComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error in min and max values");
                alert.setContentText("Please enter a Min value smaller than Max value");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in input values");
            alert.setContentText("Please enter all the values");
            alert.showAndWait();
        }

    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }
}
