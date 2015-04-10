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
 *  Author: Leo Tang <http://www.linkedin.com/in/lijuntang>
 */
public class SExprTokenizer {
  public static final String OPEN_PAREN = "(";
  public static final String CLOSE_PAREN = ")";
  private static final String PAREN_REGEXP = "\\(|\\)";
  private static final String WHITESPACE_REGEXP = "\\s+";

  private static final Pattern PATTERN = Pattern.compile(PAREN_REGEXP);

  private SExprTokenizer() { }

  public static List<String> tokenize(String input) {
    List<String> result = new ArrayList<String>();
    int pos = 0;
    int depth = 0;

    input = input.trim(); //leading and trailing whitespace omitted.

    Matcher matcher = PATTERN.matcher(input);

    while (matcher.find()) {

      String paren = matcher.group();
      int start = matcher.start();
      int end = matcher.end();

      if (depth == 0) {
        String part = input.substring(pos, start).trim();

        if (!part.isEmpty()) {
          result.addAll(Arrays.asList(part.split(WHITESPACE_REGEXP)));
        }

        pos = start;
      }
      if (paren.equals(OPEN_PAREN)) {
        depth += 1;
      }

      if (paren.equals(CLOSE_PAREN)) {
        if (depth == 0) {
          throw new InputMismatchException(
              "Unmatched close parenthesis at position " + matcher.start() + " of input " + input);
        }

        depth = Math.max(0, depth - 1);

        if (depth == 0) {
          result.add(input.substring(pos, end));
          pos = end;
        }
      }
    }

    if (depth > 0) {
      throw new InputMismatchException("Unmatched close parenthesis at position " + pos + " of input " + input);
    }

    if (pos < input.length()) {

      String part = input.substring(pos).trim();

      if (!part.isEmpty()) {
        result.addAll(Arrays.asList(part.split(WHITESPACE_REGEXP)));
      }
    }

    return result;
  }
}
