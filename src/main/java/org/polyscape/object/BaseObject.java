package org.polyscape.object;

import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Vector2;

/**
 * @author Madmegsox1
 * @since 25/07/2023
 */

public class BaseObject extends RenderProperty {

    private int objectId;

    private Vector2 position;

    /*
    * Velocity is applied in pixels per second.
    * TODO replace with conventional measuring system as pixels very from pc to pc
    */
    private Vector2 velocity;

    private long lastApplyVelocityInterval;

    public BaseObject(){
        lastApplyVelocityInterval = System.currentTimeMillis();
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2[] getVectorPoints(){
        Vector2[] points = new Vector2[4];

        points[0] = position;
        points[1] = new Vector2(position.x, position.y + height);
        points[2] = new Vector2(position.x + width, position.y + height);
        points[3] = new Vector2(position.x + width, position.y);

        return points;
    }

    public void renderObject(){
        if(this.isTextured){
            RenderEngine.drawQuadTexture(position, width, height, texture, baseColor);
        }
        else{
            RenderEngine.drawQuadA(position, width, height, baseColor);
        }
    }

    public void applyVelocity(){
        if(System.currentTimeMillis() - lastApplyVelocityInterval >= 1000) {
            lastApplyVelocityInterval = System.currentTimeMillis();
            position.addToVect(velocity.x, velocity.y);
        }
    }


}
