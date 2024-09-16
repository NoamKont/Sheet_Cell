package MainMenu.SideBar.Command;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    private MenuButton valuePicker;

    private Integer numberOfColumns2Filter = 1;

    private CommandComponentController commandComponentController;

    private Stage popupStage;

    private int columnsNumberInSheet;

    @FXML
    void filterBtnClicked(ActionEvent event) {
        String topLeft = topLeftText.getText().toUpperCase();
        String bottomRight = bottomRightText.getText().toUpperCase();
        List<String> columns = getColumns();
        List<List<String>> values = getValues();
        try{
            commandComponentController.filterSheet(topLeft, bottomRight, values, columns,popupStage);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in filtering sheet");
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

        // Create the ComboBox for columns
        ComboBox<String> columnsComboBox = new ComboBox<>();
        columnsComboBox.setMinWidth(105.0);
        columnsComboBox.setPrefWidth(150.0);
        for(int col = 0; col < columnsNumberInSheet; col++){
            String colName = "Column " + Character.toString('A' + col);
            columnsComboBox.getItems().add(colName);
        }

        // Create the Menu Button for values each value is a checkbox
        MenuButton menuButton = new MenuButton("Values");
        menuButton.setMinWidth(105.0);
        menuButton.setPrefWidth(150.0);
        menuButton.setAlignment(javafx.geometry.Pos.CENTER);
        columnsComboBox.setOnAction(e -> {
            menuButton.getItems().clear();
            int col = columnsComboBox.getSelectionModel().getSelectedIndex();
            List<String> values = commandComponentController.getValuesFromColumn(col + 1);
            for(String value : values){
                CheckBox checkBox = new CheckBox(value);
                CustomMenuItem item = new CustomMenuItem(checkBox,false);
                menuButton.getItems().add(item);
            }
        });

        // Set the GridPane row and column index for both elements
        GridPane.setRowIndex(text, numberOfColumns2Filter);
        GridPane.setColumnIndex(text, 0);

        GridPane.setRowIndex(columnsComboBox, numberOfColumns2Filter);
        GridPane.setColumnIndex(columnsComboBox, 1);

        GridPane.setRowIndex(menuButton, numberOfColumns2Filter);
        GridPane.setColumnIndex(menuButton, 2);

        // Set margin for the ComboBox
        GridPane.setMargin(columnsComboBox, new Insets(10, 0, 10, 0));

        // Set margin for the menuButton
        GridPane.setMargin(menuButton, new Insets(10, 0, 10, 0));

        // Add the Text and ComboBox to the GridPane
        GridPaneOfColumns.getChildren().addAll(text, columnsComboBox, menuButton);
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
            List<String> values = commandComponentController.getValuesFromColumn(col + 1);
            for(String value : values){
                CheckBox checkBox = new CheckBox(value);
                CustomMenuItem item = new CustomMenuItem(checkBox,false);
                valuePicker.getItems().add(item);
            }
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

    public List<List<String>> getValues() {
        List<List<String>> values = new ArrayList<>();
        for (int i = 0; i < numberOfColumns2Filter; i++) {
            List<String> columnValues = new ArrayList<>();
            MenuButton menuButton = (MenuButton) getChildFromGridPane(GridPaneOfColumns, i, 2);
            menuButton.getItems().forEach(item -> {
                CustomMenuItem customMenuItem = (CustomMenuItem) item;
                CheckBox checkBox = (CheckBox) customMenuItem.getContent();
                if (checkBox.isSelected()) {
                    columnValues.add(checkBox.getText());
                }
            });
            values.add(columnValues);
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
