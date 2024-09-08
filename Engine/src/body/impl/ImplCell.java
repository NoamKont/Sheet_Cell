package body.impl;

import body.Cell;
import body.Coordinate;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Empty;
import expression.impl.Number;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImplCell implements Cell,Serializable  {

    private Coordinate coor;
    private String Id;
    private int lastVersionUpdate;
    private String originalValue;
    private Expression expression;
    private EffectiveValue effectiveValue;
    private List<Coordinate> cellsDependsOnThem = new ArrayList<>();
    private List<Coordinate> cellsDependsOnHim = new ArrayList<>();

    public ImplCell(String id) {
        Id = id;
        coor = new CoordinateImpl(id);
        lastVersionUpdate = 1;
        originalValue = "";
        expression = new Empty();
        effectiveValue = new Empty();
    }
//
//    public ImplCell(double num){
//        Number number = new Number(num);
//        effectiveValue = number;
//    }

    @Override
    public String getId() {
        return Id;
    }

    @Override
    public void setId(String id) {
        Id = id;
    }

    @Override
    public int getLastVersionUpdate() {
        return lastVersionUpdate;
    }

    @Override
    public void setLastVersionUpdate(int version) {
        lastVersionUpdate = version;
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setOriginalValue(String original) {
        originalValue = original;
    }

    @Override
    public void setEffectiveValue(EffectiveValue value) {
        this.effectiveValue = value;
    }

    @Override
    public void setExpression(Expression expression) {
        this.expression = expression;
    }
    @Override
    public EffectiveValue getEffectiveValue()throws NumberFormatException {return effectiveValue;}

    @Override
    public void setDependsOnThem(List<Coordinate> dependsOnThem) {
        this.cellsDependsOnThem = dependsOnThem;
//        cellsDependsOnThem.clear();
//        cellsDependsOnThem.addAll(dependsOnThem);
    }

    @Override
    public void setDependsOnHim(List<Coordinate> dependsOnHim) {
        this.cellsDependsOnHim = dependsOnHim;
//        cellsDependsOnHim.clear();
//        cellsDependsOnHim.addAll(dependsOnHim);
    }

    @Override
    public List<Coordinate> getCellsDependsOnThem() {
        return cellsDependsOnThem;
    }
    @Override
    public List<Coordinate> getCellsDependsOnHim() {
        return cellsDependsOnHim;
    }

    @Override
    public Coordinate getCoordinate() {
        return coor;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }


}

