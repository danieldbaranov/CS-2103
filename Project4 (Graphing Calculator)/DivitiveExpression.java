public class DivitiveExpression implements Expression {

    private Expression _leftExpression;
    private Expression _rightExpression;

    public DivitiveExpression(Expression l, Expression r) {
        _leftExpression = l;
        _rightExpression = r;
    }

    public DivitiveExpression deepCopy() {
        return new DivitiveExpression(_leftExpression.deepCopy(), _rightExpression.deepCopy());
    }

    public String convertToString(int indentLevel) {
        String s = "";
        for (int i = 0; i < indentLevel; i++) {
            s += "\t";
        }
        s += "/";
        s += "\n" + _leftExpression.convertToString(indentLevel + 1) +
                "\n" + _rightExpression.convertToString(indentLevel + 1) + (isEnd(_rightExpression)? "\n" : "");
        return s;
    }

    private boolean isEnd(Expression e){
        return (e instanceof LiteralExpression) || (e instanceof VariableExpression);
    }

    public double evaluate(double x) {
        return _leftExpression.evaluate(x) / _rightExpression.evaluate(x);
    }

    public Expression differentiate() {
        return new SubtractiveExpression(new DivitiveExpression(_leftExpression.differentiate(), _rightExpression),
                new DivitiveExpression(new MultiplicativeExpression(_leftExpression, _rightExpression.differentiate()),
                        new ExponentialExpression(_rightExpression, new LiteralExpression(2))));
    }

}
