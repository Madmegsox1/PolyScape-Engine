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

    private BodyType bodyType;

    private float friction;

    private float density;

    private float linearDamping;

    private boolean angleCals;

    private int onLevel;


    public BaseObject() {
        bodyDef = new BodyDef();
        friction = 0.5f;
        density = 1f;
        linearDamping = 2f;
        bodyType = BodyType.STATIC;
        angleCals = false;
        onLevel = -1;
    }


    public int getLevel(){
        return this.onLevel;
    }

    public void setLevel(int level){
        this.onLevel = level;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public float getAngle() {
        return (float) Math.toDegrees(body.getAngle());
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        if (body != null) {
            body.setTransform(ObjectManager.screenToWorld(position.x, position.y, this.width, this.height), body.getAngle());
        }
    }

    public void setAngle(double angle) {
        if (body != null) {
            body.setTransform(ObjectManager.screenToWorld(position.x, position.y, this.width, this.height), (float) Math.toRadians(angle));
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

        if (body != null) {
            var angle = Math.toDegrees(body.getAngle());
            setUpPhysicsBody();
            setAngle(angle);
        }
    }

    public void setHeight(int height) {
        this.height = height;
        if (body != null) {
            var angle = Math.toDegrees(body.getAngle());
            setUpPhysicsBody();
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
        float speedX = RenderEngine.fps * v.x;
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

    public void removeBody() {
        ObjectManager.world.destroyBody(this.body);
    }

    public boolean isPointInObject(Vector2 point) {
        Vec2 toWoldPoint = ObjectManager.screenToWorld(point.x, point.y, 1, 1);

        if (body == null) return false;

        for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
            if (fixture.testPoint(toWoldPoint)) {
                return true;
            }
        }
        return false;
    }

    public void setPreviousPosition() {
        if (position == null) return;
        this.previousPosition = new Vector2(position.x, position.y);
        ;
    }


    public void updatePosition() {
        if (body == null) return;
        position = worldToScreen(body.getPosition());
        position.x -= (this.width / 2f);
        position.y -= (this.height / 2f);
    }


    public Vector2 getInterpolatedPosition(float alpha) {
        if (previousPosition == null) return position;
        float interpX = previousPosition.x + (position.x - previousPosition.x) * alpha;
        float interpY = previousPosition.y + (position.y - previousPosition.y) * alpha;
        return new Vector2(interpX, interpY);
    }

    public void addToPos(int x, int y) {
        position.addToVect(x, y);
        //ObjectManager.world.destroyBody(this.body);
        //setUpPhysicsBody(this.bodyDef.type);
        body.setTransform(ObjectManager.screenToWorld(position.x, position.y, this.width, this.height), body.getAngle());
    }

    public void setUpPhysicsBody(BodyType type, float friction, float density, float linearDamping, boolean angleCals) {
        if (body != null) {
            world.destroyBody(body);
        }
        this.bodyDef.type = type;
        bodyDef.fixedRotation = angleCals;
        var width = ObjectManager.toMeters(this.width / 2f);
        var height = ObjectManager.toMeters(this.height / 2f);

        this.bodyDef.position.set(ObjectManager.screenToWorld(position.x, position.y, this.width, this.height));
        this.body = ObjectManager.world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(width, height);
        FixtureDef fixture = new FixtureDef();
        fixture.friction = friction;
        fixture.density = density;
        fixture.shape = shape;
        this.body.setLinearDamping(linearDamping);
        body.createFixture(fixture);
    }


    public void setUpPhysicsBody() {

        setUpPhysicsBody(bodyType, friction, density, linearDamping, angleCals);

    }


    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction, boolean recreate) {
        this.friction = friction;
        if (recreate) {
            setUpPhysicsBody(bodyType, friction, density, linearDamping, angleCals);
        }
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density, boolean recreate) {
        this.density = density;
        if (recreate) {
            setUpPhysicsBody(bodyType, friction, density, linearDamping, angleCals);
        }
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping, boolean recreate) {
        this.linearDamping = linearDamping;
        if (recreate) {
            setUpPhysicsBody(bodyType, friction, density, linearDamping, angleCals);
        }
    }

    public boolean isAngleCals() {
        return angleCals;
    }

    public void setAngleCals(boolean angleCals, boolean recreate) {
        this.angleCals = angleCals;
        if (recreate) {
            setUpPhysicsBody(bodyType, friction, density, linearDamping, angleCals);
        }
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType, boolean recreate) {
        this.bodyType = bodyType;
        if (recreate) {
            setUpPhysicsBody(bodyType, friction, density, linearDamping, angleCals);
        }
    }

    public void renderObject(float alpha) {
        if(body != null) {
            if (this.isTextured) {
                RenderEngine.drawQuadTextureAngle(getInterpolatedPosition(alpha), -body.getAngle(), width, height, texture, baseColor);
            } else {
                RenderEngine.drawQuadAngleA(getInterpolatedPosition(alpha), -body.getAngle(), width, height, baseColor);
            }
        }
        else{
            if (this.isTextured) {
                RenderEngine.drawQuadTextureAngle(position, 0, width, height, texture, baseColor);
            } else {
                RenderEngine.drawQuadAngleA(position, 0, width, height, baseColor);
            }
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
