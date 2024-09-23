package body;

import expression.Range.api.Range;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Sheet {
    String getSheetName();
    Cell getCell(String cellID);
    Cell getCell(Coordinate coordinate);
    int getRowCount();
    int getColumnCount();
    int getThickness();
    int getWidth();
    int getVersion();
    int getCountUpdateCell();
    Map<Coordinate, Cell> getActiveCells();
    Map<String, Range> getAllRanges();
    Set<Cell> getRangeCells(String rangeName);
    String getUsername();

    void setUsername(String username);
    void setVersion(int version);
    void setUpdateCellCount(int countUpdateCell);

    Set<Coordinate> addRange(String rangeName, String topLeftCellId, String rightBottomCellId);
    void deleteRange(String rangeName);

    void updateCell(String cellId, String value);
    void updateListsOfDependencies(Coordinate coord);
    void updateCellDitels(String cellId, String value);
    void updateCellEffectiveValue(String cellId);

    boolean isCellExist(String cellID);

    Sheet sortSheet(String topLeft, String bottomRight, String... columns);
    Sheet filterSheet(String topLeft, String bottomRight, List<List<String>> value, List<String> columns);


    List<String> getValuesFromColumn(Integer columnIndex,int top,int bottom);

    void checkValidBounds(Coordinate coordinate);
}
