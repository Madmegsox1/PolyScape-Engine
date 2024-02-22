package org.polyscape.ui;

import org.polyscape.event.IEvent;
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
    private final HashMap<String, IScreen> screenMap;

    private ConcurrentHashMap<Integer, IScreen> currentViewMap; // <Z-Index, UI>

    public ScreenManager(){
        this.screenMap = new HashMap<>();
        this.currentViewMap = new ConcurrentHashMap<>();
        subscribeEvents();
    }


    private void subscribeEvents(){
        IEvent<RenderEvent> renderEvent = n -> currentViewMap.values().forEach(view -> view.draw(n));
        IEvent<MouseClickEvent> mouseEvent = n -> currentViewMap.values().forEach(view -> view.onClick(n));
        IEvent<KeyEvent> keyEvent = n -> currentViewMap.values().forEach(view -> view.onKey(n));

        RenderEvent.addEvent(renderEvent, RenderEvent.class);
        MouseClickEvent.addEvent(mouseEvent, MouseClickEvent.class);
        KeyEvent.addEvent(keyEvent, KeyEvent.class);
    }

    public void addScreen(String key, Screen screen) {
        screen.manager = this;
        screenMap.put(key, screen);
    }
    public void removeScreen(String key) {
        screenMap.remove(key);
    }
    public IScreen getUi(String key) {
        return screenMap.get(key);
    }

    public Map<String, IScreen> getAllScreens() {
        return Collections.unmodifiableMap(screenMap);
    }

    public Map<Integer, IScreen> getCurrentUiMap() {
        return Collections.unmodifiableMap(currentViewMap);
    }

    public void removeCurrentUi(int z){
        currentViewMap.remove(z);
        sortCurrentUiMap();
    }


    public void setCurrentUi(int z, String screen) {
        currentViewMap.put(z, getUi(screen));
        sortCurrentUiMap();
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