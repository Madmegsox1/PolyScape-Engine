package org.polyscape.ui;

import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static java.util.concurrent.ConcurrentMap.*;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public class ScreenManager {
    private final HashMap<String, Screen> screenMap;

    private ConcurrentHashMap<Integer, Screen> currentViewMap; // <Z-Index, UI>

    public ScreenManager(){
        this.screenMap = new HashMap<>();
        this.currentViewMap = new ConcurrentHashMap<>();
        subscribeEvents();
    }

    public void clearAll(){
        screenMap.clear();
        currentViewMap.clear();
    }

    private void subscribeEvents(){
        IEvent<RenderEvent> renderEvent = n -> currentViewMap.values().forEach(view -> view.draw(n));
        IEvent<MouseClickEvent> mouseEvent = n -> currentViewMap.values().forEach(view -> view.onClick(n));
        IEvent<KeyEvent> keyEvent = n -> currentViewMap.values().forEach(view -> view.onKey(n));

        RenderEvent.addEvent(renderEvent, new EventMetadata(RenderEvent.class, 0));
        MouseClickEvent.addEvent(mouseEvent, new EventMetadata(MouseClickEvent.class, 0));
        KeyEvent.addEvent(keyEvent, new EventMetadata(KeyEvent.class, 0));
    }

    public void addScreen(String key, Screen screen) {
        screen.manager = this;
        screen.setName(key);
        screenMap.put(key, screen);
    }

    public Screen getCurrentScreenAtIndex(int index){
        return currentViewMap.get(index);
    }

    public void removeScreen(String key) {
        screenMap.remove(key);
    }

    public Screen getUi(String key) {
        return screenMap.get(key);
    }

    public Map<String, Screen> getAllScreens() {
        return Collections.unmodifiableMap(screenMap);
    }

    public Map<Integer, Screen> getCurrentUiMap() {
        return Collections.unmodifiableMap(currentViewMap);
    }

    public void removeCurrentUi(int z){
        currentViewMap.remove(z);
        sortCurrentUiMap();
    }


    public void setCurrentUi(int z, String screen) {
        var ui = getUi(screen);

        ui.components.clear();
        ui.onLoad();

        currentViewMap.put(z, ui);
        sortCurrentUiMap();
    }

    public void setScreenModel(int index, Object model){
        var screen = currentViewMap.get(index);
        screen.model = model;

        if(model != null) {
            screen.model();
        }
    }

    public Object getScreenModel(int index){
        var screen = currentViewMap.get(index);
        return screen.model;
    }

    public void sortCurrentUiMap() {
        currentViewMap = currentViewMap.entrySet()
                .stream()
                .sorted(ConcurrentMap.Entry.comparingByKey())
                .collect(Collectors.toConcurrentMap(
                        Entry::getKey,
                        Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        ConcurrentHashMap::new));
    }
}