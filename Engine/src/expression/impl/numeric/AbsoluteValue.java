package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Number;
import expression.impl.UnaryExpression;

import java.io.Serializable;

public class AbsoluteValue  extends UnaryExpression implements Serializable {

    public AbsoluteValue(Expression expression1) {
        super(expression1);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {
        if(Number.CheckIsNan(evaluate)){
            return new Number(true);
        }
        Double res = Math.abs((Double)evaluate.getValue());
        return new Number(res);
    }

    @Override
    public String toString() {
        return "{ABS, " + getExpression().toString() + "}";
    }
}
