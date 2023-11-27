package org.polyscape.object;

import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

import java.awt.*;
import java.util.HashMap;

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

    private final Vector2 acceleration;

    private static final Vector2 GRAVITY = new Vector2(0, 9.8f); // Gravity force
    private static final float AIR_RESISTANCE = 0.01f;
    private float mass;
    private Vector2 velocityMax = new Vector2(10, 10);

    private HashMap<BaseObject, Integer> collisionCountMap = new HashMap<>();

    private float velocityDecay = Profile.ObjectSettings.BaseVelocityDecay;

    private boolean isColliding = false;


    public BaseObject() {
        acceleration = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        this.mass = 5f;
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

    public Vector2 getCenter() {
        return new Vector2(position.x + width / 2f, position.y + height / 2f);
    }

    public Vector2[] getVectorPoints() {
        Vector2[] points = new Vector2[4];

        points[0] = position;
        points[1] = new Vector2(position.x, position.y + height);
        points[2] = new Vector2(position.x + width, position.y + height);
        points[3] = new Vector2(position.x + width, position.y);

        return points;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) position.x, (int) position.y, width, height);
    }
    public boolean collidesWith(BaseObject other) {
        return getBounds().intersects(other.getBounds());
    }

    public void handleCollision(BaseObject other){
        if(!collisionCountMap.containsKey(other)){
            collisionCountMap.put(other, 0);
        }else{
            collisionCountMap.put(other, collisionCountMap.get(other) + 1);
        }

        //this.velocity.x = -this.velocity.x;

        if(collisionCountMap.get(other) > 1){
            this.velocity.y = 0;
        }else{
            this.velocity.y = -this.velocity.y;
        }

        isColliding = true;
    }

    /*
        Pixels Per Second
     */
    public Vector2 getSpeed(){
        float speedX = (RenderEngine.fps * velocityDecay) * velocity.x;
        float speedY = (RenderEngine.fps * velocityDecay) * velocity.y;

        return new Vector2(Math.abs(speedX), Math.abs(speedY));
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }

    public boolean isWireframe() {
        return this.wireframe;
    }
    public float getVelocityDecay() {
        return velocityDecay;
    }

    public void setVelocityDecay(float velocityDecay) {
        this.velocityDecay = velocityDecay;
    }

    public void addForce(float x, float y){
        this.acceleration.addToVect((x / mass) * RenderEngine.fps, (y / mass) * RenderEngine.fps);
    }

    public void applyPhysics() {
        float deltaTime = (float) RenderEngine.deltaTime;
        // Add the acceleration to the velocity
        this.velocity.addToVect(this.acceleration.x , this.acceleration.y );

        // Apply the damping factor
        this.velocity.x *= velocityDecay;
        this.velocity.y *= velocityDecay;
        if(!isColliding) {
            this.velocity.y += deltaTime * ((GRAVITY.y * mass) * RenderEngine.fps);
        }
        isColliding = false;
        this.velocity.x -= AIR_RESISTANCE * this.velocity.x * Math.abs(this.velocity.x) * deltaTime;
        this.velocity.y -= AIR_RESISTANCE * this.velocity.y * Math.abs(this.velocity.y) * deltaTime;

        // Add the velocity to the position
        this.position.addToVect(deltaTime * this.velocity.x, deltaTime * this.velocity.y);


        // Reset the acceleration for the next frame
        this.acceleration.x = 0;
        this.acceleration.y = 0;
    }


    public void renderObject() {

        if (this.isTextured) {
            RenderEngine.drawQuadTexture(position, width, height, texture, baseColor);
        } else {
            RenderEngine.drawQuadA(position, width, height, baseColor);
        }

    }

    public void renderObjectWireframe() {
        if (this.isTextured && this.wireframeTextured) {
            RenderEngine.drawQuadTexture(position, width, height, texture, baseColor);
        }
        drawWireframe();
    }

    private void drawWireframe() {
        Vector2[] points = getVectorPoints();
        RenderEngine.drawLine(points[0], points[1], 1f, Color.WHITE);
        RenderEngine.drawLine(points[1], points[2], 1f, Color.WHITE);
        RenderEngine.drawLine(points[2], points[3], 1f, Color.WHITE);
        RenderEngine.drawLine(points[3], points[0], 1f, Color.WHITE);
    }

    public void render() {
        if(this.wireframe){
            renderObjectWireframe();
        }else{
            renderObject();
        }
        applyPhysics();
    }


    public Vector2 getVelocityMax() {
        return velocityMax;
    }

    public void setVelocityMax(Vector2 velocityMax) {
        this.velocityMax = velocityMax;
    }
}
