function initScript() {

    let display = getDisplay()
    log("Hello from js")
    log("Display ID: " + display.getWindow())

    let obj = baseObject()
    obj.setPosition(vector2(50,200))
    obj.setWidth(50)
    obj.setHeight(50)
    obj.setBodyType(DYNAMIC_BODY(), true)
    obj.setBaseColor(BLACK())
    obj.setWireframe(true)
    obj.setObjectId(111)
    obj.setLevel(1)

    addObject(obj)
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

function STATIC_BODY(){
    return org.jbox2d.dynamics.BodyType.STATIC
}

function DYNAMIC_BODY(){
    return org.jbox2d.dynamics.BodyType.DYNAMIC
}
function baseObject() {
    return new Packages.org.polyscape.object.BaseObject()
}

function addObject(obj) {
    Packages.org.polyscape.object.ObjectManager.addObject(obj)
}

function getObject(id){
    return Packages.org.polyscape.object.ObjectManager.getObject(id)
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