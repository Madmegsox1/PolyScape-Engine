package org.polyscape.object;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.polyscape.Profile;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.polyscape.object.ObjectManager.*;

/**
 * @author Madmegsox1
 * @since 25/07/2023
 */

public class BaseObject extends RenderProperty {

    private int objectId = 0;

    private Vector2 position;

    private Vector2 previousPosition;

    private BodyDef bodyDef;

    private Body body;

    public BaseObject() {
        bodyDef = new BodyDef();
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public float getAngle(){
        return (float) Math.toDegrees(body.getAngle());
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        if(body != null) {
            body.setTransform(ObjectManager.screenToWorld(position.x, position.y, this.width, this.height), body.getAngle());
        }
    }

    public void setAngle(double angle){
        if(body != null) {
            body.setTransform(ObjectManager.screenToWorld(position.x, position.y, this.width, this.height),(float) Math.toRadians(angle));
        }
    }

    public Vector2 getVelocity() {
        var v = this.body.getLinearVelocity();
        return new Vector2(v.x, v.y);
    }

    public void setVelocity(Vector2 velocity) {
        this.body.setLinearVelocity(new Vec2(velocity.x, velocity.y));
    }

    public void setWidth(int width) {
        this.width = width;

        if(body != null) {
            var angle = Math.toDegrees(body.getAngle());
            setUpPhysicsBody(body.getType());
            setAngle(angle);
        }
    }

    public void setHeight(int height) {
        this.height = height;
        if(body != null) {
            var angle = Math.toDegrees(body.getAngle());
            setUpPhysicsBody(body.getType());
            setAngle(angle);
        }
    }

    public Vector2 getCenter() {
        return new Vector2(position.x + width / 2f, position.y + height / 2f);
    }

    public Vector2[] getVectorPoints() {
        Vector2[] points = new Vector2[4];

        points[0] = position; // Bottom left

        points[1] = new Vector2(position.x, position.y + height); // Top left

        points[2] = new Vector2(position.x + width, position.y + height); // Top right

        points[3] = new Vector2(position.x + width, position.y); // Bottom right

        return points;
    }


    /*
        Pixels Per Second
     */
    public Vector2 getSpeed() {
        var v = body.getLinearVelocity();
        float speedX = RenderEngine.fps* v.x;
        float speedY = RenderEngine.fps * v.y;

        return new Vector2(Math.abs(speedX), Math.abs(speedY));
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }

    public boolean isWireframe() {
        return this.wireframe;
    }

    public Body getBody() {
        return this.body;
    }
    public void addForce(float x, float y) {
        body.applyForceToCenter(new Vec2(x, y));
    }

    public void removeBody(){
        ObjectManager.world.destroyBody(this.body);
    }

    public boolean isPointInObject(Vector2 point){
        Vector2[] ver = getVectorPoints();
        for(int i = 0; i < ver.length; i++){
            Vector2 start = ver[i];
            Vector2 end = ver[(i + 1) % ver.length];

            float edgeVecX = end.x - start.x;
            float edgeVecY = end.y - start.y;

            float pointVecX = point.x - start.x;
            float pointVecY = point.y - start.y;

            if ((edgeVecX * pointVecY - edgeVecY * pointVecX) < 0) {
                return false; // Point is outside this edge, so it's outside the quad
            }
        }
        return true;
    }

    public void setPreviousPosition(){
        if(position == null) return;
        this.previousPosition = new Vector2(position.x, position.y);;
    }


    public void updatePosition(){
        if(body == null) return;
        position = worldToScreen(body.getPosition());
        position.x -= (this.width / 2f);
        position.y -= (this.height / 2f);
    }


    public Vector2 getInterpolatedPosition(float alpha){
        if(previousPosition == null) return position;
        float interpX = previousPosition.x + (position.x - previousPosition.x) * alpha;
        float interpY = previousPosition.y + (position.y - previousPosition.y) * alpha;
        return new Vector2(interpX, interpY);
    }

    public void addToPos(int x, int y){
        position.addToVect(x, y);
        //ObjectManager.world.destroyBody(this.body);
        //setUpPhysicsBody(this.bodyDef.type);
        body.setTransform(ObjectManager.screenToWorld(position.x, position.y, this.width, this.height), body.getAngle());
    }

    public void setUpPhysicsBody(BodyType type) {
        if(body != null){
            world.destroyBody(body);
        }
        this.bodyDef.type = type;
        //bodyDef.fixedRotation = true;
        var width = ObjectManager.toMeters(this.width / 2f);
        var height = ObjectManager.toMeters(this.height / 2f);

        this.bodyDef.position.set(ObjectManager.screenToWorld(position.x, position.y, this.width, this.height));
        this.body = ObjectManager.world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(width,height);
        FixtureDef fixture =new FixtureDef();
        fixture.friction = 0.5f;
        fixture.density= 1f;
        fixture.shape = shape;
        this.body.setLinearDamping(2.0f);
        body.createFixture(fixture);
    }


    public void renderObject(float alpha) {

        if (this.isTextured) {
            RenderEngine.drawQuadTextureAngle(getInterpolatedPosition(alpha), -body.getAngle(), width, height, texture, baseColor);
        } else {
            RenderEngine.drawQuadAngleA(getInterpolatedPosition(alpha), -body.getAngle(), width, height, baseColor);
        }

    }

    public void renderObjectWireframe(float alpha) {
        drawWireframe();
        renderObject(alpha);

    }

    private void drawWireframe() {
        PolygonShape shape = (PolygonShape) body.getFixtureList().getShape();

        Vec2 position = body.getPosition(); // Center position of the body in Box2D world
        float angle = -body.getAngle(); // Rotation of the body in radians

        // Assuming the shape is a rectangle, get width and height (Box2D stores them as half-width and half-height)
        Vec2 size = shape.getVertex(1); // Top-right vertex relative to body position gives half-width and half-height
        float width = size.x * 2; // Full width
        float height = size.y * 2; // Full height
        Vector2 screenPos = worldToScreen(position); // Convert position
        float screenWidth = width * PIXELS_PER_METER; // Convert width
        float screenHeight = height * PIXELS_PER_METER; // Convert height

        RenderEngine.drawWireframe(screenPos, screenWidth, screenHeight, angle);

    }

    public void render(float alpha) {
        if (this.wireframe) {
            renderObjectWireframe(alpha);
        } else {
            renderObject(alpha);
        }
    }

}
