package expression.impl.boolFunction;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Bool;
import expression.impl.UnaryExpression;

import java.io.Serializable;

public class Not extends UnaryExpression implements Serializable {

    public Not(Expression expression1) {
        super(expression1);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {
        if(Bool.CheckIsUnknown(evaluate)){
            return new Bool(null, true);
        }
        else{
            return new Bool(!(boolean)evaluate.getValue(), false);
        }
    }

    @Override
    public String toString() {
        return "{NOT, " + getExpression().toString() + "}";
    }

    @Override
    public String expressionTOtoString() {
        return "{NOT, " + getExpression().expressionTOtoString() + "}";
    }
}
