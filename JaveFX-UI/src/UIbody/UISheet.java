package UIbody;


import body.Cell;
import body.Coordinate;
import body.impl.CoordinateImpl;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.Range.api.Range;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UISheet {

    private IntegerProperty sheetVersion = new SimpleIntegerProperty();
    private IntegerProperty thickness = new SimpleIntegerProperty();;
    private IntegerProperty width = new SimpleIntegerProperty();;
    private Map<Coordinate, UICell> activeCells = new HashMap<>();
    private MapProperty<String, Set<Coordinate>> rangeCells =  new SimpleMapProperty<>(FXCollections.observableHashMap());
    private Map<String, UIGridPart> rows = new HashMap<>();
    private Map<String, UIGridPart> columns = new HashMap<>();



    public UISheet(){}

    public UISheet(SheetDTO sheetDTO) {
        thickness.setValue(sheetDTO.getThickness());
        width.setValue(sheetDTO.getWidth());
        sheetVersion.setValue(sheetDTO.getVersion());

        for (Map.Entry<String, Range> entry : sheetDTO.getAllRanges().entrySet()) {
            if(!rangeCells.containsKey(entry.getKey())){
                rangeCells.put(entry.getKey(), entry.getValue().getCellCoordinates());
            }
        }
        for(int i = 1; i <= sheetDTO.getRowCount(); i++){
            rows.putIfAbsent(String.valueOf(i), new UIGridPart(String.valueOf(i), width.getValue(), thickness.getValue(), true));
            for(int j = 1; j <= sheetDTO.getColumnCount(); j++){
                columns.putIfAbsent(String.valueOf((char)('A' + j - 1)), new UIGridPart(String.valueOf((char)('A' + j - 1)), width.getValue(), thickness.getValue(), false));
                Coordinate coordinate = new CoordinateImpl(i, j);

                if(sheetDTO.getActiveCells().containsKey(coordinate)){
                    UICell cell = new UICell(sheetDTO.getActiveCells().get(coordinate));
                    activeCells.put(coordinate, cell);
                    //activeCells.put(coordinate, new UICell(sheetDTO.getActiveCells().get(coordinate)));
                    rows.get(String.valueOf(i)).addCell(cell);
                    columns.get(String.valueOf((char)('A' + j - 1))).addCell(cell);

                }
                else{
                    UICell cell = new UICell(String.valueOf((char)('A' + j - 1)) + (i));
                    activeCells.put(coordinate, cell);
                    //activeCells.put(coordinate, new UICell(String.valueOf((char)('A' + j - 1)) + (i)));
                    rows.get(String.valueOf(i)).addCell(cell);
                    columns.get(String.valueOf((char)('A' + j - 1))).addCell(cell);
                }
            }
        }
    }

    public void updateSheet(SheetDTO sheetDTO) {
        thickness.setValue(sheetDTO.getThickness());
        width.setValue(sheetDTO.getWidth());
        sheetVersion.setValue(sheetDTO.getVersion());
        //add only the name of the range
        for (Map.Entry<String, Range> entry : sheetDTO.getAllRanges().entrySet()) {
            if(!rangeCells.containsKey(entry.getKey())){
                rangeCells.put(entry.getKey(), entry.getValue().getCellCoordinates());
            }
        }
        //update all the cells that active by engine
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

    public MapProperty<String, Set<Coordinate>> rangeMapProperty() {
        return rangeCells;
    }

    public Set<Coordinate> getCoordinatesOfRange(String rangeName) {
        if(rangeCells.containsKey(rangeName)){
            return rangeCells.get(rangeName);
        }
        else{
            return null;
        }
    }

    public UIGridPart getRow(String text) {
        return rows.get(text);
    }
    public UIGridPart getColumn(String text) {
        return columns.get(text);
    }
}

