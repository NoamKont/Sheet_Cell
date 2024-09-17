package MainMenu.SideBar.Command;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
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

    @FXML
    private Button addColumnBtn;

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
        Button deleteBtn = new Button("Delete");

        deleteBtn.setOnAction(e -> {
            int row = GridPane.getRowIndex(deleteBtn);
            GridPaneOfColumns.getChildren().remove(text);
            GridPaneOfColumns.getChildren().remove(deleteBtn);
            GridPaneOfColumns.getChildren().remove(comboBox);
            numberOfColumns--;
            if(numberOfColumns < 5){
                addColumnBtn.setDisable(false);
            }
            for(int i = row; i < numberOfColumns; i++){
                Button b = (Button) getChildFromGridPane(GridPaneOfColumns, i + 1, 0);
                Text t = (Text) getChildFromGridPane(GridPaneOfColumns, i + 1, 1);
                ComboBox<String> cb = (ComboBox<String>) getChildFromGridPane(GridPaneOfColumns, i + 1, 2);
                GridPane.setRowIndex(b, i );
                GridPane.setRowIndex(t, i);
                GridPane.setRowIndex(cb, i);
            }
        });
        // Set the GridPane row and column index for both elements
        GridPane.setRowIndex(deleteBtn, numberOfColumns);
        GridPane.setColumnIndex(deleteBtn, 0);

        GridPane.setRowIndex(text, numberOfColumns);
        GridPane.setColumnIndex(text, 1);

        GridPane.setRowIndex(comboBox, numberOfColumns);
        GridPane.setColumnIndex(comboBox, 2);

        // Set margin for the ComboBox
        GridPane.setMargin(comboBox, new Insets(10, 0, 10, 0));

        // Add the Text and ComboBox to the GridPane
        GridPaneOfColumns.getChildren().addAll(deleteBtn, text, comboBox);
        numberOfColumns++;
        if(numberOfColumns == 5){
            addColumnBtn.setDisable(true);
        }
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
