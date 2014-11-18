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
 * Created by Leo Tang <litang@linkedin.com> on 11/13/14.
 */
public class VariableRegistry extends ConcurrentHashMap<String, Variable>{
    /**
     * Searches for a {@link Variable} with the given name.
     * <p>
     * If the variable does not exist <tt>null</tt>  will be returned
     * </p>
     *
     * @param name the name of the variable to search
     * @return the variable with the given name or <tt>null</tt> if no such variable was found
     */
    public Variable findVariable(String name) {
        if (this.containsKey(name)) {
            return this.get(name);
        }

        return null;
    }

    /**
     * Searches for or creates a variable with the given name.
     * <p>If no variable with the given name is found, a new variable is created in this Registry</p>
     *
     * @param name the variable to look for
     * @return a variable with the given name
     */
    public Variable registerVariable(String name) {
        if (this.containsKey(name)) {
            return this.get(name);
        }

        Variable result = new Variable(name);
        this.put(name, result);

        return result;
    }


    /***
     * Refresh values for all the variables registered, based on given <varName,value> map
     * @param varMap
     */
    public void refresh(Map<String,Double> varMap){
        for (Map.Entry<String, Variable> entry : this.entrySet()){
            if(varMap.containsKey(entry.getKey())){
               entry.getValue().setValue(varMap.get(entry.getKey()).doubleValue());
            }else {
                entry.getValue().setValue(0d);//reset value to zero, better than reusing previous value
            }
        }
    }
}
