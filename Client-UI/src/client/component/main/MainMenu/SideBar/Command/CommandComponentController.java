package client.component.main.MainMenu.SideBar.Command;




import body.Coordinate;
import body.impl.CoordinateImpl;
import client.component.main.MainMenu.AppController;
import client.component.main.MainMenu.VisualUtils.GraphMakerController;
import client.component.main.UIbody.UICell;
import client.component.main.UIbody.UISheet;
import client.util.http.HttpClientUtil;
import com.google.gson.JsonObject;
import dto.SheetDTO;
import javafx.application.Platform;
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
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import static client.util.Constants.*;


public class CommandComponentController implements Initializable {

    @FXML
    private Label chosenColumnRow;

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
                mainController.alignCells(Pos.CENTER_LEFT);
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
        URL url = getClass().getResource("clientPopupViewForSort.fxml");
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

    public void sortSheet(String topLeft, String bottomRight, String[] columns,Stage popupStage) {
        RequestBody body = new FormBody.Builder()
                .add("sheetName", mainController.getSheetName())
                .add("topLeft", topLeft)
                .add("bottomRight", bottomRight)
                .add("columns", GSON_INSTANCE.toJson(columns))
                .build();

        HttpClientUtil.runPostAsync(body,SORT_SHEET, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if(response.isSuccessful()) {
                    System.out.println("Sort Sheet is successful now bringing the Sheet");
                    SheetDTO sortedSheetDTO = GSON_INSTANCE.fromJson(responseBody, SheetDTO.class);
                    UISheet sortedSheet = new UISheet(sortedSheetDTO);
                    updateStyleSheet(sortedSheet);
                    ScrollPane popupLayout = mainController.creatSheetComponent(sortedSheet, false);
                    Scene popupSortedSheet = new Scene(popupLayout, 900, 500);
                    Platform.runLater(() -> {
                        popupStage.setScene(popupSortedSheet);
                    });
                }
                else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error in sorting sheet");
                        alert.setContentText(responseBody);
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error in sorting sheet");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        });

    }

    @FXML
    void filterSheetBtnClicked(ActionEvent event) throws IOException {
        Stage popupStage = new Stage();

        // Set the pop-up window to be modal (blocks interaction with other windows)
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Filtered Sheet");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("clientPopupViewForFilter.fxml");
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

    public void filterSheet(String topLeft, String bottomRight, List<List<String>> values, List<String> columns, Stage popupStage) {
        RequestBody body = new FormBody.Builder()
                .add("sheetName", mainController.getSheetName())
                .add("topLeft", topLeft)
                .add("bottomRight", bottomRight)
                .add("values", GSON_INSTANCE.toJson(values))
                .add("columns", GSON_INSTANCE.toJson(columns))
                .build();

        HttpClientUtil.runPostAsync(body,FILTER_SHEET, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if(response.isSuccessful()) {
                    System.out.println("Filter Sheet is successful now bringing the Sheet");
                    SheetDTO filterSheetDTO = GSON_INSTANCE.fromJson(responseBody, SheetDTO.class);
                    UISheet filteredSheet = new UISheet(filterSheetDTO);
                    updateStyleSheet(filteredSheet);
                    ScrollPane popupLayout = mainController.creatSheetComponent(filteredSheet, false);
                    Scene popupSortedSheet = new Scene(popupLayout, 900, 500);
                    Platform.runLater(() -> {
                        popupStage.setScene(popupSortedSheet);
                    });
                }
                else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error in filtering sheet");
                        alert.setContentText(responseBody);
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error in filtering sheet");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        });
//
//
//        UISheet filterSheet = mainController.filterSheet(topLeft, bottomRight, values, columns);
//        updateStyleSheet(filterSheet);
//        ScrollPane popupLayout = mainController.creatSheetComponent(filterSheet, false);
//        Scene popupSortedSheet = new Scene(popupLayout, 900, 500);
//        popupStage.setScene(popupSortedSheet);
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
    public Label getChosenColumnRow() {
        return chosenColumnRow;
    }
    public Spinner<Integer> getThicknessSpinner() {
        return thicknessSpinner;
    }
    public Spinner<Integer> getWidthSpinner() {
        return widthSpinner;
    }

//    public Set<String> getValuesFromColumn(Integer columnIndex, int top, int bottom) {
//        return mainController.getValuesFromColumnsAsSet(columnIndex , top, bottom);
//    }

    public ColorPicker getBackgroundColorPicker() {
        return backgroundColorPicker;
    }
    public ColorPicker getTextColorPicker() {
        return textColorPicker;
    }
    public String getSheetName() {
        return mainController.getSheetName();
    }

}

