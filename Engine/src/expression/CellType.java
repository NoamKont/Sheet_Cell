package expression;

import expression.impl.Empty;

public enum CellType {
    NUMERIC(Double.class) ,
    STRING(String.class) ,
    BOOLEAN(Boolean.class) ,
    EMPTY(Empty .class) ;

    private Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

}
