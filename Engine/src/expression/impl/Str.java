package expression.impl;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.Serializable;

public class Str implements Expression, EffectiveValue,Serializable {

    private String string;
    private boolean isUndifined = false;

    public Str(String s) {
        this.string = s;
    }

    public Str(boolean isUndifined) {
        this.isUndifined = isUndifined;
    }

    @Override
    public EffectiveValue evaluate() {return new Str(string);}

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String expressionTOtoString() {
        return string;
    }

    @Override
    public String toString() {
        if (isUndifined) return "!UNDEFINED!";
        return string;

    }

    @Override
    public boolean isNaN() {
        return false;
        // throw new NumberFormatException("ERROR!, cant get a string to numeric function.");
    }

    @Override
    public boolean isUndefined() {
        return isUndifined;
    }

    @Override
    public CellType getCellType() {
        return CellType.STRING;
    }

    @Override
    public Object getValue() {
        return string;
    }

    public static boolean CheckIsUndifined(EffectiveValue e1){
        return e1.isUndefined() || e1.getCellType() == CellType.EMPTY;
    }

    public static boolean CheckIsUndifined(EffectiveValue e1, EffectiveValue e2){
        return e1.isUndefined() || e1.getCellType() != CellType.STRING || e2.isUndefined() || e2.getCellType() != CellType.STRING;
    }



}