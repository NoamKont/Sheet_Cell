package UIbody;


import body.Coordinate;
import body.impl.CoordinateImpl;
import dto.SheetDTO;
import dto.impl.CellDTO;
import javafx.beans.property.*;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

public class UISheet {

    private IntegerProperty sheetVersion = new SimpleIntegerProperty();
    private IntegerProperty thickness = new SimpleIntegerProperty();;
    private IntegerProperty width = new SimpleIntegerProperty();;
    private Map<Coordinate, UICell> activeCells = new HashMap<>();

    public UISheet(){}

    public UISheet(SheetDTO sheetDTO) {
        thickness.setValue(sheetDTO.getThickness());
        width.setValue(sheetDTO.getWidth());
        sheetVersion.setValue(sheetDTO.getVersion());

        for(int i = 1; i <= sheetDTO.getRowCount(); i++){
            for(int j = 1; j <= sheetDTO.getColumnCount(); j++){
                Coordinate coordinate = new CoordinateImpl(i, j);
                if(sheetDTO.getActiveCells().containsKey(coordinate)){
                    activeCells.put(coordinate, new UICell(sheetDTO.getActiveCells().get(coordinate)));
                }
                else{
                    activeCells.put(coordinate, new UICell(String.valueOf((char)('A' + j - 1)) + (i)));
                }
            }
        }
//        for (Map.Entry<Coordinate, CellDTO> entry : sheetDTO.getActiveCells().entrySet()) {
//            Coordinate coordinate = entry.getKey();
//            CellDTO cell = entry.getValue();
//
//            // Create a UICell using the constructor that takes a CellDTO
//            UICell uiCell = new UICell(cell);
//
//            // Put the new UICell into the new map with the same Coordinate key
//            activeCells.put(coordinate, uiCell);
//        }
    }

    public void updateSheet(SheetDTO sheetDTO) {
        thickness.setValue(sheetDTO.getThickness());
        width.setValue(sheetDTO.getWidth());
        sheetVersion.setValue(sheetDTO.getVersion());
        for (Map.Entry<Coordinate, CellDTO> entry : sheetDTO.getActiveCells().entrySet()) {
            Coordinate coordinate = new CoordinateImpl(entry.getKey());
            activeCells.get(coordinate).updateUICell(entry.getValue());

        }
    }

    public void setCellLabel(Coordinate coordinate, Label label) {
        activeCells.get(coordinate).setCellLabel(label);
    }
    public UICell getCell(Coordinate coordinate) {
        return activeCells.get(coordinate);
    }
    public IntegerProperty sheetVersionProperty() {
        return sheetVersion;
    }
    public IntegerProperty thicknessProperty() {
        return thickness;
    }
    public IntegerProperty widthProperty() {
        return width;
    }

}
