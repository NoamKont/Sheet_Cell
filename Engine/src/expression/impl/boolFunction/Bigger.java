package expression.impl.boolFunction;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Bool;
import expression.impl.Number;

import java.io.Serializable;

public class Bigger extends BinaryExpression implements Serializable {

    public Bigger(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) throws NumberFormatException {

        if(evaluate.isUnknown() || evaluate2.isUnknown() || Number.CheckIsNan(evaluate, evaluate2)){
            return new Bool(null, true);
        }
        else{
            return new Bool((double)evaluate.getValue() >= (double) evaluate2.getValue(), false);
        }

    }

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String toString() {
        return "{BIGGER, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
    }

    @Override
    public String expressionTOtoString() {
        return "{BIGGER," + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
    }
}
