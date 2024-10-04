package org.polyscape.logic;

import org.polyscape.event.EventBus;
import org.polyscape.logic.objectLogic.LogicObject;
import org.polyscape.object.BaseObject;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public final class LogicManager {
    private static final HashMap<Integer,LogicContainer> logics = new HashMap<>();
    private static final EventBus eventBus = new EventBus();

    private static int currentLogicId = 0;

    public static void loadLogic(String path, String className) {
        try {
            LogicLoader loader = new LogicLoader(new URL("file", null, path));
            LogicContainer logic = loader.load(className);
            currentLogicId++;
            logics.put(currentLogicId, logic);
        } catch (Exception e) {
            System.err.println("Failed to load logic: " + e.getMessage());
        }
    }

    public static void loadAllLogic(String path){
        try {
            LogicLoader loader = new LogicLoader(new URL("file", null, path));
            List<LogicContainer> logicList = loader.loadAll();
            logicList.spliterator().forEachRemaining(logicContainer->{
                currentLogicId++;
                logics.put(currentLogicId, logicContainer);
            });
        } catch (Exception e) {
            System.err.println("Failed to load logic: " + e.getMessage());
        }
    }

    public static void initLogic() {
        for (LogicContainer l : logics.values()) {
            l.logic().init(eventBus);
        }
    }

    public static void initLogicObject(BaseObject object){
        for (LogicContainer l : logics.values()) {
            if(l.logic() instanceof LogicObject && object.getObjectId() == l.linkId()){
                object.setLogic(l);
                ((LogicObject)l.logic()).initObject(object);
            }
        }
    }

    public static void loadLogic(){
        for (LogicContainer l : logics.values()) {
            l.logic().onLoad();
        }
    }

    public static void unloadLogic() {
        for (LogicContainer l : logics.values()) {
            l.logic().onUnload();
        }
    }

    public static Collection<LogicContainer> getLogics() {
        return logics.values();
    }

    public static Logic getLogic(int index){
        return logics.get(index).logic();
    }

    public static EventBus getEventBus() {
        return eventBus;
    }
}
