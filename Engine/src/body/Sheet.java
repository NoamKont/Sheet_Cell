package body;

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
    Set<Cell> getRangeCells(String rangeName);

    void setVersion(int version);
    void setUpdateCellCount(int countUpdateCell);
    void addRange(String rangeName, String topLeftCellId, String rightBottomCellId);

    void updateCell(String cellId, String value);
    void updateListsOfDependencies(Coordinate coord);
    void updateCellDitels(String cellId, String value);
    void updateCellEffectiveValue(String cellId);

    boolean isCellExist(String cellID);
}
