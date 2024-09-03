package body;

import java.util.List;
import java.util.Map;

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

    void setVersion(int version);
    void setUpdateCellCount(int countUpdateCell);

    void updateCell(String cellId, String value);
    void updateListsOfDependencies(Coordinate coord);
    void updateCellDitels(String cellId, String value);
    void updateCellEffectiveValue(String cellId);

    boolean isCellExist(String cellID);
}
