package client.component.main.UIbody;


import body.Coordinate;
import dto.impl.CellDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class UICell {
    private StringProperty id = new SimpleStringProperty();
    private IntegerProperty lastVersionUpdate = new SimpleIntegerProperty();
    private StringProperty originalValue = new SimpleStringProperty();
    private StringProperty effectiveValue = new SimpleStringProperty();
    private ListProperty<Coordinate> cellsDependsOnThem = new SimpleListProperty<>();
    private ListProperty<Coordinate> cellsDependsOnHim = new SimpleListProperty<>();
    private ObjectProperty<Label> cellLabel = new SimpleObjectProperty<>();
    private UIGridPart row;
    private UIGridPart column;
    private StringProperty updateBy = new SimpleStringProperty();



    public UICell(CellDTO Cell) {
        id.setValue(Cell.getId());
        lastVersionUpdate.setValue(Cell.getLastVersionUpdate());
        originalValue.setValue(Cell.getOriginalValue());
        effectiveValue.setValue(Cell.getOriginalEffectiveValue().toString());
        updateBy.setValue(Cell.getUpdateBy()!= null ? Cell.getUpdateBy() : "");
        cellsDependsOnThem.set(Cell.getCellsDependsOnThem() != null ? FXCollections.observableArrayList(Cell.getCellsDependsOnThem()) : new SimpleListProperty<>());
        cellsDependsOnHim.set(Cell.getCellsDependsOnHim() != null ? FXCollections.observableArrayList(Cell.getCellsDependsOnHim()) : new SimpleListProperty<>());
//        if(Cell.getCellsDependsOnThem() == null){
//            cellsDependsOnThem = new SimpleListProperty<>();
//        }
//        else{
//            cellsDependsOnThem.set(FXCollections.observableArrayList(Cell.getCellsDependsOnThem()));
//        }
//        if(Cell.getCellsDependsOnHim() == null){
//            cellsDependsOnHim = new SimpleListProperty<>();
//        }
//        else{
//            cellsDependsOnHim.set(FXCollections.observableArrayList(Cell.getCellsDependsOnHim()));
//
//        }
        initCellLabel();

    }

    public UICell(String id){
        this.id.setValue(id);
        initCellLabel();
    }

    public UICell(){}

    public void updateUICell(UICell uiCell){
        id.setValue(uiCell.id.getValue());
        lastVersionUpdate.setValue(uiCell.lastVersionUpdate.getValue());
        originalValue.setValue(uiCell.originalValue.getValue());
        effectiveValue.setValue(uiCell.effectiveValue.getValue());
        cellsDependsOnThem.set(FXCollections.observableArrayList(uiCell.cellsDependsOnThem));
        cellsDependsOnHim.set(FXCollections.observableArrayList(uiCell.cellsDependsOnHim));
        cellLabel.set(uiCell.cellLabel.get());
        updateBy.setValue(uiCell.updateBy.getValue() != null ? uiCell.updateBy.getValue() : "");
//        updateBy.setValue(uiCell.updateBy.getValue());
    }

    public void updateUICell(CellDTO Cell) {
        id.setValue(Cell.getId());
        lastVersionUpdate.setValue(Cell.getLastVersionUpdate());
        originalValue.setValue(Cell.getOriginalValue());
        effectiveValue.setValue(Cell.getOriginalEffectiveValue().toString());
        updateBy.setValue(Cell.getUpdateBy());
        cellsDependsOnThem.set(Cell.getCellsDependsOnThem() != null ? FXCollections.observableArrayList(Cell.getCellsDependsOnThem()) : new SimpleListProperty<>());
        cellsDependsOnHim.set(Cell.getCellsDependsOnHim() != null ? FXCollections.observableArrayList(Cell.getCellsDependsOnHim()) : new SimpleListProperty<>());

//        if(Cell.getCellsDependsOnThem() == null){
//            cellsDependsOnThem = new SimpleListProperty<>();
//        }
//        else{
//            cellsDependsOnThem.set(FXCollections.observableArrayList(Cell.getCellsDependsOnThem()));
//        }
//        if(Cell.getCellsDependsOnHim() == null){
//            cellsDependsOnHim = new SimpleListProperty<>();
//        }
//        else{
//            cellsDependsOnHim.set(FXCollections.observableArrayList(Cell.getCellsDependsOnHim()));
//
//        }
    }

    public void setCellLabel(Label label){
        cellLabel.set(label);
    }

    public StringProperty idProperty() {
        return id;
    }
    public IntegerProperty lastVersionUpdateProperty() {
        return lastVersionUpdate;
    }
    public StringProperty originalValueProperty() {
        return originalValue;
    }
    public StringProperty effectiveValueProperty() {
        return effectiveValue;
    }
    public StringProperty updateByProperty() {
        return updateBy;
    }

    public ListProperty<Coordinate> cellsDependsOnThemProperty() {
        return cellsDependsOnThem;
    }
    public ListProperty<Coordinate> cellsDependsOnHimProperty() {
        return cellsDependsOnHim;
    }
    public Label getCellLabel(){
        return cellLabel.get();
    }
    public ObjectProperty<Label> cellLabelProperty() {
        return cellLabel;
    }

    public void setRow(UIGridPart row) {
        this.row = row;
    }
    public void setColumn(UIGridPart column) {
        this.column = column;
    }
    public UIGridPart getRow() {
        return row;
    }
    public UIGridPart getColumn() {
        return column;
    }

    private void initCellLabel() {
        Label label = new Label();
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setAlignment(javafx.geometry.Pos.CENTER);
        cellLabel.set(label);
    }

    public void clearCell() {
        id.setValue("");
        lastVersionUpdate.setValue(0);
        originalValue.setValue("");
        effectiveValue.setValue("");
        cellsDependsOnThem.set(FXCollections.observableArrayList());
        cellsDependsOnHim.set(FXCollections.observableArrayList());
        updateBy.setValue("");
    }
}
