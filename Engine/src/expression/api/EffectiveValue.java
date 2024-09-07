package expression.api;
import expression.CellType;


public interface EffectiveValue extends Expression {
    CellType getCellType();
    Object getValue();
    String toString();
    boolean isNaN();
    boolean isUndefined();
    boolean isUnknown();
}
