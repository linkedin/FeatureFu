package com.linkedin.featurefun.expr;

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
 * Alternative s-expression parser implementations in different languages
 *   http://rosettacode.org/wiki/S-Expressions
 *   https://github.com/scireum/parsii
 *
 * Author: Leo Tang <litang@linkedin.com>
 */
public interface Expr {
    /**
     * Each Expr can be evaluated, that's the whole purpose
     * @return evaluation result
     */
    public double evaluate();

    /**
     * For displaying purpose, to be more readable for human
     * @return  human friendly string
     */
    public String toString();
}
