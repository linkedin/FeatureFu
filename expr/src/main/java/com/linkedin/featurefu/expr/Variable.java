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
 * Variable is a <name,value> pair, can be assigned different values many times to re-evaluate the expression it belongs to
 *
 *  Author: Leo Tang <http://www.linkedin.com/in/lijuntang>
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
