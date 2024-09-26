package org.polyscape.rendering.sprite;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SpriteSheetManager {

    private static int currentSpriteSheetId = 0;

    private static final ConcurrentHashMap<Integer, SpriteSheet> spriteSheets = new ConcurrentHashMap<>();

    public static void addSpriteSheet(SpriteSheet spriteSheet) {
        spriteSheet.setSpriteSheetId(currentSpriteSheetId++);
        spriteSheets.put(spriteSheet.getSpriteSheetId(), spriteSheet);
    }

    public static void clearSpriteSheets(){
        spriteSheets.clear();
    }

    public static SpriteSheet getSpriteSheet(int id) {
        return spriteSheets.get(id);
    }

    public static Collection<SpriteSheet> getSpriteSheets() {
        return spriteSheets.values();
    }

    public static Map<Integer, SpriteSheet> getSpriteSheetMap() {
        return spriteSheets;
    }

}
