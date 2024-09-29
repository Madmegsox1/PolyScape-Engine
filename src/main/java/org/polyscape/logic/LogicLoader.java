package org.polyscape.logic;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

public final class LogicLoader {
    private final URLClassLoader classLoader;

    public LogicLoader(URL url) {
        this.classLoader = new URLClassLoader(new URL[]{url});
    }

    public LogicContainer Load(String className) throws Exception {
        Class<?> logicClass = Class.forName(className, true, classLoader);
        Constructor<?> constructor = logicClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        LogicLink link = logicClass.getAnnotation(LogicLink.class);

        if(link == null) {
            throw new Exception("LogicLink annotation not found");
        }

        return new LogicContainer((Logic) constructor.newInstance(), link.logicType(), link.linkId());
    }

}
