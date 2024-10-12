package org.polyscape.logic.script;

import org.polyscape.Profile;
import org.polyscape.logic.objectLogic.LogicObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class LogicScriptObject extends LogicObject {

    private final ScriptEngine scriptEngine;
    private final Invocable invoker;

    public LogicScriptObject(String file) {
        this.scriptEngine = new ScriptEngineManager().getEngineByName("rhino");
        this.invoker = (Invocable) scriptEngine;

        try {
            scriptEngine.eval(new FileReader(Profile.Logic.JS_API_LOCATION));
            scriptEngine.eval(new FileReader(Profile.Logic.JS_LOGIC_LOCATION + file +"." + Profile.Logic.JS_LOGIC_FILEFORMAT));
            invoker.invokeFunction("initScript");
            scriptEngine.get("LOGIC_TYPE");
            scriptEngine.get("LOGIC_LINK");
        } catch (ScriptException | FileNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onRender() {
        try {
            invoker.invokeFunction("onRender");
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPosUpdate() {
        try {
            invoker.invokeFunction("onPosUpdate");
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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
