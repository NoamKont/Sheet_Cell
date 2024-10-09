package dto;

import body.Cell;
import body.Coordinate;
import dto.impl.CellDTO;

import java.util.Set;

public interface RangeDTO {
    Boolean isRangeValid(String topLeftCellId, String rightBottomCellId);
    Set<Coordinate> getCellCoordinates();
    String getRangeName();
    String getTopLeftCellId();
    String getRightBottomCellId();
    Set<CellDTO> getRangeCells();
}
