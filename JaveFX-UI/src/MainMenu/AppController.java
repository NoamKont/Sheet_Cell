package MainMenu;

import MainMenu.Header.HeaderComponentController;
import MainMenu.SideBar.Command.CommandComponentController;
import MainMenu.SideBar.Range.RangeComponentController;
import UIbody.UICell;
import UIbody.UIGridPart;
import UIbody.UISheet;
import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import jakarta.xml.bind.JAXBException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AppController {

    private GridPane mainSheet;
    private final Logic logic = new ImplLogic();
    private UISheet uiSheet = new UISheet();
    private final UICell selectedCell = new UICell();
    private ObjectProperty<UICell> selectedCellProperty = new SimpleObjectProperty<>();
    private ObjectProperty<UIGridPart> selectedRowOrColumn = new SimpleObjectProperty<>();
    private StringProperty selectedRange = new SimpleStringProperty();



    @FXML private ScrollPane headerComponent;
    @FXML private HeaderComponentController headerComponentController;

    @FXML private GridPane rangeComponent;
    @FXML private RangeComponentController rangeComponentController;

    @FXML private VBox commandComponent;
    @FXML private CommandComponentController commandComponentController;



    @FXML private BorderPane bodyComponent;

    @FXML
    public void initialize() {
        if(headerComponentController != null && rangeComponentController != null && commandComponentController != null){
            headerComponentController.setMainController(this);
            rangeComponentController.setMainController(this);
            commandComponentController.setMainController(this);
        }

        //bind the selected cell to the UI Header component
        bindModuleToUI();

        selectedCell.cellsDependsOnThemProperty().addListener((observableValue, oldList, newList) -> {
            if(newList != null){
                newList.stream().forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().add("depends-on-cell");
                });
            }
            if(oldList != null){
                oldList.stream().forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().remove("depends-on-cell");
                });
            }
        });

        selectedCell.cellsDependsOnHimProperty().addListener((observableValue, oldList, newList) -> {
            if(newList != null){
                newList.stream().forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().add("influence-on-cell");
                });
            }
            if(oldList != null){
                oldList.stream().forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().remove("influence-on-cell");
                });
            }
        });

        //listener for the selected cell dependency list  and style of selected cell CSS
        selectedCellProperty.addListener((observableValue, oldCell, newCell) -> {
            if (newCell != null) {
                newCell.getCellLabel().setId("selected-cell");
                commandComponentController.getTextColorPicker().setValue((Color) newCell.getCellLabel().getTextFill());
                Background background = newCell.getCellLabel().getBackground();
                if (background != null && !background.getFills().isEmpty()) {
                    BackgroundFill fill = background.getFills().get(0);
                    commandComponentController.getBackgroundColorPicker().setValue((Color) fill.getFill());
                }else{
                    commandComponentController.getBackgroundColorPicker().setValue(Color.WHITE);
                }

            }
            if(oldCell != null){
                oldCell.getCellLabel().setId(null);
            }
        });

        //listener for the selected range and style of selected range CSS
        selectedRange.addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                uiSheet.getCoordinatesOfRange(newValue).forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().add("selected-range");
                });
                System.out.println("Selected Range: " + newValue);
            }
            if(oldValue != null){
                uiSheet.getCoordinatesOfRange(oldValue).forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().remove("selected-range");
                });
                System.out.println("Unselected Range: " + oldValue);
            }
        });

        //listener for the selected row or column and update the command component
        selectedRowOrColumn.addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                String title;
                if(newValue.getIsRow()){
                    title = "Row: " + newValue.getName();
                }
                else{
                    title = "Column: " + newValue.getName();
                }
                commandComponentController.getChosenColumnRow().setText(title);
                commandComponentController.getWidthSpinner().getValueFactory().setValue(newValue.getWidth());
                commandComponentController.getThicknessSpinner().getValueFactory().setValue(newValue.getThickness());
                commandComponentController.getWidthSpinner().setDisable(newValue.getIsRow());
                commandComponentController.getThicknessSpinner().setDisable(!newValue.getIsRow());
            }
        });

        //listener for the width and thickness of the selected row or column and change the width or thickness
        commandComponentController.getWidthSpinner().valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (selectedRowOrColumn.get() != null) {
                changeWidth(newValue);
            }
        });
        commandComponentController.getThicknessSpinner().valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (selectedRowOrColumn.get() != null) {
                changeThickness(newValue);
            }
        });

    }

    private void rangeMapListener() {
        rangeComponentController.getRangeListView().getItems().clear();
        uiSheet.rangeMapProperty().addListener((MapChangeListener.Change<? extends String, ? extends Set<Coordinate>> change) -> {
            if (change.wasAdded()) {
                rangeComponentController.addRangeToList(change.getKey());
                System.out.println("Added: " + change.getKey() + " -> " + change.getValueAdded());
            }
            if (change.wasRemoved()) {
                rangeComponentController.deleteRangeFromList(change.getKey());
                System.out.println("Removed: " + change.getKey() + " -> " + change.getValueRemoved());
            }
        });
    }

    public void createSheet(String filePath) {
        // Create a Sheet
        try{
            logic.creatNewSheet(filePath);
            selectedCell.clearCell();
            uiSheet = new UISheet(logic.getSheet()); //set the module
            rangeMapListener();
            uiSheet.updateSheet(logic.getSheet());
            createViewSheet();
            headerComponentController.newSheetHeader();
            headerComponentController.addVersionToMenu(uiSheet.sheetVersionProperty().getValue());
            System.out.println("Sheet Created");
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in Upload new Sheet");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }

    }

    public void updateCell(String input){
        try{
            logic.updateCell(selectedCell.idProperty().getValue(), input);
            uiSheet.updateSheet(logic.getSheet());
            headerComponentController.addVersionToMenu(uiSheet.sheetVersionProperty().getValue());
            selectedCellProperty.set(uiSheet.getCell(new CoordinateImpl(selectedCell.idProperty().getValue())));
            selectedCell.updateUICell(selectedCellProperty.get());

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in updating cell");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private void bindModuleToUI() {
        // Bind the UI to the module
        headerComponentController.bindModuleToUI(selectedCell);
        //rangeComponentController.bindModuleToUI(uiSheet);
    }

    public ScrollPane creatSheetComponent(UISheet Sheet) {
        GridPane dynamicGrid = new GridPane();
        //add '1' for the header

        int numRows = logic.getRowsNumber() + 1;
        int numCols = logic.getColumnsNumber()+ 1;

        // Add RowConstraints and ColumnConstraints
        for (int i = 0; i < numRows; i++) {
            RowConstraints row = new RowConstraints();

            row.setPrefHeight(logic.getSheet().getThickness());
            row.setMinHeight(Region.USE_PREF_SIZE);
            row.setMaxHeight(Region.USE_PREF_SIZE);
            row.setVgrow(javafx.scene.layout.Priority.ALWAYS);
            dynamicGrid.getRowConstraints().add(row);
        }

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(logic.getSheet().getWidth());
            col.setMinWidth(Region.USE_PREF_SIZE);
            col.setMaxWidth(Region.USE_PREF_SIZE);
            col.setHgrow(javafx.scene.layout.Priority.ALWAYS);
            dynamicGrid.getColumnConstraints().add(col);
        }

        // Populate the GridPane with Labels and Headers
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                AnchorPane anchorPane = new AnchorPane();
                if (row == 0 && col > 0) {
                    // Top row (column headers)
                    Label label = new Label(Character.toString((char) ('A' + col - 1))); // "A", "B", "C", ...
                    dynamicGrid.getColumnConstraints().get(col).prefWidthProperty().bind(Sheet.getColumn(label.getText()).widthProperty());
                    // Set click event handler
                    label.setOnMouseClicked(event -> {
                        System.out.println("Label " + label.getText() +" clicked: " + label.getText());
                        selectedRowOrColumn.set(Sheet.getColumn(label.getText()));
                    });
                    setHeaderLabel(anchorPane, label);
                } else if (col == 0 && row > 0) {
                    // First column (row headers)
                    Label label = new Label(Integer.toString(row)); // "1", "2", "3", ..
                    dynamicGrid.getRowConstraints().get(row).prefHeightProperty().bind(Sheet.getRow(label.getText()).thicknessProperty());
                    // Set click event handler
                    label.setOnMouseClicked(event -> {
                        System.out.println("Label " + label.getText() +" clicked: " + label.getText());
                        selectedRowOrColumn.set(Sheet.getRow(label.getText()));
                    });
                    setHeaderLabel(anchorPane, label);
                } else if (row > 0 && col > 0) {
                    String cellID = fromDotToCellID(row, col);
                    Label label = Sheet.getCell(new CoordinateImpl(cellID)).getCellLabel();

                    label.getStyleClass().add("single-cell");
                    Coordinate coordinate = new CoordinateImpl(cellID);
                    label.textProperty().bind(Sheet.getCell(coordinate).effectiveValueProperty());
                    Sheet.setCellLabel(coordinate, label);
                    AnchorPane.setTopAnchor(label, 0.0);
                    AnchorPane.setBottomAnchor(label, 0.0);
                    AnchorPane.setLeftAnchor(label, 0.0);
                    AnchorPane.setRightAnchor(label, 0.0);
                    anchorPane.getChildren().add(label);

                    // Set click event handler
                    label.setOnMouseClicked(event -> {
                        System.out.println("Label clicked: " + label.getText());
                        selectedCellProperty.set(Sheet.getCell(new CoordinateImpl(cellID)));
                        selectedCell.updateUICell(selectedCellProperty.get());
                    });
                }
                dynamicGrid.add(anchorPane, col, row);
            }
        }

        dynamicGrid.setAlignment(javafx.geometry.Pos.CENTER);
        dynamicGrid.setGridLinesVisible(true);

        dynamicGrid.setMinHeight(Region.USE_PREF_SIZE);
        dynamicGrid.setMinWidth(Region.USE_PREF_SIZE);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.fitToHeightProperty().set(true);
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.setContent(dynamicGrid);
        return scrollPane;
    }

    private void createViewSheet() {
            ScrollPane scrollPane = creatSheetComponent(uiSheet);
            bodyComponent.setCenter(scrollPane);
            bodyComponent.setMinWidth(0);
            bodyComponent.setMinHeight(0);
    }

    private void setHeaderLabel(AnchorPane anchorPane, Label label) {
        label.getStyleClass().add("headers-cell");
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setBottomAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        anchorPane.getChildren().add(label);
    }

    private String fromDotToCellID(int row, int col){
        return String.valueOf((char)('A' + col - 1)) + (row);
    }

    public UISheet getUiSheet() {
        return uiSheet;
    }

    public void addRangeToSheet(String rangeName, String topLeft, String bottomRight) {
        try{
            Set<Coordinate> coordinates = logic.addRangeToSheet(rangeName,topLeft,bottomRight);
            uiSheet.addRange(rangeName, coordinates);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in adding range");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void setSelectedRange(String newValue) {
        selectedRange.set(newValue);
    }

    public void alignCells(Pos pos) {
        selectedRowOrColumn.get().alignCells(pos);
    }

    public void changeWidth(Integer newValue) {
        selectedRowOrColumn.get().setWidth(newValue);
    }

    public void changeThickness(Integer newValue) {
        selectedRowOrColumn.get().setThickness(newValue);
    }

    public void deleteRangeFromSheet(String selectedItem) {
        selectedRange.set(null);
        try {
            logic.deleteRange(selectedItem);
            uiSheet.deleteRange(selectedItem);

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in deleting range");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            //System.out.println("Error: " + e.getMessage());
        }
    }

    public UISheet sortSheet(String topLeft, String bottomRight,String... columns) {
            UISheet sortedSheet = new UISheet(logic.sortSheet(topLeft,bottomRight,columns));
            return sortedSheet;
    }

    public int getColumnsNumber() {
        return logic.getColumnsNumber();
    }

    public void showVersion(int i) {
        Stage popupStage = new Stage();
        try{
            UISheet versionSheet = new UISheet(logic.getSheetbyVersion(i-1));
            ScrollPane popupLayout = creatSheetComponent(versionSheet);
            Scene popupSortedSheet = new Scene(popupLayout, 600, 400);
            popupStage.setScene(popupSortedSheet);
            popupStage.showAndWait();
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in showing version");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    //TODO: change it only to the range
    public List<String> getValuesFromColumns(Integer columnIndex) {
        try{
            return logic.getSheet().getValuesFromColumn(columnIndex);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in getting values from column");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    public UISheet filterSheet(String topLeft, String bottomRight, List<List<String>> values, List<String> columns) {
            UISheet filterSheet = new UISheet(logic.filterSheet(topLeft,bottomRight,values,columns));
            return filterSheet;
    }

    public void changeTextColorForSelectedCell(Color textColor) {
        selectedCell.getCellLabel().setTextFill(textColor);
    }

    public void changeBackgroundColorForSelectedCell(Color backgroundColor) {
        selectedCell.getCellLabel().setBackground(new Background(new BackgroundFill(
                backgroundColor,
                CornerRadii.EMPTY,
                javafx.geometry.Insets.EMPTY)));
    }
}
