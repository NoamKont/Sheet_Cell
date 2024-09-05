package expression.impl.boolFunction;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Bool;

import java.io.Serializable;

public class And extends BinaryExpression implements Serializable {

    public And(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) throws NumberFormatException {
        if(Bool.CheckIsUnknown(evaluate, evaluate2)){
            return new Bool(null, true);
        }
        else{
            return new Bool((boolean)evaluate.getValue() && (boolean) evaluate2.getValue(), false);
        }
    }

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String toString() {
        return "{AND, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
    }

    @Override
    public String expressionTOtoString() {
        return "{AND, " + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
    }
}
