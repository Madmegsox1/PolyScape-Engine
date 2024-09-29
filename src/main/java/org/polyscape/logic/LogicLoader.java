package org.polyscape.logic;

import java.net.URL;
import java.net.URLClassLoader;

public final class LogicLoader {
    private final URLClassLoader classLoader;

    public LogicLoader(URL url) {
        this.classLoader = new URLClassLoader(new URL[]{url});
    }

    public Logic Load(String className) throws Exception {
        Class<?> logicClass = Class.forName(className, true, classLoader);
        return (Logic) logicClass.getDeclaredConstructor((Class<?>) null).newInstance();
    }

}
