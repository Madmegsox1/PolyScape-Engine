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


    public float dot(final Vector2 v){
        return this.x * v.x + this.y * v.y;
    }

    public Vector2 scale(final float scale){
        return new Vector2(this.x * scale, this.y * scale);
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

    public static Vector2 sub(final Vector2 v1, final Vector2 v2){
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }

    public static float dot(final Vector2 v1, final Vector2 v2){
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static Vector2 add(final Vector2 v1, final Vector2 v2){
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }


    public Boolean greaterThan(Vector2 v){
        return this.x > v.x && this.y > v.y;
    }

    public Boolean greaterThanOrEqual(Vector2 v){
        return this.x >= v.x && this.y >= v.y;
    }

    public Boolean lessThan(Vector2 v){
        return this.x < v.x && this.y < v.y;
    }

    public Boolean lessThanOrEqual(Vector2 v){
        return this.x <= v.x && this.y <= v.y;
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
