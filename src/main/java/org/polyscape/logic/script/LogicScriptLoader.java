package org.polyscape.logic.script;

import org.polyscape.Profile;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class LogicScriptLoader {
    private final ScriptEngine scriptEngine;
    private final Invocable invoker;

    public LogicScriptLoader() throws FileNotFoundException, ScriptException, NoSuchMethodException {
        this.scriptEngine = new ScriptEngineManager().getEngineByName("rhino");
        this.invoker = (Invocable) scriptEngine;

        scriptEngine.eval(new FileReader(Profile.Logic.JS_API_LOCATION));
        invoker.invokeFunction("initScript");
    }

    public void loadScript(String file) throws FileNotFoundException, ScriptException {
        this.scriptEngine.eval(new FileReader(Profile.Logic.JS_LOGIC_LOCATION + file));
    }

    public Invocable getInvoker() {
        return invoker;
    }

}
