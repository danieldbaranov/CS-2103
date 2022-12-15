public class ParenthesesExpression implements Expression {

    private Expression _expression;

    public ParenthesesExpression(Expression e) {
        _expression = e;
    }

    public ParenthesesExpression deepCopy() {
        return new ParenthesesExpression(_expression.deepCopy());
    }

    public String convertToString(int indentLevel) {
        String s = "";
        for (int i = 0; i < indentLevel; i++) {
            s += "\t";
        }
        s += "()";
        s += "\n" + _expression.convertToString(indentLevel + 1);
        return s;
    }

    private boolean isEnd(Expression e){
        return (e instanceof LiteralExpression) || (e instanceof VariableExpression);
    }

    public double evaluate(double x) {
        return _expression.evaluate(x);
    }

    public Expression differentiate() {
        return null;
    }

}
