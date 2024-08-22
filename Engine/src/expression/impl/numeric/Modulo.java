package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Number;

import java.io.Serializable;

public class Modulo  extends BinaryExpression implements Serializable {

    public Modulo(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate1) throws NumberFormatException {
        if(Number.CheckIsNun(evaluate, evaluate1)){
            return new Number(true);
        }
        Double res = (Double)evaluate.getValue() % (Double)evaluate1.getValue();
        return new Number(res);
    }

    @Override
    public String getOperationSign() {return "mod";}
}
