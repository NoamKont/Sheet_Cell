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
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import java.io.IOException;



public class AppController {

    private GridPane mainSheet;
    private final Logic logic = new ImplLogic();
    private UISheet uiSheet = new UISheet();
    private final UICell selectedCell = new UICell();
    private String selectedRange;
    private ObjectProperty<UIGridPart> selectedRowOrColumn = new SimpleObjectProperty<>();



    @FXML private ScrollPane headerComponent;
    @FXML private HeaderComponentController headerComponentController;

    @FXML private AnchorPane rangeComponent;
    @FXML private RangeComponentController rangeComponentController;

    @FXML private AnchorPane commandComponent;
    @FXML private CommandComponentController commandComponentController;



    @FXML private BorderPane bodyComponent;

    @FXML
    public void initialize() {
        if(headerComponentController != null && rangeComponentController != null && commandComponentController != null){
            headerComponentController.setMainController(this);
            rangeComponentController.setMainController(this);
            commandComponentController.setMainController(this);
        }
        bindModuleToUI();
        selectedCell.cellLabelProperty().addListener((observableValue, oldLabelSelection, newSelectedLabel) -> {
            if (oldLabelSelection != null) {
                oldLabelSelection.setId(null);
            }
            if (newSelectedLabel != null) {
                newSelectedLabel.setId("selected-cell");
            }
        });



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

    public void createSheet(String filePath) {
        // Create a Sheet
        try{
            logic.creatNewSheet(filePath);
            uiSheet = new UISheet(logic.getSheet()); //set the module
            createViewSheet();
            headerComponentController.newSheetHeader();
            selectedCell.clearCell();
            headerComponentController.addVersionToMenu(uiSheet.sheetVersionProperty().getValue());
            System.out.println("Sheet Created");
        }catch (JAXBException | IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public void updateCell(String input){
        logic.updateCell(selectedCell.idProperty().getValue(), input);
        uiSheet.updateSheet(logic.getSheet());
        headerComponentController.addVersionToMenu(uiSheet.sheetVersionProperty().getValue());
        selectedCell.updateUICell(uiSheet.getCell(new CoordinateImpl(selectedCell.idProperty().getValue())));
    }

    private void bindModuleToUI() {
        // Bind the UI to the module
        headerComponentController.bindModuleToUI(selectedCell);
        rangeComponentController.bindModuleToUI(uiSheet);
    }

    private void createViewSheet() {
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
                        dynamicGrid.getColumnConstraints().get(col).prefWidthProperty().bind(uiSheet.getColumn(label.getText()).widthProperty());
                        // Set click event handler
                        label.setOnMouseClicked(event -> {
                            System.out.println("Label " + label.getText() +" clicked: " + label.getText());
                            selectedRowOrColumn.set(uiSheet.getColumn(label.getText()));
                        });
                        setHeaderLable(anchorPane, label);
                    } else if (col == 0 && row > 0) {
                        // First column (row headers)
                        Label label = new Label(Integer.toString(row)); // "1", "2", "3", ..
                        dynamicGrid.getRowConstraints().get(row).prefHeightProperty().bind(uiSheet.getRow(label.getText()).thicknessProperty());
                        // Set click event handler
                        label.setOnMouseClicked(event -> {
                            System.out.println("Label " + label.getText() +" clicked: " + label.getText());
                            selectedRowOrColumn.set(uiSheet.getRow(label.getText()));
                        });
                        setHeaderLable(anchorPane, label);
                    } else if (row > 0 && col > 0) {
                        String cellID = fromDotToCellID(row, col);
                        Label label = new Label();
                        label.getStyleClass().add("single-cell");
                        Coordinate coordinate = new CoordinateImpl(cellID);
                        label.textProperty().bind(uiSheet.getCell(coordinate).effectiveValueProperty());
                        uiSheet.setCellLabel(coordinate, label);
                        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        label.setAlignment(javafx.geometry.Pos.CENTER);
                        AnchorPane.setTopAnchor(label, 0.0);
                        AnchorPane.setBottomAnchor(label, 0.0);
                        AnchorPane.setLeftAnchor(label, 0.0);
                        AnchorPane.setRightAnchor(label, 0.0);
                        anchorPane.getChildren().add(label);

                        // Set click event handler
                        label.setOnMouseClicked(event -> {
                            System.out.println("Label " + label.getId() +" clicked: " + label.getText());
                            selectedCell.updateUICell(uiSheet.getCell(new CoordinateImpl(cellID)));
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
            bodyComponent.setCenter(scrollPane);

            bodyComponent.setMinWidth(0);
            bodyComponent.setMinHeight(0);
    }

    private void setHeaderLable(AnchorPane anchorPane, Label label) {
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
        logic.addRangeToSheet(rangeName,topLeft,bottomRight);
        uiSheet.updateSheet(logic.getSheet());
    }

    public void setSelectedRange(String newValue) {
        selectedRange = newValue;
        System.out.println("Selected Range: " + selectedRange);
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
}
