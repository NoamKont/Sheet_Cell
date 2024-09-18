package dto.impl;

import body.Coordinate;
import body.Sheet;
import dto.SheetDTO;
import body.impl.ImplSheet;
import expression.Range.api.Range;
import expression.api.EffectiveValue;
import body.Cell;

import java.util.*;

public class ImplSheetDTO implements SheetDTO {
    final private Sheet currSheet;


    public ImplSheetDTO(Sheet sheet) {
        this.currSheet = sheet;
    }

    @Override
    public String getSheetName() {
        return currSheet.getSheetName();
    }

    @Override
    public int getVersion() {
        return currSheet.getVersion();
    }

    @Override
    public int getThickness() {
        return currSheet.getThickness();
    }

    @Override
    public int getWidth() {
        return currSheet.getWidth();
    }

    @Override
    public int getRowCount() {
        return currSheet.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return currSheet.getColumnCount();
    }

    @Override
    public EffectiveValue getEfectivevalueCell(Coordinate coordinate) {
        Cell cell = currSheet.getCell(coordinate);
        if (cell == null) {
            return null;
        }
        return cell.getEffectiveValue();
    }

    @Override
    public Map<Coordinate, CellDTO> getActiveCells() {
        Map<Coordinate, CellDTO> dtoMap = new HashMap<>();

        for (Map.Entry<Coordinate, Cell> entry : currSheet.getActiveCells().entrySet()) {
            Coordinate coordinate = entry.getKey();
            Cell cell = entry.getValue();

            // Create a CellDTO using the constructor that takes a Cell
            CellDTO cellDTO = new CellDTO(cell);

            // Put the new CellDTO into the new map with the same Coordinate key
            dtoMap.put(coordinate, cellDTO);
        }

        return dtoMap;
    }

    @Override
    public Map<String, Range> getAllRanges() {
        return currSheet.getAllRanges();
    }

    @Override
    public Set<String> getValuesFromColumn(Integer columnIndex, int top, int bottom) {
        return currSheet.getValuesFromColumn(columnIndex,top,bottom);
    }

}
