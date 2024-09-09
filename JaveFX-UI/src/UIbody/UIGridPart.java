package UIbody;

import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

import java.util.HashSet;
import java.util.Set;

public class UIGridPart {

    Boolean isRow;
    private final StringProperty name = new SimpleStringProperty();
    private IntegerProperty width = new SimpleIntegerProperty();
    private IntegerProperty thickness = new SimpleIntegerProperty();
    Set<UICell> cells = new HashSet<>();
    private ObjectProperty<Label> rowLabel = new SimpleObjectProperty<>();
    private ColumnConstraints columnConstraints = null;
    private RowConstraints rowConstraints = null;

    UIGridPart(String name, Integer width, Integer thickness, Boolean isRow) {
        this.name.set(name);
        this.width.setValue(width);
        this.thickness.setValue(thickness);
        this.isRow = isRow;
    }
    public void setWidth(Integer width) {
        this.width.set(width);
    }

    public void setThickness(Integer thickness) {
        this.thickness.set(thickness);
    }

    public void addCell(UICell cell){
        cells.add(cell);
        cell.setRow(this);
    }

    public Integer getThickness() {
        return thickness.getValue();
    }

    public Integer getWidth() {
        return width.getValue();
    }

    public IntegerProperty widthProperty() {
        return width;
    }
    public IntegerProperty thicknessProperty() {
        return thickness;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Set<UICell> getCells() {
        return cells;
    }
    public ObjectProperty<Label> rowLabelProperty() {
        return rowLabel;
    }
    public Label getRowLabel() {
        return rowLabel.get();
    }
    public void setRowLabel(Label rowLabel) {
        this.rowLabel.set(rowLabel);
    }

    public void alignCells(Pos pos) {
        for (UICell cell : cells) {
            cell.getCellLabel().setAlignment(pos);
        }
    }
    public void setRowConstraints(RowConstraints rowConstraints) {
        this.rowConstraints = rowConstraints;
        this.rowConstraints.prefHeightProperty().bind(this.thickness);
    }
    public void setColumnConstraints(ColumnConstraints columnConstraints) {
        this.columnConstraints = columnConstraints;
        this.columnConstraints.prefWidthProperty().bind(this.width);
    }
}
