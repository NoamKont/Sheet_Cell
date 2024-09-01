package body;

import dto.SheetDTO;
import dto.impl. CellDTO;
import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Logic {
    CellDTO getCell(String cellID);
    int getRowsNumber();
    int getColumnsNumber();
    void updateCell(String cellId, String value);
    SheetDTO getSheet();
    void creatNewSheet(String path)throws JAXBException, FileNotFoundException, IOException;
    List<Integer> getNumberOfUpdatePerVersion();
    SheetDTO getSheetbyVersion(int version);
    String saveToFile(String name) throws IOException;
    void loadFromFile(String path) throws IOException, ClassNotFoundException;
    List<Sheet> getMainSheet();
}
