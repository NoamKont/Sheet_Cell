package MainMenu.SideBar.Command;


import MainMenu.AppController;
import MainMenu.VisualUtils.GraphMakerController;
import UIbody.UICell;
import UIbody.UISheet;
import body.Coordinate;
import body.impl.CoordinateImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;


public class CommandComponentController implements Initializable {

    @FXML
    private Text chosenColumnRow;

    @FXML
    private Spinner<Integer> thicknessSpinner;

    @FXML
    private Spinner<Integer> widthSpinner;

    @FXML
    private ComboBox<String> alignmentBox;

    @FXML
    private ColorPicker textColorPicker;

    @FXML
    private ColorPicker backgroundColorPicker;

    private AppController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // alignment box
        ObservableList<String> options =
                FXCollections.observableArrayList( "Left", "Center", "Right" );
        alignmentBox.setItems(options);

        //width spinners
        SpinnerValueFactory<Integer> widthValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200);
        widthSpinner.setValueFactory(widthValueFactory);

        //thickness spinners
        SpinnerValueFactory<Integer> thicknessValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200);
        thicknessSpinner.setValueFactory(thicknessValueFactory);

    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void undoColorChangeBtnPressed(ActionEvent event) {
        mainController.resetColorForSelectedCell();
    }


    @FXML
    void graphPressed(ActionEvent event) throws IOException {
        Stage popupStage = new Stage();

        // Set the pop-up window to be modal (blocks interaction with other windows)
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Graph");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/MainMenu/VisualUtils/graph.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        GraphMakerController controller = fxmlLoader.getController();
        Scene popupScene = new Scene(root, 600, 400);
        controller.setMainController(mainController);
        controller.setStage(popupStage);

        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    @FXML
    void textColorPickerChoose(ActionEvent event) {
        Color textColor = textColorPicker.getValue();
        mainController.changeTextColorForSelectedCell(textColor);
    }

    @FXML
    void backgroundColorChoose(ActionEvent event) {
        Color backgroundColor = backgroundColorPicker.getValue();
        mainController.changeBackgroundColorForSelectedCell(backgroundColor);
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

    @FXML
    void sortSheetBtnClicked(ActionEvent event) throws IOException {
        Stage popupStage = new Stage();

        // Set the pop-up window to be modal (blocks interaction with other windows)
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Sorted Sheet");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("popupViewForSort.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        sortPopUPController controller = fxmlLoader.getController();

        Scene popupScene = new Scene(root, 600, 400);

        controller.setCommandComponentController(this);
        controller.setPopupStage(popupStage);
        controller.setColumnsNumber(mainController.getColumnsNumber());

        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    @FXML
    void filterSheetBtnClicked(ActionEvent event) throws IOException {
        Stage popupStage = new Stage();

        // Set the pop-up window to be modal (blocks interaction with other windows)
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Filtered Sheet");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("popupViewForFilter.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        FilterPopUpController controller = fxmlLoader.getController();

        Scene popupScene = new Scene(root, 600, 400);

        controller.setCommandComponentController(this);
        controller.setPopupStage(popupStage);
        controller.setColumnsNumberInSheet(mainController.getColumnsNumber());

        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    public void sortSheet(String topLeft, String bottomRight, String[] columns,Stage popupStage) {
            UISheet sortedSheet = mainController.sortSheet(topLeft, bottomRight, columns);
            updateStyleSheet(sortedSheet);
            ScrollPane popupLayout = mainController.creatSheetComponent(sortedSheet);
            Scene popupSortedSheet = new Scene(popupLayout, 900, 500);
            popupStage.setScene(popupSortedSheet);

    }

    private void updateStyleSheet(UISheet newSheet) {
        UISheet currentSheet = mainController.getUiSheet();
        Map<Coordinate, UICell> activeCellsOfNewSheet = newSheet.getActiveCells();
        activeCellsOfNewSheet.forEach((coordinate, cell) -> {
            Label oldLabel = currentSheet.getCell(new CoordinateImpl(cell.idProperty().get())).getCellLabel();
            Label newLabel = cell.getCellLabel();
            if(oldLabel != null){
                copyCellStyle(oldLabel, newLabel);
            }
        });
    }

    public void copyCellStyle(Label labelSrc, Label labelDest) {
        labelDest.setTextFill(labelSrc.getTextFill());
        labelDest.setBackground(labelSrc.getBackground());
        labelDest.setAlignment(labelSrc.getAlignment());
    }

    public Text getChosenColumnRow() {
        return chosenColumnRow;
    }
    public Spinner<Integer> getThicknessSpinner() {
        return thicknessSpinner;
    }
    public Spinner<Integer> getWidthSpinner() {
        return widthSpinner;
    }
    public Set<String> getValuesFromColumn(Integer columnIndex, int top, int bottom) {
        return mainController.getValuesFromColumnsAsSet(columnIndex , top, bottom);
    }

    public void filterSheet(String topLeft, String bottomRight, List<List<String>> values, List<String> columns, Stage popupStage) {
        UISheet filterSheet = mainController.filterSheet(topLeft, bottomRight, values, columns);
        updateStyleSheet(filterSheet);
        ScrollPane popupLayout = mainController.creatSheetComponent(filterSheet);
        Scene popupSortedSheet = new Scene(popupLayout, 900, 500);
        popupStage.setScene(popupSortedSheet);
    }

    public ColorPicker getBackgroundColorPicker() {
        return backgroundColorPicker;
    }
    public ColorPicker getTextColorPicker() {
        return textColorPicker;
    }

}

