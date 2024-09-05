package expression.api;
import expression.CellType;
import javafx.beans.property.StringProperty;

public interface EffectiveValue {
    CellType getCellType();
    Object getValue();
    String toString();
    boolean isNaN();
    boolean isUndefined();
    boolean isUnknown();
}
