package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Number;

import java.io.Serializable;

public class Divide extends BinaryExpression implements Serializable {

    public Divide(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) throws NumberFormatException {
        if(Number.CheckIsNan(evaluate, evaluate2)){
            return new Number(true);
        }
        if((Double)evaluate2.getValue() == 0){
            return new Number(true);
        }
        Double res = (Double)evaluate.getValue() / (Double)evaluate2.getValue();
        return new Number(res);
    }

    @Override
    public String expressionTOtoString() {
        return "{DIVIDE, " + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
    }

    @Override
    public String toString() {
        return "{DIVIDE, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
    }
}
