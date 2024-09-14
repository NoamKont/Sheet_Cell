package expression.impl.boolFunction;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.TrinaryExpression;

import java.io.Serializable;


public class If extends TrinaryExpression implements Serializable {

    public If(Expression expression1, Expression expression2, Expression expression3) {
            super(expression1, expression2, expression3);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2, EffectiveValue evaluate3) throws NumberFormatException {
        if(!evaluate.getCellType().equals(CellType.BOOLEAN)){
            throw new NumberFormatException("The first expression must return boolean value");
        }
        if(evaluate2.getCellType().equals(evaluate3.getCellType())){
            if((Boolean) evaluate.getValue()){
                return evaluate2;
            }
            else{
                return evaluate3;
            }
        }
        else{
            throw new NumberFormatException("The cell types of the two expressions are not the same");
        }
    }

    @Override
    public String toString() {
        return "{IF, " + getExpression1().toString() + "," + getExpression2().toString() + "," + getExpression3().toString() + "}";
    }

}
