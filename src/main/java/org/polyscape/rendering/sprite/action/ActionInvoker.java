package org.polyscape.rendering.sprite.action;

import org.polyscape.object.RenderProperty;

public final class ActionInvoker extends Thread{

    private final IAction action;

    private final RenderProperty property;

    public ActionInvoker(IAction action, RenderProperty property){
        this.action = action;
        this.property = property;
        this.setName("ActionInvokerThread");
    }

    @Override
    public void run() {
        try {
            action.run(property);
        } catch (InterruptedException ignored) {

        }
    }
}
