package com.linkedin.featurefun.expr;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 *
 * Map to records name -> variable object mapping, so that variables with same name in a expression share the same value
 *
 * Also provide helper functions to re-set values for individual variable or all the variables registered
 *
 * If users don't want two expressions to share variables, they can create and use different VariableRegistry for them
 *
 *
 * Author: Leo Tang <litang@linkedin.com>
 */
public class VariableRegistry extends ConcurrentHashMap<String, Variable> {
    /**
     * Get variable object by string name
     * @param name variable name parsed from expression, can contain any character except white space and parenthesis "()"
     * @return  variable object found
     */
    public Variable findVariable(String name) {
        if (this.containsKey(name)) {
            return this.get(name);
        }

        return null;
    }


    /**
     * Register variable by name, try find it first, if not found, create new one and register
     * @param name variable name
     * @return  variable object found or created
     */
    public Variable registerVariable(String name) {

        Variable result = findVariable(name);

        if(result==null) {
            result = new Variable(name);
            this.put(name, result);
        }

        return result;
    }

    /***
     * Refresh values for all the variables registered, based on given <varName,value> map
     * @param varMap
     */
    public void refresh(Map<String, Double> varMap) {
        for (Map.Entry<String, Variable> entry : this.entrySet()) {
            if (varMap.containsKey(entry.getKey())) {
                entry.getValue().setValue(varMap.get(entry.getKey()).doubleValue());
            } else {
                entry.getValue().setValue(0d); //reset value to zero, better than reusing previous value
            }
        }
    }
}
