package org.polyscape.logic;

import org.polyscape.event.EventBus;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class LogicManager {
    private final List<Logic> logics = new ArrayList<Logic>();
    private final EventBus eventBus = new EventBus();

    public void loadLogic(String path, String className) {
        try {
            LogicLoader loader = new LogicLoader(new URL("file", null, path));
            Logic logic = loader.Load(className);
            logics.add(logic);
        } catch (Exception e) {
            System.err.println("Failed to load logic: " + e.getMessage());
        }
    }

    public void initLogic() {
        for (Logic l : logics) {
            l.init(eventBus);
        }
    }

    public void loadLogic(){
        for (Logic l : logics) {
            l.onLoad();
        }
    }

    public void unloadLogic() {
        for (Logic l : logics) {
            l.onUnload();
        }
    }

    public Logic getLogic(int index){
        return logics.get(index);
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
