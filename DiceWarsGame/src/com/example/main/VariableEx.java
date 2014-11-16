package com.example.main;

import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Created by Krzysiek on 2014-11-16.
 */
public class VariableEx extends Variable {
    public VariableEx(String name, double value) {
        super(name);
        this.setValue(value);
    }
}
