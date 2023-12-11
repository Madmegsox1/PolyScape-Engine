package org.polyscape.object;

import java.util.Collection;
import java.util.HashMap;

public final class ObjectManager {
    private static final HashMap<Integer, BaseObject> objects = new HashMap<>();
    private static int currentId = 0;

    public static void addObject(final BaseObject object) {
        currentId++;
        object.setObjectId(currentId);
        objects.put(currentId, object);
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

    public static Collection<BaseObject> getObjects() {
        return ObjectManager.objects.values();
    }

    public static void clearObjects() {
        currentId = 0;
        objects.clear();
    }

    public static void collisionCheck() {
        for (final BaseObject object : getObjects()) {
            for (final BaseObject object2 : getObjects()) {
                if (object.getObjectId() != object2.getObjectId() && object.collidesWith(object2)) {
                    object.handleCollision(object2);
                }
            }
        }
    }

    public static void renderObjects() {
        for (final BaseObject object : getObjects()) {
            object.render();
        }
    }


}
