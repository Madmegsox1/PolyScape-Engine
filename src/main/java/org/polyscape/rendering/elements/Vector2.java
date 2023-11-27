package org.polyscape.rendering.elements;


import org.polyscape.font.Font;
import org.polyscape.Profile;

public final class Vector2 implements Comparable<Vector2> {
    public float x, y;

    public Vector2(final int x, final int y){
        this.x = x;
        this.y = y;
    }

    public Vector2(final double x, final double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public Vector2(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public void addToVect(final float x, final float y){
        this.x += x;
        this.y += y;
    }

    public void subToVect(final float x, final float y){
        this.x -= x;
        this.y -= y;
    }

    public void applyVelocity(final float x, final float y){
        if(this.x > 0){
            this.x -= x;
        }
        if(this.y > 0){
            this.y -= y;
        }

        if(this.y < 0){
            this.y += y;
        }
        if(this.x < 0){
            this.x += x;
        }

        if(this.x >= -0.04 && this.x <= 0.04){
            this.x = 0;
        }
        if(this.y >= -0.04 && this.y <= 0.04){
            this.y = 0;
        }

    }

    @Override
    public String toString(){
        return "[X: " + this.x + ", Y: " + this.y + "]";
    }

    public static Vector2 centerFontX(Font f, float y, String text){
        return centerScreenX(y, f.getWidth(text));
    }

    public static Vector2 centerFontY(Font f, float x, String text){
        return centerScreenY(x, f.getHeight(text));
    }

    public static Vector2 centerScreenY(float x, float height){
        return new Vector2(x, Profile.Display.HEIGHT / 2f - height / 2f);
    }

    public static Vector2 centerScreenX(float y, float length){
        return new Vector2(Profile.Display.WIDTH / 2f - length / 2f, y);
    }


    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Vector2){
            Vector2 v = (Vector2) obj;
            return v.x == this.x && v.y == this.y;
        }

        return false;
    }

    @Override
    public int compareTo(Vector2 o) {
        return Float.compare(o.y, this.y);
    }
}
