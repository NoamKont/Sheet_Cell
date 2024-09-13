package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Number;

import java.io.Serializable;

public class Percent extends BinaryExpression implements Serializable {

    public Percent(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue part, EffectiveValue whole) throws NumberFormatException {
        if(Number.CheckIsNan(part, whole)){
            return new Number(true);
        }
        if((Double)whole.getValue() == 0){
            return new Number(true);
        }
        Double res = ((Double)part.getValue() * (Double)whole.getValue()) / 100;
        return new Number(res);
    }

    @Override
    public String toString() {
        return "{PERCENT, " + getExpression1().toString() + "," + getExpression2().toString() + "}";
    }

}
