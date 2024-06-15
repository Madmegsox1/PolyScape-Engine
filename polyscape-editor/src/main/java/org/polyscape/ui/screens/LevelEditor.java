package org.polyscape.ui.screens;

import org.jbox2d.common.Vec2;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.object.Level;
import org.polyscape.object.ObjectManager;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;
import org.polyscape.ui.component.input.Input;
import org.polyscape.ui.component.lable.Label;

public class LevelEditor extends Screen {
    Level level;

    @Override
    public void model() {
        level = getModel();

        if (level != null) {
            getComponentById("levelName").setText(level.getLevelName());
            getComponentById("levelId").setText(String.valueOf(level.getLevelNumber()));
            getComponentById("levelGForceX").setText(String.valueOf(level.getWorldSettings().x));
            getComponentById("levelGForceY").setText(String.valueOf(level.getWorldSettings().y));
            getComponentById("levelWidth").setText(String.valueOf(level.levelWidth));
            getComponentById("levelHeight").setText(String.valueOf(level.levelHeight));
        }
    }


    @Override
    public void onLoad() {
        FontMac font = new FontMac("Segoe UI", 22);
        setFont(font);
        Input levelName = new Input(Editor.leftWidth + 20, Editor.lowerY + 40, 300, 30, 2f, "levelName", this);
        levelName.setText("Level Name");

        Label nameLabel = new Label(0, -5, "Name", "nameLabel", this, levelName);

        Input levelId = new Input(Editor.leftWidth + 20 + 300 + 20, Editor.lowerY + 40, 70, 30, 2f, "levelId", this);
        levelId.setText("Level Id");

        Label idLabel = new Label(0, -5, "ID", "idLabel", this, levelId);

        Input levelGForceX = new Input(Editor.leftWidth + 20 + 300 + 70 + 100, Editor.lowerY + 40, 70, 30, 2f, "levelGForceX", this);
        levelGForceX.setText("GForce X");

        Label gxLabel = new Label(0, -5, "GForce X", "gxLabel", this, levelGForceX);

        Input levelGForceY = new Input(Editor.leftWidth + 20 + 300 + 140 + 120, Editor.lowerY + 40, 70, 30, 2f, "levelGForceY", this);
        levelGForceY.setText("GForce Y");

        Label gyLabel = new Label(0, -5, "GForce Y", "gyLabel", this, levelGForceY);

        Input levelWidth = new Input(Editor.leftWidth + 20 + 300 + 210 + 180, Editor.lowerY + 40, 70, 30, 2f, "levelWidth", this);
        levelWidth.setText("Width");

        Label widthLabel = new Label(0, -5, "Width", "widthLabel", this, levelWidth);

        Input levelHeight = new Input(Editor.leftWidth + 20 + 300 + 280 + 180, Editor.lowerY + 40, 70, 30, 2f, "levelHeight", this);
        levelHeight.setText("Height");

        Label heightLabel = new Label(0, -5, "Height", "heightLabel", this, levelHeight);

        Button saveButton = new Button(Editor.leftWidth + 20, Editor.lowerY + 80, this, "Save", "saveButton");
        saveButton.baseColor = Profile.UiThemes.Dark.accent2;

        saveButton.setClickAction(n -> {
            if (level != null) {
                ObjectManager.removeLevel(level.getLevelNumber());
                level.setLevelName(getComponentById("levelName").getText());
                level.setLevelNumber(levelId.parseInputInt());

                level.setWorldSettings(new Vec2(levelGForceX.parseInputFloat(), levelGForceY.parseInputFloat()));
                level.levelWidth = levelWidth.parseInputInt();
                level.levelHeight = levelHeight.parseInputInt();
                ObjectManager.addLevel(level);
            } else {
                Level level = new Level(Integer.parseInt(getComponentById("levelId").getText()), getComponentById("levelName").getText());
                level.setWorldSettings(new Vec2(levelGForceX.parseInputFloat(), levelGForceY.parseInputFloat()));
                level.levelWidth = levelWidth.parseInputInt();
                level.levelHeight = levelHeight.parseInputInt();
                ObjectManager.addLevel(level);
            }

            Editor.saveObjects();
        });


        addComponent(levelName);
        addComponent(nameLabel);
        addComponent(levelId);
        addComponent(idLabel);
        addComponent(levelGForceX);
        addComponent(gxLabel);
        addComponent(levelGForceY);
        addComponent(gyLabel);
        addComponent(levelWidth);
        addComponent(widthLabel);
        addComponent(levelHeight);
        addComponent(heightLabel);
        addComponent(saveButton);
    }

    @Override
    public void render(RenderEvent event) {

    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {

    }
}
