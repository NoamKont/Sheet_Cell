package expression.impl;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.Serializable;

public class Bool implements Expression, EffectiveValue, Serializable{

    private Boolean value;
    boolean isunknown = false;

    public Bool(Boolean val, boolean unknown) {
        value = val;
        isunknown = unknown;
    }

//    public Bool(boolean unknown) {
//        this.isunknown = unknown;
//    }

    public Bool(String val) {
        this.value = Boolean.parseBoolean(val);
    }

    @Override
    public EffectiveValue evaluate() {
        return new Bool(value,isunknown);
    }

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String expressionTOtoString() { return Boolean.toString(value);}

    @Override
    public String toString() {
        if(isunknown)
            return "UNKNOWN";
        else{
            return Boolean.toString(value).toUpperCase();
        }
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
        return this.isunknown;
    }

    @Override
    public CellType getCellType() {
        return CellType.BOOLEAN;
    }

    @Override
    public Object getValue() {
        return value;
    }


    public static boolean CheckIsUnknown(EffectiveValue e1, EffectiveValue e2){
        return (e1.isUnknown() || e2.isUnknown() || e1.getCellType() != CellType.BOOLEAN ||  e2.getCellType() != CellType.BOOLEAN);
    }

    public static boolean CheckIsUnknown(EffectiveValue e1){
        return e1.isUnknown() || e1.getCellType() == CellType.EMPTY;
    }
}
