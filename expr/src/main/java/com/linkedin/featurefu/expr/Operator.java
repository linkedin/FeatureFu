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

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Random;


/**
 *
 * Operator contains the actually computation logic
 * Can be easily extended to support new operators
 *
 *  Author: Leo Tang <http://www.linkedin.com/in/lijuntang>
 */
public abstract class Operator {

  private static final long DEFAULT_RANDOM_SEED = 0L;
  private static final double LN_2 = Math.log(2);

  /**
   * Calculate the value with this operator and its operands
   * @param operands List of Expr
   * @return value
   */
  public abstract double calculate(List<Expr> operands);

  /**
   * Number of operands required for this operator
   * used for parsing and sanity check purpose
   * @return The number of operands expected
   */
  public abstract int numberOfOperands();

  /**
   * The symbol to be used in the expression String, such as + - * / ==
   *  We don't use reflection to reuse a operator's class name, as most math operators have characters cannot be used in java class name
   * @return String representation of the operator
   */
  public abstract String getSymbol();

  /**
   * Parse an expr given operator string and operands string, the reason it's delegated here is because
   *  an operator knows how many operands it need
   * @param operator
   * @param operands
   * @param variableRegistry
   * @return expr parsed
   */
  public static Expr parse(String operator, List<String> operands, VariableRegistry variableRegistry) {
    Operator op;

    if (operator.equals("-") && operands.size() == 1) { //"-" could be subtract or unary minus
      op = Operator.UNARY_MINUS;
    } else {

      if (!OperatorsSupported.isSupported(operator)) {
        throw new UnsupportedOperationException(
            "Operator not supported: " + operator + ", the list of supported operators are: " + OperatorsSupported
                .getSupported());
      }

      op = OperatorsSupported.getOperator(operator);
    }

    return new Expression(op, op.parseOperands(operands, variableRegistry));
  }

  /**
   * Use polymorphism here, this is actually being called by Operator implementations, where they know their number of operands
   * @param operands    operands in list of strings
   * @param variableRegistry  registry for registering possible variables found in operands
   * @return operands in List of Expr
   */
  protected List<Expr> parseOperands(List<String> operands, VariableRegistry variableRegistry) {
    ArrayList<Expr> list = new ArrayList<Expr>();

    final int numOperands = this.numberOfOperands();

    if (operands.size() != numOperands) {
      throw new MissingFormatArgumentException(
          this.getSymbol() + " expect " + numOperands + " operands, actual number of operands is: " + operands.size());
    }

    for (int i = 0; i < numOperands; i++) {
      list.add(Expression.parse(operands.get(i), variableRegistry));
    }

    return list;
  }

  /**
   * Display the operator by its symbiol
   * @return  String representation of the operator
   */
  public String toString() {
    return this.getSymbol();
  }

  /**
   *  Implementations of an array of operators, add yours below and register it in class OperatorsSupported, just that simple
   *    Most of the implementations below are self explanatory, just to mention boolean is represented by double too:
   *       any double can be regarded as boolean, with implicit converter: double -> boolean:  0.0 is false, otherwise true
   *    Also 'if' is defined as a ternary operator (if x a b), equivalent to x ? a : b , where x, a, b can be any expressions
   */
  public static final Operator EQ = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() == operands.get(1).evaluate() ? 1 : 0;
    }

    public String getSymbol() {
      return "==";
    }
  };

  public static final Operator NE = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() != operands.get(1).evaluate() ? 1 : 0;
    }

    public String getSymbol() {
      return "!=";
    }
  };

  public static final Operator GT = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() > operands.get(1).evaluate() ? 1 : 0;
    }

    public String getSymbol() {
      return ">";
    }
  };

  public static final Operator GT_EQ = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() >= operands.get(1).evaluate() ? 1 : 0;
    }

    public String getSymbol() {
      return ">=";
    }
  };

  public static final Operator LT = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() < operands.get(1).evaluate() ? 1 : 0;
    }

    public String getSymbol() {
      return "<";
    }
  };

  public static final Operator LT_EQ = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() <= operands.get(1).evaluate() ? 1 : 0;
    }

    public String getSymbol() {
      return "<=";
    }
  };

  public static final Operator AND = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() != 0 && operands.get(1).evaluate() != 0 ? 1 : 0;
    }

    public String getSymbol() {
      return "&&";
    }
  };

  public static final Operator OR = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() != 0 || operands.get(1).evaluate() != 0 ? 1 : 0;
    }

    public String getSymbol() {
      return "||";
    }
  };

  public static final Operator ADD = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() + operands.get(1).evaluate();
    }

    public String getSymbol() {
      return "+";
    }
  };

  public static final Operator SUBTRACT = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() - operands.get(1).evaluate();
    }

    public String getSymbol() {
      return "-";
    }
  };

  public static final Operator MULTIPLY = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() * operands.get(1).evaluate();
    }

    public String getSymbol() {
      return "*";
    }
  };

  public static final Operator DIVIDE = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() / operands.get(1).evaluate();
    }

    public String getSymbol() {
      return "/";
    }
  };

  public static final Operator POWER = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return Math.pow(operands.get(0).evaluate(), operands.get(1).evaluate());
    }

    public String getSymbol() {
      return "**";
    }
  };

  public static final Operator LN = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.log(operands.get(0).evaluate());
    }

    public String getSymbol() {
      return "ln";
    }
  };

  public static final Operator LN1PLUS = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.log(1 + operands.get(0).evaluate());
    }

    public String getSymbol() {
      return "ln1plus";
    }
  };

  public static final Operator LOG2 = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.log(operands.get(0).evaluate()) / LN_2;
    }

    public String getSymbol() {
      return "log2";
    }
  };

  public static final Operator MAX = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return Math.max(operands.get(0).evaluate(), operands.get(1).evaluate());
    }

    public String getSymbol() {
      return "max";
    }
  };

  public static final Operator MIN = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return Math.min(operands.get(0).evaluate(), operands.get(1).evaluate());
    }

    public String getSymbol() {
      return "min";
    }
  };

  public static final Operator ABS = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.abs(operands.get(0).evaluate());
    }

    public String getSymbol() {
      return "abs";
    }
  };

  public static final Operator UNARY_MINUS = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return -operands.get(0).evaluate();
    }

    public String getSymbol() {
      return "unaryMinus";
    }
  };

  public static final Operator MOD = new Operator() {

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() % operands.get(1).evaluate();
    }

    public String getSymbol() {
      return "%";
    }
  };

  public static final Operator IF = new Operator() {

    public int numberOfOperands() {
      return 3;
    }

    public double calculate(List<Expr> operands) {

      double check = operands.get(0).evaluate();

      return check != 0 ? operands.get(1).evaluate() : operands.get(2).evaluate();
    }

    public String getSymbol() {
      return "if";
    }
  };

  /**
   * We use a fixed seed here so that we can get deterministic unit test results
   * It will generate predictable random numbers which should be fine in most use cases,
   */
  public static final Operator RAND = new Operator() {

    private Random _generator = new Random(DEFAULT_RANDOM_SEED);

    public int numberOfOperands() {
      return 0;
    }

    public double calculate(List<Expr> operands) {
      return _generator.nextDouble();
    }

    public String getSymbol() {
      return "rand";
    }
  };

  /**
   * We use a fixed seed here so that we can get deterministic unit test results
   * It will generate predictable random numbers which should be fine in most use cases
   */
  public static final Operator RANDIN = new Operator() {

    private Random _generator = new Random(DEFAULT_RANDOM_SEED);

    public int numberOfOperands() {
      return 2;
    }

    public double calculate(List<Expr> operands) {
      double a = operands.get(0).evaluate();
      double b = operands.get(1).evaluate() - a;
      return a + b * _generator.nextDouble();
    }

    public String getSymbol() {
      return "rand-in";
    }
  };

  public static final Operator SIGN = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.signum(operands.get(0).evaluate());
    }

    public String getSymbol() {
      return "sign";
    }
  };

  public static final Operator EXP = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.exp(operands.get(0).evaluate());
    }

    public String getSymbol() {
      return "exp";
    }
  };

  public static final Operator SIGMOID = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return 1.0 / (1 + Math.exp(-operands.get(0).evaluate()));
    }

    public String getSymbol() {
      return "sigmoid";
    }
  };

  public static final Operator ROUND = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.round(operands.get(0).evaluate());
    }

    public String getSymbol() {
      return "round";
    }
  };

  public static final Operator FLOOR = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.floor(operands.get(0).evaluate());
    }

    public String getSymbol() {
      return "floor";
    }
  };

  public static final Operator CEIL = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.ceil(operands.get(0).evaluate());
    }

    public String getSymbol() {
      return "ceil";
    }
  };

  public static final Operator SQRT = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return Math.sqrt(operands.get(0).evaluate());
    }

    public String getSymbol() {
      return "sqrt";
    }
  };

  public static final Operator NOT = new Operator() {

    public int numberOfOperands() {
      return 1;
    }

    public double calculate(List<Expr> operands) {
      return operands.get(0).evaluate() != 0 ? 0 : 1;
    }

    public String getSymbol() {
      return "!";
    }
  };

  /**
   * (in x a b) checks if x is within half-open interval [a,b)
   */
  public static final Operator IN = new Operator() {

    public int numberOfOperands() {
      return 3;
    }

    public double calculate(List<Expr> operands) {
      double check = operands.get(0).evaluate();

      return check >= operands.get(1).evaluate() && check < operands.get(2).evaluate() ? 1 : 0;
    }

    public String getSymbol() {
      return "in";
    }
  };

  public static final Operator COS = new Operator() {
    public int numberOfOperands() {
        return 1;
    }

    public double calculate(List<Expr> operands) {
        return Math.cos(operands.get(0).evaluate());
    }

    public String getSymbol() {
        return "cos";
    }
  };


    public static final Operator SIN = new Operator() {
        public int numberOfOperands() {
            return 1;
        }

        public double calculate(List<Expr> operands) {
            return Math.sin(operands.get(0).evaluate());
        }

        public String getSymbol() {
            return "sin";
        }
    };

    public static final Operator TAN = new Operator() {
        public int numberOfOperands() {
            return 1;
        }

        public double calculate(List<Expr> operands) {
            return Math.tan(operands.get(0).evaluate());
        }

        public String getSymbol() {
            return "tan";
        }
    };

    public static final Operator TANH = new Operator() {
        public int numberOfOperands() {
            return 1;
        }

        public double calculate(List<Expr> operands) {
            return Math.tanh(operands.get(0).evaluate());
        }

        public String getSymbol() {
            return "tanh";
        }
    };
}
