package com.linkedin.featurefun.expr;

/**
 *
 * Constant just has a value in double type, as other numeric types can be converted to double (not verse visa)
 *
 * Author: Leo Tang <litang@linkedin.com>
 */
public class Constant extends Atom {
    public Constant(double v){
        this.value = v;
    }

    /**
     * For displaying purpose only
     * @return value in String type
     */
    public String toString(){
        return String.valueOf(this.value);
    }
}
