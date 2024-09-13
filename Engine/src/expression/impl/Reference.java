package expression.impl;

import body.Cell;
import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class Reference implements EffectiveValue,Serializable {

    private Cell cell;

    public Reference(Cell cell) {
        this.cell = cell;
    }

    @Override
    public EffectiveValue evaluate() {return new Reference(cell);}

    @Override
    public String expressionTOtoString() {
        return "{REF," + cell.getId() + "}";
    }

    @Override
    public String toString() {
        return cell.getEffectiveValue().toString();
    }

    @Override
    public boolean isNaN() {
        return cell.getEffectiveValue().isNaN();
    }

    @Override
    public boolean isUndefined() {
        return cell.getEffectiveValue().isUndefined();
    }

    @Override
    public boolean isUnknown() {
        return cell.getEffectiveValue().isUnknown();
    }

    @Override
    public CellType getCellType() {
        return cell.getEffectiveValue().getCellType();
    }
    @Override
    public Object getValue() {
        return cell.getEffectiveValue().getValue();
    }

    public Cell getCell() {
            return cell;
    }

    public void setCell(Cell value) {
        this.cell = value;
    }

    public EffectiveValue getEffectiveValue() {
        return cell.getEffectiveValue();
    }
}