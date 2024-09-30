package org.polyscape.logic;

import org.polyscape.event.EventBus;
import org.polyscape.logic.objectLogic.LogicObject;
import org.polyscape.object.BaseObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class LogicManager {
    private final List<LogicContainer> logics = new ArrayList<>();
    private final EventBus eventBus = new EventBus();

    public void loadLogic(String path, String className) {
        try {
            LogicLoader loader = new LogicLoader(new URL("file", null, path));
            LogicContainer logic = loader.load(className);
            logics.add(logic);
        } catch (Exception e) {
            System.err.println("Failed to load logic: " + e.getMessage());
        }
    }

    public void loadAllLogic(String path){
        try {
            LogicLoader loader = new LogicLoader(new URL("file", null, path));
            List<LogicContainer> logicList = loader.loadAll();
            logics.addAll(logicList);
        } catch (Exception e) {
            System.err.println("Failed to load logic: " + e.getMessage());
        }
    }

    public void initLogic() {
        for (LogicContainer l : logics) {
            l.logic().init(eventBus);
        }
    }

    public void initLogicObject(BaseObject object){
        for (LogicContainer l : logics) {
            if(l.logic() instanceof LogicObject && object.getObjectId() == l.linkId()){
                object.setLogic(l);
                ((LogicObject)l.logic()).initObject(object);
            }
        }
    }

    public void loadLogic(){
        for (LogicContainer l : logics) {
            l.logic().onLoad();
        }
    }

    public void unloadLogic() {
        for (LogicContainer l : logics) {
            l.logic().onUnload();
        }
    }

    public Logic getLogic(int index){
        return logics.get(index).logic();
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
