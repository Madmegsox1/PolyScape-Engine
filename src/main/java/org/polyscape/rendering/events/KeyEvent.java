package org.polyscape.rendering.events;

import org.lwjgl.glfw.GLFW;
import org.polyscape.event.Event;

import java.util.ArrayList;

public class KeyEvent extends Event<KeyEvent> {
    public final int key, action;
    public final long window;

    public static ArrayList<Integer> keysDown = new ArrayList<>();

    public KeyEvent(int key, int action,long window){
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
        switch (value){
            case "1":
                return "!";
            case "2":
                return "@";
            case "4":
                return "$";
            case "5":
                return "%";
            case "6":
                return "^";
            case "7":
                return "&";
            case "8":
                return "*";
            case "9":
                return "(";
            case "0":
                return ")";
            case "-":
                return "_";
            case "=":
                return "+";
            case "[":
                return "{";
            case "]":
                return "}";
            case ";":
                return ":";
            case "'":
                return "\"";
            case ",":
                return "<";
            case ".":
                return ">";
            case "/":
                return "?";
            case "\\":
                return "|";
            case "`":
                return "~";
            default:
                return value.toUpperCase();
        }
    }
}
