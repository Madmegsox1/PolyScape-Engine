package org.polyscape.logic.script;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class LogicScriptObject extends LogicScript {

    public LogicScriptObject(ScriptEngine engine, Invocable invoker) {
        super(engine, invoker);
    }

    public void onRender() {
        try {
            invoker.invokeFunction("onRender");
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void onPosUpdate() {
        try {
            invoker.invokeFunction("onPosUpdate");
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
