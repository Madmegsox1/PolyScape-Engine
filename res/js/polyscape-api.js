function initScript() {

    let display = getDisplay()
    log("Hello from js")
    log("Display ID: " + display.getWindow())

    let txt = texture("001")
    let jsPlayerPos = vector2(100, 100)

    addRenderEvent(function (event) {
        event.renderEngine.drawQuadTexture(jsPlayerPos, 100.0, 100.0, txt)
    })

    addKeyboardEvent(function (event) {
        jsPlayerPos.addToVect(1, 0)
    })

}

function getEventBus() {
    return Packages.org.polyscape.logic.script.LogicScriptApi.getEventBus()
}

function getRenderEngine() {
    return Packages.org.polyscape.logic.script.LogicScriptApi.getRenderEngine()
}

function getDisplay() {
    return Packages.org.polyscape.logic.script.LogicScriptApi.getDisplay()
}

function getRenderer() {
    return Packages.org.polyscape.logic.script.LogicScriptApi.getRenderer()
}

function getFontRenderer() {
    return Packages.org.polyscape.logic.script.LogicScriptApi.getFontRenderer()
}

function getScreenManager() {
    return Packages.org.polyscape.logic.script.LogicScriptApi.getScreenManager()
}

function log(message) {
    Packages.org.polyscape.logic.script.LogicScriptApi.writeLine(message)
}

function color(r,g,b){
    return new Packages.org.polyscape.rendering.elements.Color(r, g, b, 255)
}

function colorAlpha(r,g,b,a){
    return new Packages.org.polyscape.rendering.elements.Color(r, g, b, a)
}

function BLACK(){
    return Packages.org.polyscape.rendering.elements.Color.BLACK;
}

function vector2(x, y) {
    return new Packages.org.polyscape.rendering.elements.Vector2(x, y)
}

function texture(fileName) {
    return new Packages.org.polyscape.rendering.elements.Texture(fileName)
}

function addRenderEvent(event) {
    let renderEvent = new Packages.org.polyscape.event.IEvent()
    {
        run: function (instance) {
            event(instance)
        }
    }

    let eventMetadata = new Packages.org.polyscape.event.EventMetadata(
        Packages.org.polyscape.rendering.events.RenderEvent,
        1
    );

    Packages.org.polyscape.event.Event.addEvent(renderEvent, eventMetadata);
}


function addMouseEvent(event) {
    let mouseEvent= new Packages.org.polyscape.event.IEvent()
    {
        run: function (instance) {
            event(instance)
        }
    }

    let eventMetadata = new Packages.org.polyscape.event.EventMetadata(
        Packages.org.polyscape.rendering.events.MouseClickEvent,
        1
    );

    Packages.org.polyscape.event.Event.addEvent(mouseEvent, eventMetadata);
}

function addKeyboardEvent(event) {
    let keyboardEvent = new Packages.org.polyscape.event.IEvent()
    {
        run: function (instance) {
            event(instance)
        }
    }

    let eventMetadata = new Packages.org.polyscape.event.EventMetadata(
        Packages.org.polyscape.rendering.events.KeyEvent,
        1
    );

    Packages.org.polyscape.event.Event.addEvent(keyboardEvent, eventMetadata);
}