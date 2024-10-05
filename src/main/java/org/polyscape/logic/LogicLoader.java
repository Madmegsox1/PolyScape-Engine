package org.polyscape.logic;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class LogicLoader {
    private final URLClassLoader classLoader;
    private final File file;

    public LogicLoader(URL url) {
        this.classLoader = new URLClassLoader(new URL[]{url});
        this.file = new File(url.getFile());
    }

    public LogicContainer load(String className) throws Exception {
        Class<?> logicClass = Class.forName(className, true, classLoader);
        Constructor<?> constructor = logicClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        LogicLink link = logicClass.getAnnotation(LogicLink.class);

        if(link == null) {
            throw new Exception("LogicLink annotation not found");
        }

        return new LogicContainer((Logic) constructor.newInstance(), link.logicType(), link.linkId(), 0, logicClass.getSimpleName());
    }

    public List<LogicContainer> loadAll() throws Exception {
        List<LogicContainer> containers = new ArrayList<>();

        try(JarFile jar = new JarFile(file)) {
            Enumeration<JarEntry> entries = jar.entries();
            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
                    String className = entry.getName().replace('/', '.').replace(".class", "");
                    try {
                        Class<?> logicClass = Class.forName(className, true, classLoader);
                        Constructor<?> constructor = logicClass.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        LogicLink link = logicClass.getAnnotation(LogicLink.class);
                        if(link != null) {
                            containers.add(new LogicContainer((Logic) constructor.newInstance(), link.logicType(), link.linkId(), 0, logicClass.getSimpleName()));
                        }
                    } catch (ClassNotFoundException | NoClassDefFoundError e) {
                        System.out.println("Failed to load class " + className);
                    }
                }
            }
        }
        return containers;
    }

}
