package dto;

import body.Coordinate;
import dto.impl.CellDTO;
import expression.api.EffectiveValue;
import expression.Range.api.Range;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface SheetDTO {
    String getOwner();
    String getSheetName();
    int getVersion();
    int getThickness();
    int getWidth();
    int getRowCount();
    int getColumnCount();
    String getEfectivevalueCell(Coordinate coordinate);
    Map<Coordinate, CellDTO> getActiveCells();
    Map<String, RangeDTO> getAllRanges();
    String getFilePath();

    List<String> getValuesFromColumn(Integer columnIndex, int top, int bottom);
}
