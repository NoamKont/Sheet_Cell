package MainMenu.SideBar.Command;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FilterPopUpController {

    @FXML
    private GridPane GridPaneOfColumns;

    @FXML
    private TextField bottomRightText;

    @FXML
    private ComboBox<String> firstColumnPicker;

    @FXML
    private TextField topLeftText;

    @FXML
    private ComboBox<String> valuePicker;

    private Integer numberOfColumns2Filter = 1;

    private CommandComponentController commandComponentController;

    private Stage popupStage;

    private int columnsNumberInSheet;

    @FXML
    void filterBtnClicked(ActionEvent event) {
        String topLeft = topLeftText.getText().toUpperCase();
        String bottomRight = bottomRightText.getText().toUpperCase();
        List<String> columns = getColumns();
        List<String> values = getValues();
        commandComponentController.filterSheet(topLeft, bottomRight, values, columns,popupStage);
    }

    @FXML
    void addAnotherColumnBtnPressed(ActionEvent event) {
        // Create the Text
        Text text = new Text("Then By");
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);

        // Create the ComboBox for columns
        ComboBox<String> columnsComboBox = new ComboBox<>();
        columnsComboBox.setMinWidth(105.0);
        columnsComboBox.setPrefWidth(150.0);
        for(int col = 0; col < columnsNumberInSheet; col++){
            String colName = "Column " + Character.toString('A' + col);
            columnsComboBox.getItems().add(colName);

        }

        //Create the ComboBox for values
        ComboBox<String> valuesComboBox = new ComboBox<>();
        valuesComboBox.setMinWidth(105.0);
        valuesComboBox.setPrefWidth(150.0);

        columnsComboBox.setOnAction(e -> {
            valuesComboBox.getItems().clear();
            int col = columnsComboBox.getSelectionModel().getSelectedIndex();
            valuesComboBox.getItems().addAll(commandComponentController.getValuesFromColumn(col + 1));
        });

        // Set the GridPane row and column index for both elements
        //int nextRowIndex = getNextRowIndex(GridPaneOfColumns);
        GridPane.setRowIndex(text, numberOfColumns2Filter);
        GridPane.setColumnIndex(text, 0);

        GridPane.setRowIndex(columnsComboBox, numberOfColumns2Filter);
        GridPane.setColumnIndex(columnsComboBox, 1);

        GridPane.setRowIndex(valuesComboBox, numberOfColumns2Filter);
        GridPane.setColumnIndex(valuesComboBox, 2);

        // Set margin for the ComboBox
        GridPane.setMargin(columnsComboBox, new Insets(10, 0, 10, 0));

        // Set margin for the ComboBox
        GridPane.setMargin(valuesComboBox, new Insets(10, 0, 10, 0));

        // Add the Text and ComboBox to the GridPane
        GridPaneOfColumns.getChildren().addAll(text, columnsComboBox, valuesComboBox);
        numberOfColumns2Filter++;
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    public void setCommandComponentController(CommandComponentController commandComponentController) {
        this.commandComponentController = commandComponentController;
    }

    public void setColumnsNumberInSheet(int columnsNumber) {
        this.columnsNumberInSheet = columnsNumber;
        for(int col = 0; col < columnsNumber; col++){
            String colName = "Column " + Character.toString('A' + col);
            firstColumnPicker.getItems().add(colName);
        }
        firstColumnPicker.setOnAction(e -> {
            valuePicker.getItems().clear();
            int col = firstColumnPicker.getSelectionModel().getSelectedIndex();
            valuePicker.getItems().addAll(commandComponentController.getValuesFromColumn(col + 1));
        });
    }

    public String getTopLeft() {
        return topLeftText.getText();
    }

    public String getBottomRight() {
        return bottomRightText.getText();
    }

    public List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        for (int i = 0; i < numberOfColumns2Filter; i++) {
            ComboBox<String> comboBox = (ComboBox<String>) getChildFromGridPane(GridPaneOfColumns, i, 1);
            columns.add(comboBox.getValue().substring(7));
        }
        return columns;
    }

    public List<String> getValues() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < numberOfColumns2Filter; i++) {
            ComboBox<String> comboBox = (ComboBox<String>) getChildFromGridPane(GridPaneOfColumns, i, 2);
            values.add(comboBox.getValue());
        }
        return values;
    }

    public static Node getChildFromGridPane(GridPane gridPane, int rowIndex, int columnIndex) {
        for (Node node : gridPane.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer column = GridPane.getColumnIndex(node);

            if (row == null) row = 0; // Default to row 0 if not specified
            if (column == null) column = 0; // Default to column 0 if not specified

            if (row == rowIndex && column == columnIndex) {
                return node;
            }
        }
        return null; // No child found at specified row and column
    }
}
