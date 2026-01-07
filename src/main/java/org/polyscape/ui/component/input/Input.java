package org.polyscape.ui.component.input;

import org.lwjgl.glfw.GLFW;
import org.polyscape.Profile;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Timer;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.Component;
import org.polyscape.ui.events.ComponentClickEvent;

public class Input extends Component {

    private final Timer timer = new Timer();
    private boolean editing;

    private final float boarder;

    private IUpdateText updateText;
    private IUpdateText finalText;

    public Input(int x, int y, int width, int height, float boarder, String id, Screen screen) {
        super(x, y, width, height, screen, id);
        this.boarder = boarder;
        text = "";
    }

    @Override
    public void draw(RenderEvent event) {
        RenderEngine.drawQuadNew(new Vector2(getX() - boarder, getY() - boarder), width + boarder * 2, height + boarder * 2, Profile.UiThemes.Dark.foregroundDark);
        RenderEngine.drawQuadNew(getPosition(), width, height, baseColor);
        screen.font.renderText((this.editing) ? text + getCaret() : text, new Vector2(getX() + 5, getY() + height - 5), Profile.UiThemes.Dark.foregroundDark);
    }

    @Override
    public void onClick(MouseClickEvent event) {
        if (!inBounds(event.mX, event.mY) && editing && event.action == 1) {
            editing = false;
            updateFinalText();
        }
    }

    @Override
    public void onKey(KeyEvent event) {
        if (editing && event.action == GLFW.GLFW_PRESS) {
            if (event.key == GLFW.GLFW_KEY_BACKSPACE && text != "") {
                this.text = this.text.substring(0, this.text.length() - 1);
            } else if (screen.font.getWidth(this.text + KeyEvent.convertKey(event.key)) < this.width - 10 && KeyEvent.convertKey(event. key) != null) {
                this.text += KeyEvent.convertKey(event.key);
            } else if(event.key == GLFW.GLFW_KEY_SPACE && (screen.font.getWidth(this.text + KeyEvent.convertKey(event.key)) < this.width - 10)){
                this.text += " ";
            }else if(event.key == GLFW.GLFW_KEY_ENTER){
                updateFinalText();
                this.editing = false;
            }

            updateText();
        }

    }

    public int parseInputInt(){
        if(text.isEmpty()) return 0;
        if(text.equals("-"))return 0;
        try {
            return Integer.parseInt(text);
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public float parseInputFloat(){
        if(text.isEmpty()) return 0;
        if(text.equals("-"))return 0;

        if(text.endsWith(".")){
            try {
                return Float.parseFloat(text.substring(0, text.length() - 1));
            }catch (NumberFormatException e){
                return 0;
            }
        }
        try {
            return Float.parseFloat(text);
        }catch (NumberFormatException e){
            return 0;
        }
    }

    @Override
    public void onComponentClick(ComponentClickEvent event) {
        if (event.action == 0) {
            if(editing){
                updateText();
                updateFinalText();
            }
            editing = !editing;
        }
    }

    @Override
    public String getName() {
        return "Input";
    }

    public void setUpdateAction(IUpdateText action){
        this.updateText = action;
    }

    public void setFinalAction(IUpdateText action){
        this.finalText = action;
    }

    public boolean isEditing(){
        return this.editing;
    }


    private void updateText(){
        if(updateText != null){
            updateText.update(this, this.text);
        }
    }

    private void updateFinalText(){
        if(finalText != null){
            finalText.update(this, this.text);
        }
    }

    private String getCaret() {
        if (this.timer.passedMs(1000L)) {
            timer.reset();
        }
        if (timer.passedMs(500l)) {
            return "_";
        }
        return "";
    }
}
