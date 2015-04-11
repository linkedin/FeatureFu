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

import java.util.List;

/**
 *  Recursively build an expression from s-expression style string input
 *  register variables within the expression along the way
 *
 *  Author: Leo Tang <http://www.linkedin.com/in/lijuntang>
 */
public class Expression implements Expr {
  private Operator _operator;
  private List<Expr> _operands;

  /**
   * Constructor of the expression, only used by Operator thus protected
   * @param operator _operator object
   * @param operands list of Expr as _operands, so it's actually a tree
   */
  protected Expression(Operator operator, List<Expr> operands) {
    this._operator = operator;
    this._operands = operands;
  }

  /**
   * Evaluate this expression given _operator and its _operands
   * @return value evaluated
   */
  public double evaluate() {
    return this._operator.calculate(this._operands);
  }

  /**
   * Parse a expression from string, register variables if any
   * @param input    s-expression input string
   * @param variableRegistry   registry for variable name -> variable object mapping
   * @return Expr object (Atom or a Tree for Expression)
   */
  public static Expr parse(String input, VariableRegistry variableRegistry) {

    List<String> tokens = SExprTokenizer.tokenize(input);

    if (tokens.isEmpty()) {
      return null;
    }

    if (tokens.size() == 1) {
      String newExp = tokens.get(0);

      if (newExp.startsWith(SExprTokenizer.OPEN_PAREN)) {
        return parse(newExp.substring(1, newExp.length() - 1), variableRegistry);
      } else {
        return Atom.parse(newExp, variableRegistry);
      }
    } else {
      return Operator.parse(tokens.get(0), tokens.subList(1, tokens.size()), variableRegistry);
    }
  }

  /**
   * Convenience function for testing purpose, to evaluate expressions without any variables directly
   * @param input  expression with only constants
   * @return expression evaluation result
   */
  public static double evaluate(String input) {
    return parse(input, new VariableRegistry()).evaluate();
  }

  /**
   * Wrapper for more generic use case, to support Atom and Expression
   * @param expr
   * @return String as print friendly tree illustration of the s-expression
   */
  public static String prettyTree(Expr expr) {
    if (expr instanceof Atom) {
      return expr.toString();
    } else {
      return ((Expression) expr).prettyTree("", true);
    }
  }

  /**
   * Pretty print the expression in hierarchical tree format
     * Reference: http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram/8948691#8948691
     *
   * @param prefix  Indent for this node
   * @param isTail  Is it the last operand
   * @return String representation of the tree structure
   */
  private String prettyTree(String prefix, boolean isTail) {
    StringBuilder sb = new StringBuilder(prefix + (isTail ? "└── " : "├── ") + this._operator.getSymbol() + "\n");

    final String tab = isTail ? "    " : "|   ";

    for (int i = 0; i < this._operands.size() - 1; i++) {

      Expr child = this._operands.get(i);
      if (child instanceof Atom) {
        sb.append(prefix + tab + "├── " + child.toString() + "\n");
      } else {
        sb.append(((Expression) child).prettyTree(prefix + tab, false));
      }
    }
    if (_operands.size() > 0) {

      Expr child = this._operands.get(_operands.size() - 1);
      if (child instanceof Atom) {
        sb.append(prefix + tab + "└── " + child.toString() + "\n");
      } else {
        sb.append(((Expression) child).prettyTree(prefix + tab, true));
      }
    }
    return sb.toString();
  }

  /***
   * convert s-expression to human friendly math expression
   * @return a human readable expression
   */
  public String toString() {
    StringBuilder builder = new StringBuilder();

    if (_operator.numberOfOperands() == 1) {
      builder.append(_operator.toString());
      builder.append(SExprTokenizer.OPEN_PAREN);
      builder.append(_operands.get(0).toString());
      builder.append(SExprTokenizer.CLOSE_PAREN);
    } else if (_operator.numberOfOperands() == 2) {
      builder.append(SExprTokenizer.OPEN_PAREN);
      builder.append(_operands.get(0).toString());
      builder.append(_operator.toString());
      builder.append(_operands.get(1).toString());
      builder.append(SExprTokenizer.CLOSE_PAREN);
    } else {
      builder.append(SExprTokenizer.OPEN_PAREN);
      builder.append(_operator.toString());
      for (Expr opr : _operands) {
        builder.append(" ");
        builder.append(opr.toString());
      }
      builder.append(SExprTokenizer.CLOSE_PAREN);
    }
    return builder.toString();
  }

    /***
     * Command line tool for testing and pretty printing expressions
     * @param args
     */
    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("s-expression expected");
            return;
        }

        VariableRegistry registry = new VariableRegistry();
        Expr expr = Expression.parse(args[0], registry);

        System.out.println("=" + expr.toString());

        if (registry.isEmpty()) {
            System.out.println("=" + expr.evaluate());
        }

        System.out.println("tree");
        System.out.println(Expression.prettyTree(expr));
  }
}
