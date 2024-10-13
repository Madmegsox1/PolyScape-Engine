package org.polyscape.logic.script;

import org.polyscape.Profile;
import org.polyscape.logic.LogicContainer;
import org.polyscape.logic.LogicType;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class LogicScriptLoader {
    public static LogicContainer loadScript(String script) {
        var engine = new ScriptEngineManager().getEngineByName("rhino");
        var invoker = (Invocable) engine;
        LogicContainer logicContainer;
        try {
            engine.eval(new FileReader(Profile.Logic.JS_API_LOCATION));
            engine.eval(new FileReader(Profile.Logic.JS_LOGIC_LOCATION + script));
            invoker.invokeFunction("initScript");
            var logicType = LogicType.valueOf((String) engine.get("LINK_TYPE"));
            int linkId = ((Integer) engine.get("LINK_ID"));


            switch (logicType) {
                case BASE -> {
                    logicContainer = loadScript(engine, invoker, logicType, linkId, script);
                }
                case OBJECT -> {
                    logicContainer = loadScriptObject(engine, invoker, logicType, linkId, script);
                }
                default -> throw new RuntimeException("Unsupported LogicType: " + logicType);
            }

        } catch (ScriptException | FileNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return logicContainer;
    }

    public static LogicContainer loadScript(ScriptEngine engine, Invocable invoker, LogicType type, int linkId, String script) {
        LogicScript logicScript = new LogicScript(engine, invoker);

        logicScript.onLoad();

        return new LogicContainer(logicScript, type, linkId, 0, script, new File(Profile.Logic.JS_LOGIC_LOCATION + script), "");
    }

    public static LogicContainer loadScriptObject(ScriptEngine engine, Invocable invoker, LogicType type, int linkId, String script) {
        LogicScriptObject logicScriptObject = new LogicScriptObject(engine, invoker);

        return new LogicContainer(logicScriptObject, type, linkId, 0, script, new File(Profile.Logic.JS_LOGIC_LOCATION + script), "");
    }
}
