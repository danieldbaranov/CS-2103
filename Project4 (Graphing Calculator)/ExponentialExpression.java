public class ExponentialExpression implements Expression {

    private Expression _baseExpression;
    private Expression _exponentExpression;

    public ExponentialExpression(Expression l, Expression r) {
        _baseExpression = l;
        _exponentExpression = r;
    }

    public ExponentialExpression deepCopy() {
        return new ExponentialExpression(_baseExpression.deepCopy(), _exponentExpression.deepCopy());
    }

    public String convertToString(int indentLevel) {
        String s = "";
        for (int i = 0; i < indentLevel; i++) {
            s += "\t";
        }
        s += "^";
        s += "\n" + _baseExpression.convertToString(indentLevel + 1) +
                "\n" + _exponentExpression.convertToString(indentLevel + 1) + (isEnd(_exponentExpression)? "" : "\n");
        return s;
    }

    private boolean isEnd(Expression e){
        return (e instanceof LiteralExpression) || (e instanceof VariableExpression);
    }

    public double evaluate(double x) {
        return Math.pow(_baseExpression.evaluate(x), _exponentExpression.evaluate(x));
    }

    public Expression differentiate() {
        if (_exponentExpression instanceof LiteralExpression) {
            return new MultiplicativeExpression(_exponentExpression, new MultiplicativeExpression(
                    new ExponentialExpression(_baseExpression, new SubtractiveExpression(_exponentExpression, new LiteralExpression(1))),
                    _baseExpression.differentiate()
            ));
        } else if (_baseExpression instanceof LiteralExpression){
            return new MultiplicativeExpression(new LogarithmicExpression(_baseExpression),
                    new MultiplicativeExpression(new ExponentialExpression(_baseExpression, _exponentExpression),
                            _exponentExpression.differentiate()));
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
