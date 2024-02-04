package org.polyscape.font;


import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FontRenderer {
    public volatile ArrayList<Font> fonts;
    public volatile HashMap<String, Font> fontHashMap;

    protected boolean useMap;



    public FontRenderer(Font... f1){
        fonts = new ArrayList<>();
        fonts.addAll(Arrays.asList(f1));
    }


    public FontRenderer(boolean useMap , Map<String, Font> f1){
        fontHashMap = new HashMap<>();
        fontHashMap.putAll(f1);
        this.useMap = useMap;
    }

    public void addFont(Font f){
        if(useMap){
            throw new IllegalStateException("You are trying to access a mapped font render as a flat font render");
        }
        fonts.add(f);
    }


    public void addFont(Font f, String name){
        if(!useMap){
            throw new IllegalStateException("You are trying to access a flat font render as a mapped font render");
        }
        fontHashMap.put(name, f);
    }

    public void compileFontTextures(){
        if(useMap){
            fontHashMap.forEach((n, x) -> {
                x.compileTexture();
            });
        }else{
            fonts.forEach(Font::compileTexture);
        }
    }



    public void renderFont(String text, Vector2 vector, Color c){

        if(useMap){
            Object s = fontHashMap.keySet().toArray()[0];
            fontHashMap.get(s).drawText(text, vector.x, vector.y, c);
        }else {
            fonts.get(0).drawText(text, vector.x, vector.y, c);
        }
    }

    public void renderFont(String text, Vector2 vector){

        if(useMap){
            Object s = fontHashMap.keySet().toArray()[0];
            fontHashMap.get(s).drawText(text, vector.x, vector.y);
        }else {
            fonts.get(0).drawText(text, vector.x, vector.y);
        }


    }

    public void renderFont(String text, Vector2 vector, Color c,  String key) {
        if(!useMap){
            throw new IllegalStateException("You are trying to access a flat font render as a mapped font render");
        }
        fontHashMap.get(key).drawText(text, vector.x, vector.y, c);
    }


    public void renderFont(String text, Vector2 vector, String key){

        if(!useMap){
            throw new IllegalStateException("You are trying to access a flat font render as a mapped font render");
        }

        fontHashMap.get(key).drawText(text, vector.x, vector.y);
    }



    public void renderFont(String text, Vector2 vector, Color c, int index){
        if(useMap){
            throw new IllegalStateException("You are trying to access a mapped font render as a flat font render");
        }
        fonts.get(index).drawText(text, vector.x, vector.y, c);
    }

    public void renderFont(String text, Vector2 vector, int index){
        if(useMap){
            throw new IllegalStateException("You are trying to access a mapped font render as a flat font render");
        }
        fonts.get(index).drawText(text, vector.x, vector.y);
    }


    public Font getFont(){
        if(useMap){
            Object s = fontHashMap.keySet().toArray()[0];
            return fontHashMap.get(s);
        }else {
            return fonts.get(0);
        }
    }


    public Font getFont(int index){
        if(useMap){
            throw new IllegalStateException("You are trying to access a mapped font render as a flat font render");
        }

        return fonts.get(0);
    }


    public Font getFont(String name){
        if(!useMap){
            throw new IllegalStateException("You are trying to access a flat font render as a mapped font render");
        }

        return fontHashMap.get(name);
    }

}
