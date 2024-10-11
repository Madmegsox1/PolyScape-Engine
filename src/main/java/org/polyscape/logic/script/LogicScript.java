package org.polyscape.logic.script;

import org.polyscape.logic.Logic;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class LogicScript extends Logic {

    private final ScriptEngine engine;
    private final Invocable invoker;

    public LogicScript(ScriptEngine engine, Invocable invoker){
       this.engine = engine;
       this.invoker = invoker;
    }

    @Override
    public void onLoad() {
        try {
            invoker.invokeFunction("onLoad");
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUnload() {
        try {
            invoker.invokeFunction("onUnload");
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
