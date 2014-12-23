package com.linkedin.featurefun.expr;

/**
 *
 * Variable is a <name,value> pair, can be assigned different values many times to re-evaluate the expression it belongs to
 *
 * Author: Leo Tang <litang@linkedin.com>
 */
public class Variable extends Atom {
  private String _name; // value is already defined in Atom

  protected Variable(String name) {
    this._name = name;
  }

  public void setValue(double v) {
    _value = v;
  }

  public double getValue() {
    return _value;
  }

  public String getName() {
    return _name;
  }

  public String toString() {
    return _name;
  }
}
