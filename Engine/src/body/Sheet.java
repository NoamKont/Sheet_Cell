package body;

import body.impl.Graph;
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
    Graph getGraph();

    void setUsername(String username);
    void setVersion(int version);
    void setUpdateCellCount(int countUpdateCell);
    void setGraph(Graph graph);

    Set<Coordinate> addRange(String rangeName, String topLeftCellId, String rightBottomCellId);
    void deleteRange(String rangeName);

    void updateCell(String cellId, String value, String username);
    void updateListsOfDependencies(Coordinate coord);
    void updateCellDetails(String cellId, String value, String username);
    void updateCellEffectiveValue(String cellId,String username);

    boolean isCellExist(String cellID);

    Sheet sortSheet(String topLeft, String bottomRight, String... columns);
    Sheet filterSheet(String topLeft, String bottomRight, List<List<String>> value, List<String> columns);


    List<String> getValuesFromColumn(Integer columnIndex,int top,int bottom);

    void checkValidBounds(Coordinate coordinate);


    String getPermission();
}
