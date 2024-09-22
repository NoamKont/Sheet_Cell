package MainMenu.Header.DynamicAnalysis;

import MainMenu.AppController;
import MainMenu.Header.HeaderComponentController;
import MainMenu.SideBar.Command.FilterPopUpController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DynamicAnalysisController implements Initializable {

    @FXML
    private Button addCellBtn;

    @FXML
    private Label cellLabel;

    @FXML
    private GridPane cellsValue;

    @FXML
    private Label originalCellValLabel;

    @FXML
    private BorderPane popupBody;

    @FXML
    private Slider valueSlider;

    @FXML
    private Label valueSliderLabel;

    private HeaderComponentController headerComponentController;

    private AppController mainController;

    private Stage popupStage;


    @FXML
    void addCellPressed(ActionEvent event) {
        //Label cellId = new Label("Cell ID");
        //Slider valueSlider = new Slider();
        Label value = new Label("Value");
        Label originalValue = new Label("Original Value");
        Button deleteBtn = new Button("Delete");

        int currentRowCount = cellsValue.getRowCount();

        // Create new Slider and configure it like the existing one
        Slider valueSlider = new Slider();
        valueSlider.setBlockIncrement(1.0);
        valueSlider.setMajorTickUnit(10.0);
        valueSlider.setMinorTickCount(0);
        valueSlider.setShowTickLabels(true);
        valueSlider.setShowTickMarks(true);
        valueSlider.setValue(50.0);
        GridPane.setRowIndex(valueSlider, currentRowCount);
        GridPane.setColumnIndex(valueSlider, 1);

        // Create new cellLabel
        Label cellId = new Label("Label");
        cellId.setPrefHeight(83.0);
        cellId.setPrefWidth(299.0);
        GridPane.setRowIndex(cellId, currentRowCount);
        GridPane.setColumnIndex(cellId, 0);

        // Create new originalCellValLabel

        originalValue.setPrefHeight(103.0);
        originalValue.setPrefWidth(288.0);
        GridPane.setRowIndex(originalValue, currentRowCount);
        GridPane.setColumnIndex(originalValue, 3);

        // Create new valueSliderLabel

        value.setPrefHeight(236.0);
        value.setPrefWidth(373.0);
        value.setFont(valueSliderLabel.getFont());
        GridPane.setRowIndex(value, currentRowCount);
        GridPane.setColumnIndex(value, 2);
        GridPane.setHalignment(value, HPos.CENTER);
        GridPane.setValignment(value, VPos.CENTER);

        // Create new Delete Button
        GridPane.setRowIndex(deleteBtn, currentRowCount);
        GridPane.setColumnIndex(deleteBtn, 4);
        GridPane.setHalignment(deleteBtn, HPos.CENTER);
        GridPane.setValignment(deleteBtn, VPos.CENTER);

        // Add all new elements to the GridPane
        cellsValue.getChildren().addAll(
                valueSlider, cellId, originalValue,
                value, deleteBtn
        );


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        valueSliderLabel.setText(String.valueOf(valueSlider.getValue()));
        valueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            valueSliderLabel.setText(String.valueOf(newValue.intValue()));

        });
        valueSliderLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            int row = GridPane.getRowIndex(valueSliderLabel);
            String cellId = ((Label) FilterPopUpController.getChildFromGridPane(cellsValue, row, 0)).getText();
            String value = ((Label) FilterPopUpController.getChildFromGridPane(cellsValue, row, 2)).getText();
            popupBody.setCenter(mainController.creatSheetComponent(mainController.getSheetForDynamicAnalysis(cellId,value), false));
            valueSlider.setValue(Double.parseDouble(newValue));
        });
    }

    public void setHeaderComponentController(HeaderComponentController headerComponentController) {
        this.headerComponentController = headerComponentController;
    }

    public void setMainController(AppController mainController){
        this.mainController = mainController;
        setSelectedCell();
        popupBody.setCenter(mainController.creatSheetComponent(mainController.getSheetForDynamicAnalysis(), false));
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    public void setSelectedCell() {
        cellLabel.setText(mainController.getSelectedCell().idProperty().get());
        originalCellValLabel.setText(mainController.getSelectedCell().originalValueProperty().get());
    }

    public void setMin(int min) {
        valueSlider.setMin(min);
    }

    public void setMax(int max){
        valueSlider.setMax(max);
    }

    public void setStepSize(int stepSize) {
        valueSlider.setBlockIncrement(stepSize);
        valueSlider.setMajorTickUnit(1);
        valueSlider.setMinorTickCount(0);
        valueSlider.snapToTicksProperty().set(true);
    }


}
