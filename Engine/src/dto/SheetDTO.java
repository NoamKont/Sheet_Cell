package dto;

import body.Cell;
import body.Coordinate;
import dto.impl.CellDTO;
import expression.api.EffectiveValue;

import java.util.Map;

public interface SheetDTO {
    String getSheetName();
    int getVersion();
    int getThickness();
    int getWidth();
    int getRowCount();
    int getColumnCount();
    EffectiveValue getEfectivevalueCell(Coordinate coordinate);
    Map<Coordinate, CellDTO> getActiveCells();
}
