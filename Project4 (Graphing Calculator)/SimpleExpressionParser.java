import java.util.function.*;

public class SimpleExpressionParser implements ExpressionParser {
    /*
     * Attempts to create an expression tree from the specified String.
     * Throws a ExpressionParseException if the specified string cannot be parsed.
     * Grammar:
     * S -> A | P
     * A -> A+M | A-M | M
     * M -> M*E | M/E | E
     * E -> P^E | P | log(P)
     * P -> (S) | L | V
     * L -> <float>
     * V -> x
     * @param str the string to parse into an expression tree
     * @return the Expression object representing the parsed expression tree
     */
    public Expression parse(String str) throws ExpressionParseException {
        str = str.replaceAll(" ", "");
        Expression expression = null;
        try {
            expression = parseAdditiveExpression(str);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(e);
        }

        if (expression == null) {
            throw new ExpressionParseException("Cannot parse expression: " + str);
        }

        return expression;
    }

    protected Expression parseAdditiveExpression(String str) {
        Expression expression;

        //S -> A
        for(int i = 0; i < str.length(); i++){
            //A -> A+M
            if(str.charAt(i) == '+' &&
                    parseAdditiveExpression(str.substring(0, i)) != null &&
                    parseMultiplicativeExpression(str.substring(i + 1)) != null){
                return new AdditiveExpression(parseAdditiveExpression(str.substring(0, i)),
                        parseMultiplicativeExpression(str.substring(i + 1)));
            }

            // separate condition for if the parser encounters a negative number
            if(i == 0 && str.charAt(i) == '-'){
                return new MultiplicativeExpression(new LiteralExpression(-1), parseAdditiveExpression(str.substring(1)));
            }

            //A -> A-M
            if(str.charAt(i) == '-' &&
                    parseAdditiveExpression(str.substring(0, i)) != null &&
                    parseMultiplicativeExpression(str.substring(i + 1)) != null){
                return new SubtractiveExpression(parseAdditiveExpression(str.substring(0, i)),
                        parseMultiplicativeExpression(str.substring(i + 1)));
            }
        }

        //A -> M
        if(parseMultiplicativeExpression(str) != null){
            return parseMultiplicativeExpression(str);
        }

        //S -> P
        if(str.substring(0,1).equals("(")&&
                str.substring(str.length()-1).equals(")")){
            return new ParenthesesExpression(parseAdditiveExpression(str.substring(1,str.length() -1)));
        }

        return null;
    }

    protected Expression parseMultiplicativeExpression(String str) {
        for(int i = 0; i < str.length(); i++){
            //M -> M*E
            if(str.charAt(i) == '*' &&
                    parseMultiplicativeExpression(str.substring(0, i)) != null &&
                    parseExponentialExpression(str.substring(i + 1)) != null){
                return new MultiplicativeExpression(parseMultiplicativeExpression(str.substring(0, i)),
                        parseExponentialExpression(str.substring(i + 1)));
            }

            //M -> M/E
            if(str.charAt(i) == '/' &&
                    parseMultiplicativeExpression(str.substring(0, i)) != null &&
                    parseExponentialExpression(str.substring(i + 1)) != null){
                return new DivitiveExpression(parseMultiplicativeExpression(str.substring(0, i)),
                        parseExponentialExpression(str.substring(i + 1)));
            }
        }

        //M -> E
        if(parseExponentialExpression(str) != null){
            return parseExponentialExpression(str);
        }

        return null;
    }

    protected Expression parseExponentialExpression(String str){
        //E -> P^E
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == '^' &&
                    parseParenthesesExpression(str.substring(0, i)) != null &&
                    parseExponentialExpression(str.substring(i + 1)) != null){
                return new ExponentialExpression(parseParenthesesExpression(str.substring(0, i)),
                        parseExponentialExpression(str.substring(i + 1)));
            }
        }

        //E -> log P
        if(str.length() > 5 && str.substring(0, 3).equals("log") &&
                parseParenthesesExpression(str.substring(3)) != null){
            return new LogarithmicExpression(parseParenthesesExpression(str.substring(3)));
        }

        //E -> P
        if(parseParenthesesExpression(str) != null){
            return parseParenthesesExpression(str);
        }

        return null;
    }

    protected Expression parseParenthesesExpression(String str){

        //P -> (S)
        if(str.substring(0,1).equals("(")&&
        str.substring(str.length()-1).equals(")")){
            return new ParenthesesExpression(parseAdditiveExpression(str.substring(1,str.length() -1)));
        }

        //P -> V
        if(parseVariableExpression(str) != null){
            return parseVariableExpression(str);
        }

        //P -> L
        if(parseLiteralExpression(str) != null){
            return parseLiteralExpression(str);
        }
        return null;
    }

    protected VariableExpression parseVariableExpression(String str) {
        if (str.equals("x")) {
            return new VariableExpression();
        }
        return null;
    }

    protected LiteralExpression parseLiteralExpression(String str) {
        // From https://stackoverflow.com/questions/3543729/how-to-check-that-a-string-is-parseable-to-a-double/22936891:
        final String Digits = "(\\p{Digit}+)";
        final String HexDigits = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally
        // signed decimal integer.
        final String Exp = "[eE][+-]?" + Digits;
        final String fpRegex =
                ("[\\x00-\\x20]*" + // Optional leading "whitespace"
                        "[+-]?(" +         // Optional sign character
                        "NaN|" +           // "NaN" string
                        "Infinity|" +      // "Infinity" string

                        // A decimal floating-point string representing a finite positive
                        // number without a leading sign has at most five basic pieces:
                        // Digits . Digits ExponentPart FloatTypeSuffix
                        //
                        // Since this method allows integer-only strings as input
                        // in addition to strings of floating-point literals, the
                        // two sub-patterns below are simplifications of the grammar
                        // productions from the Java Language Specification, 2nd
                        // edition, section 3.10.2.

                        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                        "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

                        // . Digits ExponentPart_opt FloatTypeSuffix_opt
                        "(\\.(" + Digits + ")(" + Exp + ")?)|" +

                        // Hexadecimal strings
                        "((" +
                        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "(\\.)?)|" +

                        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                        ")[pP][+-]?" + Digits + "))" +
                        "[fFdD]?))" +
                        "[\\x00-\\x20]*");// Optional trailing "whitespace"

        if (str.matches(fpRegex)) {
            return new LiteralExpression(Double.parseDouble(str));
        }
        return null;
    }

    public static void main(String[] args) throws ExpressionParseException {
        final ExpressionParser parser = new SimpleExpressionParser();
        System.out.println(parser.parse("10*2+12-4.").convertToString(0));
    }
}
