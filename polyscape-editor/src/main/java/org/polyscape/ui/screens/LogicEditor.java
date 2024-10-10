package org.polyscape.ui.screens;

import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.logic.LogicContainer;
import org.polyscape.logic.LogicManager;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;
import org.polyscape.ui.component.checkbox.CheckBox;
import org.polyscape.ui.component.checkbox.CheckBoxType;
import org.polyscape.ui.component.input.Input;
import org.polyscape.ui.component.lable.Label;

public final class LogicEditor extends Screen {

    private LogicContainer logicContainer;

    @Override
    public void model() {
        this.logicContainer = getModel();

        getComponentById("fileName").setText(this.logicContainer.logicFile().getName());
        ((CheckBox)getComponentById("loadAll")).state = 1;

    }

    @Override
    public void onLoad() {
        components.clear();
        FontMac font = new FontMac("Segoe UI", 22);
        setFont(font);

        Input fileName = new Input(Editor.leftWidth + 20, Editor.lowerY + 40, 500, 30, 2f, "fileName", this);
        Label fileLabel = new Label(0, -5, "File Name", "fileNameLabel", this,fileName);

        CheckBox loadAll = new CheckBox((int)(fileName.getX() + fileName.getWidth() + 20), Editor.lowerY + 40, "loadAll", CheckBoxType.Untextured, this);
        loadAll.setText("Load All");
        loadAll.baseColor = Profile.UiThemes.Dark.foregroundDark;

        Input className = new Input(Editor.leftWidth + 20, Editor.lowerY + 40 + 30 + 25, 500, 30, 2f, "className", this);
        Label classNameLabel = new Label(0, -5, "Class Name", "classNameLabel", this, className);


        Button loadLogic = new Button(Editor.leftWidth + 40 + 500, Editor.lowerY + 40 + 30 + 25, this, "Load Logic", "loadLogic");

        className.hidden = true;
        classNameLabel.hidden = true;

        loadAll.setClickAction((n, i) -> {
            if(i){
                className.hidden = false;
                classNameLabel.hidden = false;
            }
            else {
                className.hidden = true;
                classNameLabel.hidden = true;
            }
        });

        loadLogic.setClickAction(n -> {
            var jar = (Input) getComponentById("fileName");
            var checkBox = (CheckBox) getComponentById("loadAll");

            if(checkBox.state == 1) {
               var clName = (Input) getComponentById("className");
               LogicManager.loadLogic(jar.getText(), clName.getText());
            }
            else {
                LogicManager.loadAllLogic(jar.getText());
            }
        });



        addComponent(fileName);
        addComponent(fileLabel);
        addComponent(loadAll);
        addComponent(className);
        addComponent(classNameLabel);
        addComponent(loadLogic);
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