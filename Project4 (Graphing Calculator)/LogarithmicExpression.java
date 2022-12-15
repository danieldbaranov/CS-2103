public class LogarithmicExpression implements Expression {

    private Expression _expression;

    public LogarithmicExpression(Expression e) {
        _expression = e;
    }

    public LogarithmicExpression deepCopy() {
        return new LogarithmicExpression(_expression.deepCopy());
    }

    public String convertToString(int indentLevel) {
        String s = "";
        for (int i = 0; i < indentLevel; i++) {
            s += "\t";
        }
        s += "log()";
        s += "\n" + _expression.convertToString(indentLevel + 1) + (isEnd(_expression)? "\n" : "");
        return s;
    }

    private boolean isEnd(Expression e){
        return (e instanceof LiteralExpression) || (e instanceof VariableExpression);
    }

    public double evaluate(double x) {
        return Math.log(_expression.evaluate(x));
    }

    public Expression differentiate() {
        return new DivitiveExpression(_expression.differentiate(), _expression);
    }

}
