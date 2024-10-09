package expression.Range.api;

import body.Cell;
import body.Coordinate;
import body.Sheet;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.util.Set;

public interface Range extends EffectiveValue {
    String getRangeName();
    String getTopLeftCellId();
    String getRightBottomCellId();
    Set<Cell> getRangeCells();
    Sheet getSheet();

    void setRangeName(String rangeName);
    void setSheet(Sheet sheet);
    void setRangeCells();

    Boolean isRangeValid(String topLeftCellId, String rightBottomCellId);
    Set<Cell> getCells();
    Set<Coordinate> getCellCoordinates();

}
