package expression.impl.string;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Number;
import expression.impl.Str;
import expression.impl.TrinaryExpression;

import java.io.Serializable;

public class Sub  extends TrinaryExpression implements Serializable {

    public Sub(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2, EffectiveValue evaluate3) throws NumberFormatException {
        if(Str.CheckIsUndifined(evaluate) || Number.CheckIsNan(evaluate2, evaluate3))
            return new Str(true);
        double e1 = (Double) evaluate2.getValue();
        double e2 = (Double) evaluate3.getValue() + 1;
        if (e1 % 1 != 0 || e2 % 1 != 0)
            throw new NumberFormatException("Index must be an integer");
        boolean error = false;
        if (e1 < 0 || e1 > e2 || e2 > ((String) evaluate.getValue()).length()) {
           return new Str(true);
        }
        String res = ((String) evaluate.getValue()).substring((int) e1, (int) e2);
        //String res = ((String)((String) evaluate.getValue()).substring((Integer)((Double) evaluate2.getValue()).intValue(),(Integer)((Double) evaluate3.getValue()).intValue()));
        return new Str(res);
    }

    @Override
    public String toString() {
        return "{SUB, " + getExpression1().toString() + ", " + getExpression2().toString() + ", " + getExpression3().toString() + "}";
    }
}
