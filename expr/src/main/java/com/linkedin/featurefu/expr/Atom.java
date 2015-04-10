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

/**
 *
 * inheritance hierarchy:  Expr -> Atom  -> Constant
 *                            \      \__->  Variable
 *                              \_->Expression
 *
 * Atom has a value, possibly a name (if it's variable), expression has a operator, and one or more operands
 *
 *  Author: Leo Tang <http://www.linkedin.com/in/lijuntang>
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
