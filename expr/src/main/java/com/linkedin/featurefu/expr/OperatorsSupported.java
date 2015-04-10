/*
 * Copyright 2015 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software  
 * distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package com.linkedin.featurefu.expr;

import java.util.HashMap;
import java.util.Set;

/**
 * Singleton class to hold the <Name,Operator> map for supported operators
 * Cannot put this logic inside Operator class as its static data member may not be instantiated yet by the time.
 *
 *  Author: Leo Tang <http://www.linkedin.com/in/lijuntang>
 */
public class OperatorsSupported extends HashMap<String, Operator> {
  /**
   * A map for symbol -> Operator implementation object
   * For constant time constructing of an operator from its string representation
   */
  private static OperatorsSupported _supportedOps = new OperatorsSupported();

  private OperatorsSupported() {
  }

  public static void registerOperator(Operator operator) {
    _supportedOps.put(operator.getSymbol(), operator);
  }

  static {
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
    registerOperator(Operator.UNARY_MINUS);   // - or unaryMinus
    registerOperator(Operator.MOD);           // %
    registerOperator(Operator.IF);            // if
    registerOperator(Operator.EXP);           // exp
    registerOperator(Operator.SIGMOID);       // sigmoid
    registerOperator(Operator.TANH);            // tanh
    registerOperator(Operator.RAND);          // rand   return [0,1]
    registerOperator(Operator.RANDIN);        // rand-in a,b return [a,b]
    registerOperator(Operator.FLOOR);         // floor
    registerOperator(Operator.CEIL);          // ceil
    registerOperator(Operator.ROUND);         // round
    registerOperator(Operator.SQRT);          // sqrt
    registerOperator(Operator.IN);            // in
    registerOperator(Operator.COS);            // cos
    registerOperator(Operator.SIN);            // sin
    registerOperator(Operator.TAN);            // tan
  }

  public static Operator getOperator(String symbol) {
    return _supportedOps.get(symbol);
  }

  public static boolean isSupported(String symbol) {
    return _supportedOps.containsKey(symbol);
  }

  /**
   * For debugging purpose, let user know what are currently supported
   * @return list of operators supported in string
   */
  public static String getSupported() {
    Set<String> keys = _supportedOps.keySet();

    StringBuilder builder = new StringBuilder();

    for (String str : keys) {
      builder.append(str).append(" ");
    }

    return builder.toString();
  }
}
