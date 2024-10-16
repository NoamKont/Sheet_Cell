package MainMenu.SideBar.Command;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class FilterPopUpController implements Initializable {

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

    @FXML
    private Button addFilterColumnBtn;

    @FXML
    private Button filterBtn;

    private Integer numberOfColumns2Filter = 1;

    private CommandComponentController commandComponentController;

    private Stage popupStage;

    private int columnsNumberInSheet;

    private BooleanProperty rangeFilled = new SimpleBooleanProperty(false);

    @FXML
    void filterBtnClicked(ActionEvent event) {
        if(topLeftText.getText().isEmpty() || bottomRightText.getText().isEmpty() || firstColumnPicker.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in filtering sheet");
            alert.setContentText("Please fill in the top left and bottom right cells");
            alert.showAndWait();
            return;
        }

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
            int top = Integer.parseInt(topLeftText.getText().substring(1));
            int bottom = Integer.parseInt(bottomRightText.getText().substring(1));
            Set<String> values = commandComponentController.getValuesFromColumn(col + 1,top,bottom);
            for(String value : values){
                CheckBox checkBox = new CheckBox(value);
                CustomMenuItem item = new CustomMenuItem(checkBox,false);
                menuButton.getItems().add(item);
            }
        });
        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> {
            int row = GridPane.getRowIndex(deleteBtn);
            GridPaneOfColumns.getChildren().remove(text);
            GridPaneOfColumns.getChildren().remove(columnsComboBox);
            GridPaneOfColumns.getChildren().remove(menuButton);
            GridPaneOfColumns.getChildren().remove(deleteBtn);
            numberOfColumns2Filter--;
            if(numberOfColumns2Filter < 5){
                addFilterColumnBtn.setDisable(false);
            }
            for(int i = row; i < numberOfColumns2Filter; i++){
                Button b = (Button) getChildFromGridPane(GridPaneOfColumns, i + 1, 0);
                Text t = (Text) getChildFromGridPane(GridPaneOfColumns, i + 1, 1);
                ComboBox<String> cb = (ComboBox<String>) getChildFromGridPane(GridPaneOfColumns, i + 1, 2);
                MenuButton mb = (MenuButton) getChildFromGridPane(GridPaneOfColumns, i + 1, 3);
                GridPane.setRowIndex(t, i);
                GridPane.setRowIndex(cb, i);
                GridPane.setRowIndex(mb, i);
                GridPane.setRowIndex(b, i );
            }
        });

        // Set the GridPane row and column index for both elements
        GridPane.setRowIndex(deleteBtn, numberOfColumns2Filter);
        GridPane.setColumnIndex(deleteBtn, 0);

        GridPane.setRowIndex(text, numberOfColumns2Filter);
        GridPane.setColumnIndex(text, 1);

        GridPane.setRowIndex(columnsComboBox, numberOfColumns2Filter);
        GridPane.setColumnIndex(columnsComboBox, 2);

        GridPane.setRowIndex(menuButton, numberOfColumns2Filter);
        GridPane.setColumnIndex(menuButton, 3);

        // Set margin for the ComboBox
        GridPane.setMargin(columnsComboBox, new Insets(10, 0, 10, 0));

        // Set margin for the menuButton
        GridPane.setMargin(menuButton, new Insets(10, 0, 10, 0));

        // Add the Text and ComboBox to the GridPane
        GridPaneOfColumns.getChildren().addAll(deleteBtn, text, columnsComboBox, menuButton);
        numberOfColumns2Filter++;

        if(numberOfColumns2Filter == 5){
            addFilterColumnBtn.setDisable(true);
        }

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
            int top = Integer.parseInt(topLeftText.getText().substring(1));
            int bottom = Integer.parseInt(bottomRightText.getText().substring(1));
            Set<String> values = commandComponentController.getValuesFromColumn(col + 1,top,bottom);
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
            ComboBox<String> comboBox = (ComboBox<String>) getChildFromGridPane(GridPaneOfColumns, i, 2);
            columns.add(comboBox.getValue().substring(7));
        }
        return columns;
    }

    public List<List<String>> getValues() {
        List<List<String>> values = new ArrayList<>();
        for (int i = 0; i < numberOfColumns2Filter; i++) {
            List<String> columnValues = new ArrayList<>();
            MenuButton menuButton = (MenuButton) getChildFromGridPane(GridPaneOfColumns, i, 3);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rangeFilled.bind(Bindings.createBooleanBinding(() -> !topLeftText.getText().isEmpty() && !bottomRightText.getText().isEmpty(), topLeftText.textProperty(), bottomRightText.textProperty()));
        filterBtn.disableProperty().bind(rangeFilled.not());
        GridPaneOfColumns.disableProperty().bind(rangeFilled.not());
    }
}
