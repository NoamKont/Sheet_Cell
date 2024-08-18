package expression.impl.string;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Str;

public class Concat extends BinaryExpression {


    public Concat(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) {
        String res = (String)evaluate.getValue() + (String)evaluate2.getValue();
        return new Str(res);
    }

    @Override
    public String getOperationSign() {
        return "";
    }
}
