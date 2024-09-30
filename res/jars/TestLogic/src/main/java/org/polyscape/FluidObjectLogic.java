package org.polyscape;

import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;
import org.polyscape.logic.LogicLink;
import org.polyscape.logic.LogicType;
import org.polyscape.logic.objectLogic.LogicObject;
import org.polyscape.object.FluidObject;
import org.polyscape.rendering.events.MouseClickEvent;

@LogicLink(logicType = LogicType.OBJECT, linkId = 991)
public class FluidObjectLogic extends LogicObject {

    private FluidObject fluid;

    @Override
    public void onRender() {

    }

    @Override
    public void onPosUpdate() {

    }

    @Override
    public void onLoad() {
        fluid = (FluidObject) object;
        IEvent<MouseClickEvent> spawnEvent = e -> {
            float adjustedMouseX = (float) e.mX;
            float adjustedMouseY = (float) e.mY;

            e.mX = adjustedMouseX - RenderLogic.translateX.get();
            e.mY = adjustedMouseY - RenderLogic.translateY.get();
            if (e.action == 0 && e.key == 0) {
                fluid.createFluid((float) e.mX, (float) e.mY, 20);
            }
        };
        MouseClickEvent.addEvent(spawnEvent, new EventMetadata(MouseClickEvent.class, 1));
    }

    @Override
    public void onUnload() {

    }
}
