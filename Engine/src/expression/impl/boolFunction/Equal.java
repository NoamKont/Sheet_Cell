package expression.impl.boolFunction;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Bool;

import java.io.Serializable;

public class Equal extends BinaryExpression implements Serializable {

    public Equal(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) throws NumberFormatException {
        if(evaluate.getCellType() != evaluate2.getCellType()){
            return new Bool(false, false);
        }
        if(evaluate.isUnknown() || evaluate2.isUnknown() || evaluate.isNaN() || evaluate2.isNaN()|| evaluate.isUndefined() || evaluate2.isUndefined()){
            return new Bool(null, true);
        }
        else{
            return new Bool(evaluate.getValue().equals(evaluate2.getValue()), false);
        }
    }

    @Override
    public String toString() {
        return "{EQUAL, " + getExpression1().toString() + "," + getExpression2().toString() + "}";
    }

}
