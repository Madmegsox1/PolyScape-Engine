package org.polyscape.render.events;

import org.lwjgl.glfw.GLFW;
import org.polyscape.eventbus.Event;

import java.util.ArrayList;

public class KeyEvent extends Event<KeyEvent> {
    public final int key, action;
    public final long window;

    public static ArrayList<Integer> keysDown = new ArrayList<>();

    public KeyEvent(int key, int action,long window){
        super("KeyEvent");
        this.key = key;
        this.action = action;
        this.window = window;
        onKeyPress(this);
    }


    private static void onKeyPress(KeyEvent event){
        if(event.action == GLFW.GLFW_PRESS){
            keysDown.add(event.key);
        }

        if(event.action == GLFW.GLFW_RELEASE && keysDown.contains(event.key)){
            keysDown.remove((Object) event.key);
        }
    }

    public static boolean isKeyDown(final int button){
        return keysDown.contains(button);
    }


    public static String convertKey(int key){
        String letter = GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
        if(letter == null){
            return null;
        }

        if(isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT)){
            letter = toUpper(letter);
        }

        return letter;
    }

    public static String toUpper(String value){
        return switch (value) {
            case "1" -> "!";
            case "2" -> "@";
            case "4" -> "$";
            case "5" -> "%";
            case "6" -> "^";
            case "7" -> "&";
            case "8" -> "*";
            case "9" -> "(";
            case "0" -> ")";
            case "-" -> "_";
            case "=" -> "+";
            case "[" -> "{";
            case "]" -> "}";
            case ";" -> ":";
            case "'" -> "\"";
            case "," -> "<";
            case "." -> ">";
            case "/" -> "?";
            case "\\" -> "|";
            case "`" -> "~";
            default -> value.toUpperCase();
        };
    }
}
