public class LiteralExpression implements Expression {

    private double _number;

    public LiteralExpression(double num) {
        _number = num;
    }

    public LiteralExpression deepCopy() {
        return new LiteralExpression(_number);
    }

    public String convertToString(int indentLevel) {
        String s = "";
        for (int i = 0; i < indentLevel; i++) {
            s += "\t";
        }
        s += _number;
        return s;
    }


    public double evaluate(double x) {
        return _number;
    }

    public Expression differentiate() {
        return new LiteralExpression(0);
    }

}
