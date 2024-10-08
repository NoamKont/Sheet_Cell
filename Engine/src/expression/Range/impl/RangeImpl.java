package expression.Range.impl;

import body.Cell;
import body.Coordinate;
import body.Sheet;
import body.impl.CoordinateImpl;
import expression.CellType;
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
    private Sheet sheet;

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
    public String getTopLeftCellId() {
        return topLeftCellId;
    }

    @Override
    public String getRightBottomCellId() {
        return rightBottomCellId;
    }

    @Override
    public Set<Cell> getRangeCells() {
        return rangeCells;
    }

    @Override
    public Sheet getSheet() {
        return sheet;
    }

    @Override
    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    @Override
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Boolean isRangeValid(String topLeftCellId, String rightBottomCellId) {
        int sheetRowCount = sheet.getRowCount();
        int sheetColumnCount = sheet.getColumnCount();

        sheet.checkValidBounds(new CoordinateImpl(topLeftCellId));
        sheet.checkValidBounds(new CoordinateImpl(rightBottomCellId));

        Coordinate leftCoordinate = new CoordinateImpl(topLeftCellId);
        Coordinate rightCoordinate = new CoordinateImpl(rightBottomCellId);
        checkTopLeftIsBeforeRightBottom(leftCoordinate, rightCoordinate);

        if(leftCoordinate.getRow() > sheetRowCount || leftCoordinate.getColumn() > sheetColumnCount || rightCoordinate.getRow() > sheetRowCount || rightCoordinate.getColumn() > sheetColumnCount){
            return false;
        }
        return true;
    }

    private void checkTopLeftIsBeforeRightBottom(Coordinate topLeftCellId, Coordinate rightBottomCellId) {
        if(topLeftCellId.getRow() > rightBottomCellId.getRow() || topLeftCellId.getColumn() > rightBottomCellId.getColumn()){
            throw new IllegalArgumentException("Top left cell is not before right bottom cell");
        }
    }

    private void checkValidCoordinate(String cellId) {
        if(cellId.length() >= 2 ){
            char column = cellId.charAt(0);
            if(column < 'A' || column > 'Z'){
                throw new IllegalArgumentException("Invalid cell id");
            }
        }
        else{
            throw new IllegalArgumentException("Invalid cell id");
        }

    }

    @Override
    public Set<Cell> getCells() {
        return rangeCells;
    }

    @Override
    public Set<Coordinate> getCellCoordinates() {
        Set<Coordinate> res = new HashSet<>();
        for (Cell c : rangeCells) {
            res.add(c.getCoordinate());
        }
        return res;
    }

    public void setRangeCells() {
        String topLeftCellColumn = topLeftCellId.substring(0, 1);
        String topLeftCellRow = topLeftCellId.substring(1);
        String rightBottomCellColumn = rightBottomCellId.substring(0, 1);
        String rightBottomCellRow = rightBottomCellId.substring(1);

        if(topLeftCellColumn.equals(rightBottomCellColumn)) {
            for(int i = Integer.parseInt(topLeftCellRow); i <= Integer.parseInt(rightBottomCellRow); i++) {
                Cell c = sheet.getCell(topLeftCellColumn + i);
                rangeCells.add(c);
                //rangeCells.add(sheet.getCell(topLeftCellColumn + i));
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
        return this;
    }

    @Override
    public String toString() {
        return rangeName;
    }

    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean isNaN() {
        return false;
    }

    @Override
    public boolean isUndefined() {
        return false;
    }

    @Override
    public boolean isUnknown() {
        return false;
    }
}
