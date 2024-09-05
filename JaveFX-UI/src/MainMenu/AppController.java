package MainMenu;

import MainMenu.Header.HeaderComponentController;
import UIbody.UICell;
import UIbody.UISheet;
import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import jakarta.xml.bind.JAXBException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import java.io.IOException;

import static javafx.scene.paint.Color.*;


public class AppController {

    private GridPane mainSheet;
    private final Logic logic = new ImplLogic();
    private UISheet uiSheet = new UISheet();
    private final UICell selectedCell = new UICell();

    @FXML private ScrollPane headerComponent;
    @FXML private HeaderComponentController headerComponentController;

    @FXML private BorderPane bodyComponent;

    @FXML
    public void initialize() {
        if(headerComponentController != null){
            headerComponentController.setMainController(this);
        }
        bindModuleToUI();
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
                        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        label.setAlignment(javafx.geometry.Pos.CENTER);
                        AnchorPane.setTopAnchor(label, 0.0);
                        AnchorPane.setBottomAnchor(label, 0.0);
                        AnchorPane.setLeftAnchor(label, 0.0);
                        AnchorPane.setRightAnchor(label, 0.0);
                        anchorPane.getChildren().add(label);
                    } else if (col == 0 && row > 0) {
                        // First column (row headers)
                        Label label = new Label(Integer.toString(row)); // "1", "2", "3", ...
                        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        label.setAlignment(javafx.geometry.Pos.CENTER);
                        AnchorPane.setTopAnchor(label, 0.0);
                        AnchorPane.setBottomAnchor(label, 0.0);
                        AnchorPane.setLeftAnchor(label, 0.0);
                        AnchorPane.setRightAnchor(label, 0.0);
                        anchorPane.getChildren().add(label);
                    } else if (row > 0 && col > 0) {
                        String cellID = fromDotToCellID(row, col);
                        Label label = new Label();
                        label.setId(cellID);
                        Coordinate coordinate = new CoordinateImpl(cellID);
                        label.textProperty().bind(uiSheet.getCell(coordinate).effectiveValueProperty());
                        uiSheet.setCellLabel(coordinate, label);
                        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        label.setAlignment(javafx.geometry.Pos.CENTER);
                        label.setFocusTraversable(true);
                        AnchorPane.setTopAnchor(label, 0.0);
                        AnchorPane.setBottomAnchor(label, 0.0);
                        AnchorPane.setLeftAnchor(label, 0.0);
                        AnchorPane.setRightAnchor(label, 0.0);
                        anchorPane.getChildren().add(label);

                        // Set click event handler
                        label.setOnMouseClicked(event -> {
                            System.out.println("Label " + label.getId() +" clicked: " + label.getText());
                            selectedCell.updateUICell(uiSheet.getCell(new CoordinateImpl(cellID)));
                            label.setStyle(
                                    "-fx-border-color: #91b2f0; " +     // Border color
                                    "-fx-border-width: 3px; " +         // Border width
                                    "-fx-border-style: solid; " +       // Border style (solid, dashed, dotted, etc.)
                                    "-fx-border-radius: 2px;"           // Rounded corners (optional)
                            );
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


    private String fromDotToCellID(int row, int col){
        return String.valueOf((char)('A' + col - 1)) + (row);
    }

    public UISheet getUiSheet() {
        return uiSheet;
    }
}
