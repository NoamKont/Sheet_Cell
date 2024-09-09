package UIbody;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

import java.util.HashSet;
import java.util.Set;

public class UIColumn {
    final private StringProperty name = new SimpleStringProperty();
    private Integer width;
    private Integer thickness;
    Set<UICell> cells = new HashSet<>();
    private ObjectProperty<Label> columnLabel = new SimpleObjectProperty<>();

    UIColumn(String name, Integer width, Integer thickness) {
        this.name.set(name);
        this.width = width;
        this.thickness = thickness;
    }
    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    public void addCell(UICell cell){
        cells.add(cell);
        cell.setColumn(this);
    }

    public Integer getThickness() {
        return thickness;
    }

    public Integer getWidth() {
        return width;
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

}
