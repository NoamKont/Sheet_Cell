package expression.impl.numeric.Range;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Number;
import expression.impl.UnaryExpression;

import java.io.Serializable;

public class Sum extends UnaryExpression implements Serializable {

    public Sum(Expression expression1) {
        super(expression1);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {



        Double res = 1.1;
        return new Number(res);
    }

    @Override
    public String getOperationSign() {
        return "";
    }
}
