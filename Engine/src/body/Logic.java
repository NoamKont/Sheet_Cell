package body;

import body.permission.PermissionManager;
import dto.SheetDTO;
import dto.impl. CellDTO;
import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Logic {

    void creatNewSheet(String path)throws JAXBException, FileNotFoundException, IOException;

    void updateCell(String cellId, String value, String username);

    CellDTO getCell(String cellID);
    int getRowsNumber();
    SheetDTO getSheet();
    int getColumnsNumber();
    List<Integer> getNumberOfUpdatePerVersion();
    SheetDTO getSheetbyVersion(int version);
    //TODO change to List<SheetDTO>
    List<Sheet> getMainSheet();

    PermissionManager getPermissionManager();
    String getOwner();

    void setOwner(String owner);

    SheetDTO dynamicAnalysis(String cellId, String value);
    SheetDTO sortSheet(String topLeft, String bottomRight, String... columns);
    SheetDTO filterSheet(String topLeft, String bottomRight, List<List<String>> value, List<String> columns);
    Set<Coordinate> addRangeToSheet(String rangeName, String topLeft, String bottomRight);
    void deleteRange(String rangeName);
    boolean isCellExist(String cellID);

    String saveToFile(String name) throws IOException;
    void loadFromFile(String path) throws IOException, ClassNotFoundException;
}
