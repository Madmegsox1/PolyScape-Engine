package org.polyscape.project;


import com.google.gson.Gson;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.polyscape.Loader;
import org.polyscape.object.BaseObject;
import org.polyscape.object.Level;
import org.polyscape.object.ObjectManager;
import org.polyscape.project.model.*;
import org.polyscape.project.model.Object;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.ui.UiEngine;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ProjectLoader {
    private final String projectPath = Loader.polyscapePath + "/projects";

    private final String projectConfigFileName = projectPath + "/projectConfig.json";

    private File projectConfig = new File(projectConfigFileName);
    public ProjectConfig config;


    public void initPath() throws IOException {
        File projectFile = new File(projectPath);
        if (!projectFile.exists()) {
            projectFile.mkdirs();
        }
        if (!projectConfig.exists()) {
            projectConfig.createNewFile();
        }

    }

    public void saveProjectConfig() {
        try {
            FileWriter fw = new FileWriter(projectConfig);

            Gson gson = new Gson();

            gson.toJson(config, fw);

            Comparator<ProjectInfo> compareInfo = Comparator.comparingLong((ProjectInfo o) -> o.lastOpened).reversed();

            config.projects.sort(compareInfo);

            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<Integer,Level> loadLevels(String projectPath) throws IOException {
        String path = this.projectPath + projectPath;
        HashMap<Integer,Level> levels = new HashMap<>();

        File projectFolder = new File(path);
        if(!projectFolder.exists()) return levels;

        String levelPath = path + "/levels.json";
        File levelFile = new File(levelPath);
        if(!levelFile.exists()) return levels;

        Gson gson = new Gson();
        FileReader fr = new FileReader(levelFile);
        LevelList levelList = gson.fromJson(fr, LevelList.class);
        fr.close();
        if(levelList != null && levelList.levels != null){
            for (var lvl : levelList.levels){
                Level level = new Level(lvl.levelNumber, lvl.levelName);
                level.levelWidth = lvl.levelWidth;
                level.levelHeight = lvl.levelHeight;
                level.setWorldSettings(new Vec2(lvl.worldGForceX, lvl.worldGForceY));
                levels.put(level.getLevelNumber(), level);
            }
        }

        return levels;
    }


    public void saveLevels(String projectPath) throws IOException {
        String path = this.projectPath + projectPath;

        File projectFolder = new File(path);
        if (!projectFolder.exists()) {
            projectFolder.mkdirs();
        }

        String lvls = path + "/levels.json";
        File levelFile = new File(lvls);
        if (!levelFile.exists()) {
            levelFile.createNewFile();
        }

        LevelList lvlList = new LevelList();
        lvlList.levels = new ArrayList<>();

        ObjectManager.getLevels().forEach(n -> {
            org.polyscape.project.model.Level level = new org.polyscape.project.model.Level();
            level.levelName = n.getLevelName();
            level.levelNumber = n.getLevelNumber();
            level.levelWidth = n.levelWidth;
            level.levelHeight = n.levelHeight;
            level.worldGForceX = n.getWorldSettings().x;
            level.worldGForceY = n.getWorldSettings().y;
            lvlList.levels.add(level);
        });

        Gson gson = new Gson();

        FileWriter fw = new FileWriter(levelFile);
        gson.toJson(lvlList, fw);

        fw.close();
    }

    public List<BaseObject> loadObject(String projectPath) throws IOException {
        String path = this.projectPath + projectPath;
        List<BaseObject> baseObjects = new ArrayList<>();

        File projectFolder = new File(path);
        if (!projectFolder.exists()) return baseObjects;

        String object = path + "/objects.json";
        File objectFile = new File(object);
        if (!objectFile.exists()) return baseObjects;

        Gson gson = new Gson();
        FileReader fr = new FileReader(objectFile);
        ObjectList objectList = gson.fromJson(fr, ObjectList.class);
        fr.close();
        if (objectList != null && objectList.objects != null) {
            for (Object obj : objectList.objects) {
                BaseObject baseObject = getBaseObject(obj);
                baseObjects.add(baseObject);
            }
        }

        return baseObjects;
    }

    private static BaseObject getBaseObject(Object obj) {
        BaseObject baseObject = new BaseObject();
        baseObject.setObjectId(obj.objectId);
        baseObject.setPosition(new Vector2(obj.x, obj.y));
        baseObject.setWidth(obj.width);
        baseObject.setHeight(obj.height);
        baseObject.setLevel(obj.level);

        baseObject.setBaseColor(new org.polyscape.rendering.elements.Color(
                obj.renderProperty.color.r,
                obj.renderProperty.color.g,
                obj.renderProperty.color.b,
                obj.renderProperty.color.a));

        baseObject.setTextured(obj.renderProperty.isTextured);
        if(baseObject.isTextured()){
            baseObject.setTexture(new Texture(obj.renderProperty.textureName));
        }

        if(obj.physicsBody != null){
            baseObject.setBodyType(BodyType.values()[obj.physicsBody.bodyType], false);
            baseObject.setAngleCals(obj.physicsBody.angleCals, false);
            baseObject.setDensity(obj.physicsBody.density, false);
            baseObject.setFriction(obj.physicsBody.friction, false);
            baseObject.setLinearDamping(obj.physicsBody.linearDamping, false);
            baseObject.setUpPhysicsBody();
            baseObject.setAngle(obj.physicsBody.angle);
            if(obj.physicsBody.active){
                //baseObject.setBodyActive();
            }else{
                baseObject.destroyPhysicsBody();
            }
        }
        return baseObject;
    }

    public void saveObjects(String projectPath) throws IOException {
        String path = this.projectPath + projectPath;

        File projectFolder = new File(path);
        if (!projectFolder.exists()) {
            projectFolder.mkdirs();
        }

        String object = path + "/objects.json";
        File objectFile = new File(object);
        if (!objectFile.exists()) {
            objectFile.createNewFile();
        }

        ObjectList objectList = new ObjectList();
        objectList.objects = new ArrayList<>();
        ObjectManager.getAllObject().forEach(n -> {
            objectList.objects.add(objectFactory(n));
        });


        Gson gson = new Gson();

        FileWriter fw = new FileWriter(objectFile);
        gson.toJson(objectList, fw);

        fw.close();
    }

    public Object objectFactory(BaseObject baseObject) {
        Object object = new Object();

        object.objectId = baseObject.getObjectId();
        object.x = baseObject.getPosition().x;
        object.y = baseObject.getPosition().y;
        object.width = baseObject.getWidth();
        object.height = baseObject.getHeight();
        object.level = baseObject.getLevel();

        object.renderProperty = new RenderProperty();
        object.renderProperty.color = new Color();
        object.renderProperty.color.r = baseObject.getBaseColor().r;
        object.renderProperty.color.g = baseObject.getBaseColor().g;
        object.renderProperty.color.b = baseObject.getBaseColor().b;
        object.renderProperty.color.a = baseObject.getBaseColor().a;
        object.renderProperty.width = baseObject.getWidth();
        object.renderProperty.height = baseObject.getHeight();
        object.renderProperty.isTextured = baseObject.isTextured();
        if (baseObject.isTextured()) {
            var tex = Texture.loadedTextures.get(baseObject.getTexture().getTexture());
            if (!tex.isEmpty()) {
                object.renderProperty.textureName = tex;
            }
        }

        if (baseObject.getBody() != null) {
            object.physicsBody = new PhysicsBody();
            object.physicsBody.angle = baseObject.getAngle();
            object.physicsBody.bodyType = baseObject.getBody().m_type.ordinal();
            object.physicsBody.angleCals = baseObject.getBody().isFixedRotation();
            object.physicsBody.density = baseObject.getBody().getFixtureList().getDensity();
            object.physicsBody.friction = baseObject.getBody().getFixtureList().getFriction();
            object.physicsBody.linearDamping = baseObject.getBody().getLinearDamping();
            object.physicsBody.active = baseObject.getBody().isActive();
        }

        return object;


    }


    public void loadProjects() throws IOException {
        FileReader fr = new FileReader(projectConfig);

        Gson gson = new Gson();

        this.config = gson.fromJson(fr, ProjectConfig.class);

        Comparator<ProjectInfo> compareInfo = Comparator.comparingLong((ProjectInfo o) -> o.lastOpened).reversed();

        if (config != null) {
            config.projects.sort(compareInfo);
        }
        fr.close();
    }
}
