package expression.impl;

import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.Serializable;

public abstract class BinaryExpression implements Expression, Serializable {

    private Expression expression1;
    private Expression expression2;

    public BinaryExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public EffectiveValue evaluate() {
        return evaluate(expression1.evaluate(), expression2.evaluate());
    }

    @Override
    public String toString() {return "(" + expression1 + getOperationSign() + expression2 + ")";}

    abstract protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) throws NumberFormatException;

}
