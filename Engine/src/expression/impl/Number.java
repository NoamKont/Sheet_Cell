package expression.impl;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.Serializable;

public class Number implements EffectiveValue, Serializable{

    private double num;
    private boolean isNan = false;

    public Number(double num) {
        this.num = num;
        isNan = false;
    }

    public Number(boolean isNan) {
        this.isNan = isNan;
    }

    public Number(String num) {
        this.num = Double.parseDouble(num);
    }

    @Override
    public EffectiveValue evaluate() {
        return new Number(num);
    }

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String expressionTOtoString() {
        return Integer.toString((int) num);
    }

    @Override
    public String toString() {
        if(isNan)
            return "NaN";
        if (num % 1 == 0) {
            return Integer.toString((int) num);
        }
        return String.format("%.2f", num);
    }

    @Override
    public boolean isNaN() {
        return isNan;
    }

    @Override
    public boolean isUndefined() {
//        throw new NumberFormatException("ERROR!, cant get a number to string function.");
        return false;
    }

    @Override
    public boolean isUnknown() {
        return false;
    }

    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    @Override
    public Object getValue() {
        return num;
    }


    public static boolean CheckIsNan(EffectiveValue e1, EffectiveValue e2){
        return (e1.isNaN() || e2.isNaN() || e1.getCellType() != CellType.NUMERIC ||  e2.getCellType() != CellType.NUMERIC);
    }

    public static boolean CheckIsNan(EffectiveValue e1){
        return e1.isNaN() || e1.getCellType() == CellType.EMPTY;
    }
}
