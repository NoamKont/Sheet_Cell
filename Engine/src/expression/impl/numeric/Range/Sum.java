package expression.impl.numeric.Range;

import expression.CellType;
import expression.Range.api.Range;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Number;
import expression.impl.UnaryExpression;

import java.io.Serializable;

public class Sum extends UnaryExpression implements Serializable {

    public Sum(Expression expression1) {
        super(expression1);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {
        if(evaluate == null){
            throw new NumberFormatException("ERROR!, No such Range founded.");
        }
        else if (evaluate instanceof Range) {
            double res = 0.0;
            Range range = (Range) evaluate;
            res = range.getCells().stream()
                    .filter(cell -> cell.getEffectiveValue().getCellType() == CellType.NUMERIC)
                    .mapToDouble(cell -> (Double) cell.getEffectiveValue().getValue())
                    .sum();// Sum numeric values

            return new Number(res);
        }
        else{
            throw new NumberFormatException("ERROR!, SUM function must get valid Range.");
        }
    }

    @Override
    public String expressionTOtoString() {
        return "{SUM, " + getExpression().expressionTOtoString() + "}";
    }

    @Override
    public String toString() {
        return "{SUM, " + getExpression().toString() + "}";
    }
}
