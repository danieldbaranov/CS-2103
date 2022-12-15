public class VariableExpression implements Expression {


    public VariableExpression() {

    }

    public VariableExpression deepCopy() {
        return new VariableExpression();
    }

    public String convertToString(int indentLevel) {
        String s = "";
        for (int i = 0; i < indentLevel; i++) {
            s += "\t";
        }
        s += "x";
        return s;
    }


    public double evaluate(double x) {
        return x;
    }

    public Expression differentiate() {
        return new LiteralExpression(1);
    }

}
