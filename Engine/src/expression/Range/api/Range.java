package expression.Range.api;

import body.Cell;
import body.Coordinate;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.util.Set;

public interface Range extends EffectiveValue {
    String getRangeName();
    void setRangeName(String rangeName);
    Boolean isRangeValid(String topLeftCellId, String rightBottomCellId);
    Set<Cell> getCells();
    Set<Coordinate> getCellCoordinates();
}
