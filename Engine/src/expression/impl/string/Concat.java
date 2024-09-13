package expression.impl.string;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Str;

import java.io.Serializable;

public class Concat extends BinaryExpression implements Serializable {


    public Concat(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) {
        if(Str.CheckIsUndifined(evaluate, evaluate2)){
            return new Str(true);
        }
        String res = (String)evaluate.getValue() + (String)evaluate2.getValue();
        return new Str(res);
    }

    @Override
    public String expressionTOtoString() {
        return "{CONCAT," + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
    }

    @Override
    public String toString() {
        return "{CONCAT," + getExpression1().toString() + "," + getExpression2().toString() + "}";
    }

}
