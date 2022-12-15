public class AdditiveExpression implements Expression {

    private Expression _leftExpression;
    private Expression _rightExpression;

    public AdditiveExpression(Expression l, Expression r) {
        _leftExpression = l;
        _rightExpression = r;
    }

    public AdditiveExpression deepCopy() {
        return new AdditiveExpression(_leftExpression.deepCopy(), _rightExpression.deepCopy());
    }

    public String convertToString(int indentLevel) {
        String s = "";
        for (int i = 0; i < indentLevel; i++) {
            s += "\t";
        }
        s += "+";
        s += "\n" + _leftExpression.convertToString(indentLevel + 1) +
                "\n" + _rightExpression.convertToString(indentLevel + 1) + (isEnd(_rightExpression)? "\n" : "");
        return s;
    }

    private boolean isEnd(Expression e){
        return (e instanceof LiteralExpression) || (e instanceof VariableExpression);
    }

    public double evaluate(double x) {
        return _leftExpression.evaluate(x) + _rightExpression.evaluate(x);
    }

    public Expression differentiate() {
        return new AdditiveExpression(_leftExpression.differentiate(), _rightExpression.differentiate());
    }

}
