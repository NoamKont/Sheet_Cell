package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Number;

import java.io.Serializable;

public class Pow extends BinaryExpression implements Serializable {

    public Pow(Expression expression1, Expression expression2)
    {
        super(expression1, expression2);
    }

    @Override
    public String expressionTOtoString() {
        return "{POW," + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue e1, EffectiveValue e2) {
        if (Number.CheckIsNan(e1, e2)) {
            return new Number(true);
        }
        Double res = Math.pow((Double) e1.getValue(), (Double) e2.getValue());
        return new Number(res);
    }

    @Override
    public String toString() {
        return "{POW, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
    }


}
