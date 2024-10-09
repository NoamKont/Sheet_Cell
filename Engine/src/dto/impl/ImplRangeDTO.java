package dto.impl;

import body.Cell;
import body.Coordinate;
import dto.RangeDTO;
import expression.Range.api.Range;

import java.util.HashSet;
import java.util.Set;

public class ImplRangeDTO implements RangeDTO {
    private String rangeName;
    private Set<CellDTO> rangeCells = new HashSet<>();
    final private String topLeftCellId;
    final private String rightBottomCellId;


    public ImplRangeDTO(Range range) {
        this.rangeName = range.getRangeName();
        this.topLeftCellId = range.getTopLeftCellId();
        this.rightBottomCellId = range.getRightBottomCellId();
        for(Cell cell : range.getRangeCells()){
            rangeCells.add(new CellDTO(cell));
        }

    }

    @Override
    public Boolean isRangeValid(String topLeftCellId, String rightBottomCellId) {
        return null;
    }

    @Override
    public Set<Coordinate> getCellCoordinates() {
        Set<Coordinate> res = new HashSet<>();
        for (CellDTO c : rangeCells) {
            res.add(c.getCoordinate());
        }
        return res;
    }

    @Override
    public Set<CellDTO> getRangeCells() {
        return this.rangeCells;
    }

    @Override
    public String getRangeName() {
        return this.rangeName;
    }

    @Override
    public String getTopLeftCellId() {
        return this.topLeftCellId;
    }

    @Override
    public String getRightBottomCellId() {
        return this.rightBottomCellId;
    }
}
