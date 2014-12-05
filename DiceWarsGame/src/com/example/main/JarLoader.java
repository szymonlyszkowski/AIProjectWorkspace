package com.example.main;

import ai.dicewars.common.Agent;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.JclUtils;

/**
 * Created by szymonidas on 04.12.14.
 */
public class JarLoader {

    public Agent loadAgent(String filePath) {

        JarClassLoader jcl = new JarClassLoader();
        jcl.add(filePath);
        JclObjectFactory factory = JclObjectFactory.getInstance();

        //Create object of loaded class
        Object obj = factory.create(jcl, "ai.common.Agent");

        return JclUtils.cast(obj, Agent.class);
    }
}
