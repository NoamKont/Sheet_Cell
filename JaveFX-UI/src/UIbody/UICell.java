package UIbody;

import body.Coordinate;
import dto.impl.CellDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class UICell {
    private StringProperty id = new SimpleStringProperty();
    private IntegerProperty lastVersionUpdate = new SimpleIntegerProperty();
    private StringProperty originalValue = new SimpleStringProperty();
    private StringProperty effectiveValue = new SimpleStringProperty();
    private ListProperty<Coordinate> cellsDependsOnThem = new SimpleListProperty<>();
    private ListProperty<Coordinate> cellsDependsOnHim = new SimpleListProperty<>();


    public UICell(CellDTO Cell) {
        id.setValue(Cell.getId());
        lastVersionUpdate.setValue(Cell.getLastVersionUpdate());
        originalValue.setValue(Cell.getOriginalValue());
        effectiveValue.setValue(Cell.getOriginalEffectiveValue().toString());
        cellsDependsOnThem.set(FXCollections.observableArrayList(Cell.getCellsDependsOnThem()));
        cellsDependsOnHim.set(FXCollections.observableArrayList(Cell.getCellsDependsOnHim()));
    }
    public UICell(){}

    public void updateUICell(CellDTO Cell) {
        id.setValue(Cell.getId());
        lastVersionUpdate.setValue(Cell.getLastVersionUpdate());
        originalValue.setValue(Cell.getOriginalValue());
        effectiveValue.setValue(Cell.getOriginalEffectiveValue().toString());
        cellsDependsOnThem.set(FXCollections.observableArrayList(Cell.getCellsDependsOnThem()));
        cellsDependsOnHim.set(FXCollections.observableArrayList(Cell.getCellsDependsOnHim()));
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
    public ListProperty<Coordinate> cellsDependsOnThemProperty() {
        return cellsDependsOnThem;
    }
    public ListProperty<Coordinate> cellsDependsOnHimProperty() {
        return cellsDependsOnHim;
    }

}
