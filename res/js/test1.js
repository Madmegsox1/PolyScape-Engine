var LINK_TYPE = "OBJECT"
var LINK_ID = 800

let object;

function onLoad(){
    object = getObject(LINK_ID)
}

function onUnload(){
}

function onRender(){
    getRenderEngine().drawQuad(vector2(object.getPosition().x - (30/2), object.getPosition().y - (30/2)), 30.0, 30.0, BLACK())
}

function onPosUpdate(){

}