package expression.impl;

import expression.CellType;
import expression.api.EffectiveValue;

import java.io.Serializable;

public class Empty implements EffectiveValue, Serializable {

    @Override
    public CellType getCellType() {
        return CellType.EMPTY;
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

    @Override
    public EffectiveValue evaluate() {
        return new Empty();
    }

    @Override
    public String toString(){
        return "";
    }
}
