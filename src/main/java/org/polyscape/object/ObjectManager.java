package org.polyscape.object;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.polyscape.Profile;
import org.polyscape.rendering.elements.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public final class ObjectManager {
    private static final HashMap<Integer, BaseObject> objects = new HashMap<>();

    private static final HashMap<Integer, Level> levels = new HashMap<>();
    private static int currentId = 0;

    public static World world = new World(new Vec2(0, 0));

    private static int currentLevel;

    public static final float PIXELS_PER_METER = 100.0f;

    public static void addObject(final BaseObject object) {
        currentId++;
        if(object.getObjectId() == 0) {
            object.setObjectId(currentId);
        }
        objects.put(object.getObjectId(), object);
    }

    public static float toPixels(float meters) {
        return meters * PIXELS_PER_METER;
    }

    public static float toMeters(float pixels) {
        return pixels / PIXELS_PER_METER;
    }

    public static Vec2 screenToWorld(float screenX, float screenY, float objectWidth, float objectHeight) {
        float worldX = screenX / PIXELS_PER_METER;
        // Directly convert y-coordinate from screen to world coordinates
        float worldY = (Profile.Display.HEIGHT - screenY) / PIXELS_PER_METER;
        worldX += (objectWidth / 2f) / PIXELS_PER_METER; // Adjust X for object's half width
        worldY -= (objectHeight / 2f) / PIXELS_PER_METER;


        return new Vec2(worldX, worldY);
    }

    public static Vector2 worldToScreen(Vec2 pos){
        float screenX = pos.x * PIXELS_PER_METER;
        float screenY = Profile.Display.HEIGHT - (pos.y  * PIXELS_PER_METER);
        return new Vector2(screenX, screenY );
    }

    public static void setPreviousPositions(){
        for (var o : objects.values()){
            o.setPreviousPosition();
        }
    }

    public static void updatePosition(){
        for (var o : objects.values()){
            o.updatePosition();
        }
    }

    public static void removeLevel(final Level level){
        levels.remove(level.getLevelNumber());
    }

    public static void removeLevel(final int levelId){
        levels.remove(levelId);
    }


    public static void removeObject(final BaseObject object) {
        objects.remove(object.getObjectId());
    }

    public static void removeObject(final int object) {
        objects.remove(object);
    }

    public static BaseObject getObject(final int id) {
        return objects.get(id);
    }

    public static void iterateObjects(final ILoopObject loopObject) {
        for (final BaseObject object : getObjects()) {
            loopObject.run(object);
        }
    }


    public static void editObject(final int id, IEditObject editObject) {
        BaseObject edited = editObject.run(objects.get(id));
        objects.put(id, edited);
    }

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(final int currentId) {
        ObjectManager.currentId = currentId;
    }

    public static HashMap<Integer, BaseObject> getObjectsMap() {
        return ObjectManager.objects;
    }

    public static Collection<Level> getLevels(){
        return levels.values();
    }


    public static Collection<BaseObject> getAllObject(){
        return objects.values();
    }

    public static Collection<BaseObject> getObjects() {

        List<BaseObject> objectList = new ArrayList<>();

        for (var obj : ObjectManager.objects.values()){
            if(obj.getLevel() == currentLevel || obj.getLevel() == -1) {
                objectList.add(obj);
            }
        }


        return objectList;
    }

    public static void clearLevels(){
        levels.clear();
    }

    public static void loadLevel(int level){
        currentLevel = level;
    }

    public static Level getCurrentLevel(){
        return levels.get(currentLevel);
    }


    public static void clearAllObjects(){
        currentId = 0;
        for (var i : getAllObject()){
            i.removeBody();
        }
        objects.clear();
    }

    public static void clearObjects() {
        currentId = 0;
        iterateObjects(BaseObject::removeBody);
        objects.clear();
    }

    public static void renderObjects(float alpha) {
        for (final BaseObject object : getObjects()) {
            object.render(alpha);
        }
    }


}
