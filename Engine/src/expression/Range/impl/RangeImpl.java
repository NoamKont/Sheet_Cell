package expression.Range.impl;

import body.Cell;
import body.Coordinate;
import body.Sheet;
import body.impl.CoordinateImpl;
import expression.Range.api.Range;
import expression.api.EffectiveValue;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RangeImpl implements Range, Serializable{
    private String rangeName;
    final private Set<Cell> rangeCells = new HashSet<>();
    final private String topLeftCellId;
    final private String rightBottomCellId;
    final private Sheet sheet;

    public RangeImpl(String rangeName, String topLeftCellId, String rightBottomCellId, Sheet sheet) {
        this.rangeName = rangeName;
        this.sheet = sheet;
        if(isRangeValid(topLeftCellId.toUpperCase(), rightBottomCellId.toUpperCase())) {
            this.topLeftCellId = topLeftCellId.toUpperCase();
            this.rightBottomCellId = rightBottomCellId.toUpperCase();
            setRangeCells();
        }
        else{
            throw new IllegalArgumentException("Invalid Range of coordinates");
        }

    }
    @Override
    public String getRangeName() {
        return this.rangeName;
    }
    @Override
    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }
    @Override
    public Boolean isRangeValid(String topLeftCellId, String rightBottomCellId) {
        int sheetRowCount = sheet.getRowCount();
        int sheetColumnCount = sheet.getColumnCount();
        Coordinate leftCoordinate = new CoordinateImpl(topLeftCellId);
        Coordinate rightCoordinate = new CoordinateImpl(rightBottomCellId);
        if(leftCoordinate.getRow() > sheetRowCount || leftCoordinate.getColumn() > sheetColumnCount || rightCoordinate.getRow() > sheetRowCount || rightCoordinate.getColumn() > sheetColumnCount){
            return false;
        }
        return true;
    }

    @Override
    public Set<Cell> getCells() {
        return rangeCells;
    }

    private void setRangeCells() {
        String topLeftCellColumn = topLeftCellId.substring(0, 1);
        String topLeftCellRow = topLeftCellId.substring(1);
        String rightBottomCellColumn = rightBottomCellId.substring(0, 1);
        String rightBottomCellRow = rightBottomCellId.substring(1);

        if(topLeftCellColumn.equals(rightBottomCellColumn)) {
            for(int i = Integer.parseInt(topLeftCellRow); i <= Integer.parseInt(rightBottomCellRow); i++) {
                rangeCells.add(sheet.getCell(topLeftCellColumn + i));
            }
        }
        else if(topLeftCellRow.equals(rightBottomCellRow)) {
            for(int i = topLeftCellColumn.charAt(0); i <= rightBottomCellColumn.charAt(0); i++) {
                rangeCells.add(sheet.getCell((char)i + topLeftCellRow));
            }
        }
        else {
            for(int i = topLeftCellColumn.charAt(0); i <= rightBottomCellColumn.charAt(0); i++) {
                for(int j = Integer.parseInt(topLeftCellRow); j <= Integer.parseInt(rightBottomCellRow); j++) {
                    String cellId = (char) i + Integer.toString(j);
                    rangeCells.add(sheet.getCell(cellId));
                }
            }
        }

    }

    @Override
    public EffectiveValue evaluate() {
        return null;
    }

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String expressionTOtoString() {
        return rangeName;
    }
}
