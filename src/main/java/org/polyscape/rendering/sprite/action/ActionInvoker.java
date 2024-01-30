package org.polyscape.rendering.sprite.action;

import org.polyscape.object.RenderProperty;

import java.util.concurrent.ConcurrentHashMap;

public final class ActionInvoker extends Thread{

    private final IAction action;

    private final String actionName;
    private final RenderProperty property;

    public static ConcurrentHashMap<String, Boolean> runningActions = new ConcurrentHashMap<>();

    public ActionInvoker(IAction action,String actionName, RenderProperty property){
        this.action = action;
        this.actionName = actionName;
        this.property = property;
        this.setName("ActionInvokerThread");
    }

    @Override
    public void run() {
        try {
            runningActions.put(actionName, true);
            action.run(property);
            runningActions.put(actionName, false);
        } catch (InterruptedException ignored) {

        }
    }
}
