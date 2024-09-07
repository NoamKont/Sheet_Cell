package expression.Range.api;

import body.Cell;
import expression.api.Expression;

import java.util.Set;

public interface Range extends Expression{
    String getRangeName();
    void setRangeName(String rangeName);
    Boolean isRangeValid(String topLeftCellId, String rightBottomCellId);
    Set<Cell> getCells();
}
