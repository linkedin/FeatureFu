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
 * Constant just has a value in double type, as other numeric types can be converted to double (not verse visa)
 *
 *  Author: Leo Tang <http://www.linkedin.com/in/lijuntang>
 */
public class Constant extends Atom {
  public Constant(double v) {
    this._value = v;
  }

  /**
   * For displaying purpose only
   * @return value in String type
   */
  public String toString() {
    return String.valueOf(this._value);
  }
}
