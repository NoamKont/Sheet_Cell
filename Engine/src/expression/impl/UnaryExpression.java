package expression.impl;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.Serializable;

public abstract class UnaryExpression implements Expression, Serializable {

    private Expression expression1;

    public UnaryExpression(Expression expression1) {
        this.expression1 = expression1;
    }

    @Override
    public EffectiveValue evaluate() {
        return evaluate(expression1.evaluate());
    }


    @Override
    public String toString() {
        return "{" + expression1.toString() + "}";
    }

    abstract protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException;

    public Expression getExpression() {
        return expression1;
    }

}

