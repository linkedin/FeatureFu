package com.linkedin.featurefun.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple s-expression tokenizer to return top level s-expressions found
 * Example:
 *      tokenize('(a b (c d)) e f (g)') returns ['(a b (c d))', 'e', 'f', '(g)']
 *      tokenize('(x y z)') returns ['(x y z)']
 * clients are supposed to call tokenize() many times to fully parse an complex expression
 * ported and modified from python code:
 *  http://www.nltk.org/_modules/nltk/tokenize/sexpr.html
 *
 * Author: Leo Tang <litang@linkedin.com>
 */
public class SExprTokenizer {
    public static final String open_paren = "(";
    public static final String close_paren = ")";
    private static final String paren_regexp = "\\(|\\)";
    private static final String whitespace_regexp = "\\s+";

    private static final Pattern pattern = Pattern.compile(paren_regexp);

    public static List<String> tokenize(String input){
        List<String> result=new ArrayList<String>();
        int pos = 0;
        int depth = 0;

        input = input.trim();//leading and trailing whitespace omitted.

        Matcher matcher = pattern.matcher(input);

        while(matcher.find()) {

            String paren = matcher.group();
            int start =  matcher.start();
            int end = matcher.end();

           if(depth==0){
               String part = input.substring(pos, start).trim();

               if(!part.isEmpty())
                   result.addAll(Arrays.asList(part.split(whitespace_regexp)));

               pos=start;
           }
            if(paren.equals(open_paren)){
                depth +=1;
            }

            if(paren.equals(close_paren)){
               if(depth==0) throw new InputMismatchException("Unmatched close parenthesis at position "+ matcher.start() + " of input "+input);

                depth = Math.max(0, depth - 1);

                if(depth==0){
                    result.add(input.substring(pos,end));
                    pos = end;
                }
            }
    }

    if(depth>0){
            throw new InputMismatchException("Unmatched close parenthesis at position "+ pos + " of input "+input);
    }

    if (pos < input.length()){

        String part = input.substring(pos).trim();

        if(!part.isEmpty())
            result.addAll(Arrays.asList(part.split(whitespace_regexp)));
    }

       return result;
    }
}
