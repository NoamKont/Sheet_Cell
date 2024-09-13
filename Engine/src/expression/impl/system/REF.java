package expression.impl.system;

import body.Cell;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Reference;
import expression.impl.UnaryExpression;

import java.io.Serializable;

public class REF extends UnaryExpression implements Serializable {
    private Cell cell;
    public REF(Expression expression1,Cell cell) {
        super(expression1);
        this.cell = cell;
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {
        return new Reference(cell);
    }

    @Override
    public String toString() {
        return "{REF," + cell.getId() + "}";
    }
}
