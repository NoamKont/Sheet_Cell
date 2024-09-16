package MainMenu.SideBar.Command;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class sortPopUPController {

    @FXML
    private GridPane GridPaneOfColumns;

    @FXML
    private TextField bottomRightText;

    @FXML
    private ComboBox<String> firstColumnPicker;

    @FXML
    private TextField topLeftText;

    private Integer numberOfColumns = 1;

    private CommandComponentController commandComponentController;

    private Stage popupStage;

    private int columnsNumber;

    public void setCommandComponentController(CommandComponentController commandComponentController) {
        this.commandComponentController = commandComponentController;
    }

    public void setColumnsNumber(int columnsNumber) {
        this.columnsNumber = columnsNumber;
        for(int col = 0; col < columnsNumber; col++){
            String colName = "Column " + Character.toString('A' + col);
            firstColumnPicker.getItems().add(colName);
        }
    }

    @FXML
    void SortBtnClicked(ActionEvent event) {
        String topLeft = topLeftText.getText().toUpperCase();
        String bottomRight = bottomRightText.getText().toUpperCase();
        String[] columns = getColumns();
        try{
            commandComponentController.sortSheet(topLeft, bottomRight, columns,popupStage);
        }catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in sorting sheet");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
        }
        finally {
            topLeftText.clear();
            bottomRightText.clear();
        }

    }

    @FXML
    void addAnotherColumnBtnPressed(ActionEvent event) {
        // Create the Text
        Text text = new Text("Then By");
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);

        // Create the ComboBox
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setMinWidth(105.0);
        comboBox.setPrefWidth(150.0);
        for(int col = 0; col < columnsNumber; col++){
            String colName = "Column " + Character.toString('A' + col);
            comboBox.getItems().add(colName);

        }
        // Set the GridPane row and column index for both elements
        //int nextRowIndex = getNextRowIndex(GridPaneOfColumns);
        GridPane.setRowIndex(text, numberOfColumns);
        GridPane.setColumnIndex(text, 0);

        GridPane.setRowIndex(comboBox, numberOfColumns);
        GridPane.setColumnIndex(comboBox, 1);

        // Set margin for the ComboBox
        GridPane.setMargin(comboBox, new Insets(10, 0, 10, 0));

        // Add the Text and ComboBox to the GridPane
        GridPaneOfColumns.getChildren().addAll(text, comboBox);
        numberOfColumns++;
    }

    public String getTopLeft() {
        return topLeftText.getText();
    }

    public String getBottomRight() {
        return bottomRightText.getText();
    }

    public String[] getColumns() {
        String[] columns = new String[numberOfColumns];
        //columns[0] = firstColumnPicker.getValue();
        for (int i = 0; i < numberOfColumns; i++) {
            ComboBox<String> comboBox = (ComboBox<String>) getChildFromGridPane(GridPaneOfColumns, i, 1);
            columns[i] = comboBox.getValue().substring(7);
        }
        return columns;
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
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
