package dto.impl;

import body.Coordinate;
import body.Sheet;
import body.impl.Graph;
import dto.RangeDTO;
import dto.SheetDTO;
import expression.Range.api.Range;
import expression.api.EffectiveValue;
import body.Cell;

import java.util.*;

public class ImplSheetDTO implements SheetDTO {
    //final private Sheet currSheet;
    private int sheetVersion;
    final private String sheetName;
    final private int thickness;
    final private int width;
    final private int row;
    final private int col;
    private Map<Coordinate, CellDTO> activeCells = new HashMap<>();
    private Graph graph;
    private int countUpdateCell;
    private Map<String, RangeDTO> rangeMap = new HashMap<>();
    private String username;
    private String filePath;


    public ImplSheetDTO(Sheet sheet) {
        this.sheetVersion = sheet.getVersion();
        this.sheetName = sheet.getSheetName();
        this.thickness = sheet.getThickness();
        this.width = sheet.getWidth();
        this.row = sheet.getRowCount();
        this.col = sheet.getColumnCount();
        this.username = sheet.getUsername();
        this.graph = sheet.getGraph();
        this.countUpdateCell = sheet.getCountUpdateCell();
        this.filePath = sheet.getFilePath();
        for(Map.Entry<String, Range> entry : sheet.getAllRanges().entrySet()){
            rangeMap.put(entry.getKey(), new ImplRangeDTO(entry.getValue()));
        }
//        this.rangeMap = sheet.getAllRanges();

        for (Map.Entry<Coordinate, Cell> entry : sheet.getActiveCells().entrySet()) {
            Coordinate coordinate = entry.getKey();
            Cell cell = entry.getValue();

            // Create a CellDTO using the constructor that takes a Cell
            CellDTO cellDTO = new CellDTO(cell);

            // Put the new CellDTO into the new map with the same Coordinate key
            activeCells.put(coordinate, cellDTO);
        }
    }

    @Override
    public String getOwner() {
        return username;
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public int getVersion() {
        return sheetVersion;
    }

    @Override
    public int getThickness() {
        return thickness;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getRowCount() {
        return row;
    }

    @Override
    public int getColumnCount() {
        return col;
    }

    @Override
    public String getEfectivevalueCell(Coordinate coordinate) {
        return activeCells.get(coordinate).getEffectiveValue();
    }

    @Override
    public Map<Coordinate, CellDTO> getActiveCells() {
        return activeCells;
    }

    @Override
    public Map<String, RangeDTO> getAllRanges() {
        return rangeMap;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public List<String> getValuesFromColumn(Integer columnIndex, int top, int bottom) {
        List<String> values = new ArrayList<>();
        if(top > bottom){
            throw new IllegalArgumentException("Top value can't be bigger than bottom value");
        }
        activeCells.forEach((coordinate, cell) -> {
            if (coordinate.getColumn() == columnIndex && coordinate.getRow() >= top && coordinate.getRow() <= bottom) {
                values.add(cell.getEffectiveValue());
            }
        });
        return values;
    }


//
//    @Override
//    public String getOwner() {
//        return currSheet.getUsername();
//    }
//
//    @Override
//    public String getSheetName() {
//        return currSheet.getSheetName();
//    }
//
//    @Override
//    public int getVersion() {
//        return currSheet.getVersion();
//    }
//
//    @Override
//    public int getThickness() {
//        return currSheet.getThickness();
//    }
//
//    @Override
//    public int getWidth() {
//        return currSheet.getWidth();
//    }
//
//    @Override
//    public int getRowCount() {
//        return currSheet.getRowCount();
//    }
//
//    @Override
//    public int getColumnCount() {
//        return currSheet.getColumnCount();
//    }
//
//    @Override
//    public EffectiveValue getEfectivevalueCell(Coordinate coordinate) {
//        Cell cell = currSheet.getCell(coordinate);
//        if (cell == null) {
//            return null;
//        }
//        return cell.getEffectiveValue();
//    }
//
//    @Override
//    public Map<Coordinate, CellDTO> getActiveCells() {
//        Map<Coordinate, CellDTO> dtoMap = new HashMap<>();
//
//        for (Map.Entry<Coordinate, Cell> entry : currSheet.getActiveCells().entrySet()) {
//            Coordinate coordinate = entry.getKey();
//            Cell cell = entry.getValue();
//
//            // Create a CellDTO using the constructor that takes a Cell
//            CellDTO cellDTO = new CellDTO(cell);
//
//            // Put the new CellDTO into the new map with the same Coordinate key
//            dtoMap.put(coordinate, cellDTO);
//        }
//
//        return dtoMap;
//    }
//
//    @Override
//    public Map<String, Range> getAllRanges() {
//        return currSheet.getAllRanges();
//    }
//
//    @Override
//    public List<String> getValuesFromColumn(Integer columnIndex, int top, int bottom) {
//        return currSheet.getValuesFromColumn(columnIndex,top,bottom);
//    }

}
