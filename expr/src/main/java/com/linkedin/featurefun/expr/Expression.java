package com.linkedin.featurefun.expr;

import java.util.List;

/**
 *  Recursively build an expression from s-expression style string input
 *  register variables within the expression along the way
 *
 *  Created by Leo Tang <litang@linkedin.com> on 11/13/14.
 */
public class Expression implements Expr {
    private Operator operator;
    private List<Expr> operands;

    /**
     * Constructor of the expression, only used by Operator thus protected
     * @param operator operator object
     * @param operands list of Expr as operands, so it's actually a tree
     */
    protected Expression(Operator operator,  List<Expr> operands){
        this.operator = operator;
        this.operands = operands;
    }

    /**
     * Evaluate this expression given operator and its operands
     * @return
     */
    public double evaluate(){
        return this.operator.calculate(this.operands);
    }

    /**
     * Parse a expression from string, register variables if any
     * @param input    s-expression input string
     * @param variableRegistry   registry for variable name -> variable object mapping
     * @return        Expr object (Atom or a Tree for Expression)
     */
    public static Expr parse(String input, VariableRegistry variableRegistry){

        List<String> tokens = SExprTokenizer.tokenize(input);

        if(tokens.isEmpty()){
            return null;
        }

        if(tokens.size()==1){
            String new_exp = tokens.get(0);

            if(new_exp.startsWith(SExprTokenizer.open_paren)){
                return parse(new_exp.substring(1, new_exp.length() - 1),variableRegistry);
            }else{
                return Atom.parse(new_exp, variableRegistry);
            }
        }else{
          return Operator.parse(tokens.get(0), tokens.subList(1, tokens.size()),variableRegistry);
        }
    }

    /**
     * Convenience function for testing purpose, to evaluate expressions without any variables directly
     * @param input  expression with only constants
     * @return    expression evaluation result
     */
    public static double evaluate(String input){
        return parse(input,new VariableRegistry()).evaluate();
    }

    /**
     * Wrapper for more generic use case, to support Atom and Expression
     * @param expr
     * @return
     */
    public static String prettyTree(Expr expr) {
        if(expr instanceof Atom){
           return expr.toString();
        }else
            return ((Expression) expr).prettyTree("", true);
    }

    /**
     * Pretty print the expression in hierarchical tree format
     * @param prefix  Indent for this node
     * @param isTail  Is it the last operand
     */
    private String prettyTree(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder(prefix + (isTail ? "└── " : "├── ") + this.operator.getSymbol()+"\n");

        final String tab = isTail? "    " : "|   ";

        for (int i = 0; i < this.operands.size() - 1; i++) {

            Expr child = this.operands.get(i);
            if(child instanceof Atom){
                sb.append(prefix + tab + "├── " + child.toString() +"\n");
            }else sb.append(((Expression) child).prettyTree(prefix + tab, false));

        }
        if (operands.size() > 0) {

            Expr child = this.operands.get(operands.size() - 1);
            if(child instanceof Atom){
                sb.append(prefix + tab + "└── " + child.toString()+"\n");
            }else
                sb.append(((Expression) child).prettyTree(prefix + tab, true));
        }
        return sb.toString();
    }

    /***
     * convert s-expression to human friendly math expression
     * @return a human readable expression
     */
    public String toString(){
        StringBuilder builder= new StringBuilder();

        if(operator.numberOfOperands()==1){
            builder.append(operator.toString());
            builder.append(SExprTokenizer.open_paren);
            builder.append(operands.get(0).toString());
            builder.append(SExprTokenizer.close_paren);
        }else if(operator.numberOfOperands()==2){
            builder.append(SExprTokenizer.open_paren);
            builder.append(operands.get(0).toString());
            builder.append(operator.toString());
            builder.append(operands.get(1).toString());
            builder.append(SExprTokenizer.close_paren);

        }else{
            builder.append(SExprTokenizer.open_paren);
            builder.append(operator.toString());
            for(Expr opr:operands){
                builder.append(" ");
                builder.append(opr.toString());
            }
            builder.append(SExprTokenizer.close_paren);
        }
        return builder.toString();
    }

    /***
     * Command line tool for testing and pretty printing expressions
     * @param args
     */
    public static void main(String[] args){

        if(args.length<1){
            System.out.println("s-expression expected");
            return;
        }

        VariableRegistry registry = new VariableRegistry();
        Expr expr = Expression.parse(args[0], registry);

        System.out.println("="+expr.toString());

        if(registry.isEmpty()){
            System.out.println("="+expr.evaluate());
        }

        System.out.println("tree");
        System.out.println(Expression.prettyTree(expr));
    }
}
