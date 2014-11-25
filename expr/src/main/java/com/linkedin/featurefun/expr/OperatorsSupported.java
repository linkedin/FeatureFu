package com.linkedin.featurefun.expr;

import java.util.HashMap;
import java.util.Set;

/**
 * Singleton class to hold the <Name,Operator> map for supported operators
 * Cannot put this logic inside Operator class as its static data member may not be instantiated yet by the time.
 *
 * Author: Leo Tang <litang@linkedin.com>
 */
public class OperatorsSupported extends HashMap<String, Operator>{
    /**
     * A map for symbol -> Operator implementation object
     * For constant time constructing of an operator from its string representation
     */
    private static OperatorsSupported supportedOps = new OperatorsSupported();
    private OperatorsSupported(){ }

    public static void registerOperator(Operator operator) {
        supportedOps.put(operator.getSymbol(), operator);
    }

    static
    {
        registerOperator(Operator.EQ);            // ==
        registerOperator(Operator.NE);            // !=
        registerOperator(Operator.GT);            // >
        registerOperator(Operator.GT_EQ);         // >=
        registerOperator(Operator.LT);            // <
        registerOperator(Operator.LT_EQ);         // <=
        registerOperator(Operator.OR);            // ||
        registerOperator(Operator.AND);           // &&
        registerOperator(Operator.NOT);           // !
        registerOperator(Operator.SIGN);          // sign
        registerOperator(Operator.ADD);           // +
        registerOperator(Operator.SUBTRACT);      // -
        registerOperator(Operator.MULTIPLY);      // *
        registerOperator(Operator.DIVIDE);        // /
        registerOperator(Operator.POWER);         // **
        registerOperator(Operator.LN);            // ln
        registerOperator(Operator.LN1PLUS);       // ln1plus
        registerOperator(Operator.LOG2);          // log2
        registerOperator(Operator.MAX);           // max
        registerOperator(Operator.MIN);           // min
        registerOperator(Operator.ABS);           // abs
        registerOperator(Operator.UnaryMinus);    // - or unaryMinus
        registerOperator(Operator.MOD);           // %
        registerOperator(Operator.IF);            // if
        registerOperator(Operator.EXP);           // exp
        registerOperator(Operator.SIGMOID);       // sigmoid
        registerOperator(Operator.RAND);          // rand   return [0,1]
        registerOperator(Operator.RANDIN);       // rand-in a,b return [a,b]
        registerOperator(Operator.FLOOR);         // floor
        registerOperator(Operator.CEIL);          // ceil
        registerOperator(Operator.ROUND);         // round
        registerOperator(Operator.SQRT);          // sqrt
        registerOperator(Operator.IN);            // in
    }

    public static Operator getOperator(String symbol){
        return supportedOps.get(symbol);
    }

    public static boolean isSupported(String symbol){
        return supportedOps.containsKey(symbol);
    }

    /**
     * For debugging purpose, let user know what are currently supported
     * @return list of operators supported in string
     */
    public static String getSupported(){
      Set<String> keys = supportedOps.keySet();

        StringBuilder builder = new StringBuilder();

        for (String str : keys) {
            builder.append(str).append(" ");
        }

        return builder.toString();
    }
}
