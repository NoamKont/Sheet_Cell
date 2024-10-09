package client.component.main.UIbody;



import body.Coordinate;
import body.impl.CoordinateImpl;
import dto.RangeDTO;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.Range.api.Range;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;

import java.util.*;

public class UISheet {

    private IntegerProperty sheetVersion = new SimpleIntegerProperty();
    private IntegerProperty thickness = new SimpleIntegerProperty();;
    private IntegerProperty width = new SimpleIntegerProperty();
    private Map<Coordinate, UICell> activeCells = new HashMap<>();
    private MapProperty<String, Set<Coordinate>> ranges =  new SimpleMapProperty<>(FXCollections.observableHashMap());
    private Map<String, UIGridPart> rows = new HashMap<>();
    private Map<String, UIGridPart> columns = new HashMap<>();
    private int rowsNumber;
    private int columnsNumber;
    private String sheetName;



    public UISheet(){}

    public UISheet(SheetDTO sheetDTO) {
        sheetName = sheetDTO.getSheetName();
        rowsNumber = sheetDTO.getRowCount();
        columnsNumber = sheetDTO.getColumnCount();
        thickness.setValue(sheetDTO.getThickness());
        width.setValue(sheetDTO.getWidth());
        //sheetVersion.setValue(sheetDTO.getVersion());

        //range map and add the all ranges as entry
        for (Map.Entry<String, RangeDTO> entry : sheetDTO.getAllRanges().entrySet()) {
            if(!ranges.containsKey(entry.getKey())){
                ranges.put(entry.getKey(), entry.getValue().getCellCoordinates());

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
                    rows.get(String.valueOf(i)).addCell(cell);
                    columns.get(String.valueOf((char)('A' + j - 1))).addCell(cell);

                }
                else{
                    UICell cell = new UICell(String.valueOf((char)('A' + j - 1)) + (i));
                    activeCells.put(coordinate, cell);
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

        //RESET range map and add the all ranges as entry
        ranges.clear();
        for (Map.Entry<String, RangeDTO> entry : sheetDTO.getAllRanges().entrySet()) {
            if(!ranges.containsKey(entry.getKey())){
                ranges.put(entry.getKey(), entry.getValue().getCellCoordinates());
            }
        }
        //update all the cells that active by engine
        for (Map.Entry<Coordinate, CellDTO> entry : sheetDTO.getActiveCells().entrySet()) {
            Coordinate coordinate = new CoordinateImpl(entry.getKey());
            activeCells.get(coordinate).updateUICell(entry.getValue());
        }
    }

    public void addRange(String rangeName, Set<Coordinate> coordinates) {
        ranges.put(rangeName, coordinates);
    }
    public void deleteRange(String rangeName) {
        ranges.remove(rangeName);
    }

    public void setCellLabel(Coordinate coordinate, Label label) {
        activeCells.get(coordinate).setCellLabel(label);
    }

    public UICell getCell(Coordinate coordinate) {
        return activeCells.get(coordinate);
    }

    public Map<Coordinate, UICell> getActiveCells() {
        return activeCells;
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
        return ranges;
    }

    public Set<Coordinate> getCoordinatesOfRange(String rangeName) {
        if(ranges.containsKey(rangeName)){
            return ranges.get(rangeName);
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

    public int getRowsNumber() {
        return rowsNumber;
    }
    public int getColumnsNumber() {
        return columnsNumber;
    }
    public String getSheetName() {
        return sheetName;
    }

    public List<String> getValuesFromColumn(Integer columnIndex, int top, int bottom) {
        List<String> values = new ArrayList<>();
        if(top > bottom){
            throw new IllegalArgumentException("Top value can't be bigger than bottom value");
        }

        List<Map.Entry<Coordinate, UICell>> sortedCells = new ArrayList<>();

        // Collect cells that match the column and row criteria
        for (Map.Entry<Coordinate, UICell> entry : activeCells.entrySet()) {
            Coordinate coordinate = entry.getKey();
            UICell cell = entry.getValue();

            if (coordinate.getColumn() == columnIndex && coordinate.getRow() >= top && coordinate.getRow() <= bottom) {
                sortedCells.add(entry);
            }
        }

        // Sort the list by row in ascending order
        sortedCells.sort(Comparator.comparingInt(entry -> entry.getKey().getRow()));

        // Add the sorted values to the list
        for (Map.Entry<Coordinate, UICell> entry : sortedCells) {
            values.add(entry.getValue().effectiveValueProperty().getValue());
        }


        return values;

    }
}

