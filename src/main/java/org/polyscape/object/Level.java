package org.polyscape.object;

import org.jbox2d.common.Vec2;

public class Level {
    private int levelNumber;

    private String levelName;

    private Vec2 worldSettings;


    public Level(int levelNumber, String levelName) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getLevelName() {
        return levelName;
    }

    public Vec2 getWorldSettings() {
        return worldSettings;
    }

    public void setWorldSettings(Vec2 worldSettings) {
        this.worldSettings = worldSettings;
    }

    public void setCurrentLevel(){
        if(worldSettings != null) {
            ObjectManager.world.setGravity(worldSettings);
        }
    }
}
