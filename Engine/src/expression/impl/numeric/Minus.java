package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Number;

import java.io.Serializable;

public class Minus extends BinaryExpression implements Serializable {

    public Minus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue e1, EffectiveValue e2)throws NumberFormatException{
        if(Number.CheckIsNan(e1, e2)){
            return new Number(true);
        }
        Double res = (Double)e1.getValue() - (Double)e2.getValue();
        return new Number(res);
    }

    @Override
    public String toString() {
        return "{MINUS, " + getExpression1().toString() + "," + getExpression2().toString() + "}";
    }


}
