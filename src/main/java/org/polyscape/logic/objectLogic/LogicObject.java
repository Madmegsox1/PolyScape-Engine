package org.polyscape.logic.objectLogic;

import org.polyscape.logic.Logic;
import org.polyscape.object.BaseObject;

public abstract class LogicObject extends Logic {
    protected BaseObject object;

    public final void initObject(BaseObject object){
        this.object = object;
    }

    public abstract void onRender();

    public abstract void onPosUpdate();

}
