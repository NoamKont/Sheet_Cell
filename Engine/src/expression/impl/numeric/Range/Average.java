package expression.impl.numeric.Range;

import expression.CellType;
import expression.Range.api.Range;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Number;
import expression.impl.UnaryExpression;

import java.io.Serializable;

public class Average extends UnaryExpression implements Serializable {

    public Average(Expression expression1) {
        super(expression1);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {
        if(evaluate == null){
            throw new NumberFormatException("ERROR!, No such Range founded.");
        }
        else if (evaluate instanceof Range) {
            long count = 0;
            double res = 0.0;
            Range range = (Range) evaluate;
            count = range.getCells().stream()
                    .filter(cell -> cell.getEffectiveValue().getCellType() == CellType.NUMERIC)
                    .count();
            if(count == 0){
                throw new NumberFormatException("ERROR!, No numeric values in the Range.");
            }
            else {
                res = range.getCells().stream()
                        .filter(cell -> cell.getEffectiveValue().getCellType() == CellType.NUMERIC)
                        .mapToDouble(cell -> (Double) cell.getEffectiveValue().getValue())
                        .sum();// Sum numeric values

                return new Number(res/count);
            }

        }
        else{
            throw new NumberFormatException("ERROR!, AVERAGE function must get valid Range.");
        }
    }

    @Override
    public String toString() {
        return "{AVERAGE, " + getExpression().toString() + "}";
    }
}
