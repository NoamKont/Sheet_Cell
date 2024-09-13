package expression.impl.boolFunction;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Bool;

import java.io.Serializable;

public class Or extends BinaryExpression implements Serializable {

    public Or(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) throws NumberFormatException {
        if(Bool.CheckIsUnknown(evaluate, evaluate2)){
            return new Bool(null, true);
        }
        else{
            return new Bool((boolean)evaluate.getValue() || (boolean) evaluate2.getValue(), false);
        }
    }

    @Override
    public String toString() {
        return "{OR, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
    }

}
