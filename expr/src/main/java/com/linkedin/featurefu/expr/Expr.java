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
 * This interface represents an s-expression: http://en.wikipedia.org/wiki/S-expression
 *
 * an s-expression is classically defined:
 *  1. an atom (constant or variable), or
 *  2. an expression of the form (operator x y) where x and y are s-expressions
 *
 * therefore the inheritance hierarchy:  Expr -> Atom  -> Constant
 *                                        \      \__->  Variable
 *                                         \_->Expression
 *
 *                                       Operator -> dozens of Operator implementations
 *
 * More s-expression parser implementation in different languages
 *   http://rosettacode.org/wiki/S-Expressions
 *
 *  Author: Leo Tang <http://www.linkedin.com/in/lijuntang>
 */
public interface Expr {
  /**
   * Each Expr can be evaluated, that's the whole purpose
   * @return evaluation result
   */
  public double evaluate();

  /**
   * For displaying purpose, to be more readable for human
   * @return human friendly string
   */
  public String toString();
}
