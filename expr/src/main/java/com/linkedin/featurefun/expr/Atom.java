package com.linkedin.featurefun.expr;

/**
 * inheritance hierarchy:  Expr -> Atom  -> Constant
 *                            \      \__->  Variable
 *                              \_->Expression
 *
 * Atom has a value, possibly a name (if it's variable), expression has a operator, and one or more operands
 *
 * Author: Leo Tang <litang@linkedin.com>
 * */
public abstract class Atom implements Expr {
  protected double _value = 0d;

  /**
   * Evaluate this Atom
   * @return its value
   */
  public double evaluate() {
    return _value;
  }

  /**
   * Parse an Atom, if input string is not constant (double), consider it variable name
   * @param input   input string without () or whitespaces
   * @param registry variable registry to keep the variable name -> variable object mapping, only if we found it is variable
   * @return Atom object
   */
  public static Atom parse(String input, VariableRegistry registry) {
    try {
      double d = Double.parseDouble(input);
      return new Constant(d);
    } catch (NumberFormatException nfe) {
      return registry.registerVariable(input);
    }
  }
}
