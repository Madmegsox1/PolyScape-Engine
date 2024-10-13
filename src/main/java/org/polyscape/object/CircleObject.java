package org.polyscape.object;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

import static org.polyscape.object.ObjectManager.*;

public class CircleObject extends BaseObject {
    
    protected float radius;
    
    public void setRadius(float radius) {
        this.radius = radius;

        if (body != null) {
            var angle = Math.toDegrees(body.getAngle());
            setUpPhysicsBody();
            setAngle(angle);
        }
    }
    
    public float getRadius() {
        return radius;
    }
    
    public void addToRadius(float radius) {
        this.radius += radius;
    }
    
    @Override
    public void render(float alpha) {
        if(this.wireframe){
            drawWireframe();
        }
        if(isTextured) {
            RenderEngine.drawCircleAngleTextured(this.position, this.radius, -this.getAngle(), 360, this.getTexture());
        }
        else {
            RenderEngine.drawCircleAngle(this.position, this.radius, -this.getAngle(), 360, this.getBaseColor());
        }
        onRenderLogic();
    }

    @Override
    public void drawWireframe() {
        CircleShape shape = (CircleShape) body.getFixtureList().getShape();

        float radius = shape.getRadius() * PIXELS_PER_METER;
        Vector2 screenPos = worldToScreen(body.getPosition(), getLevel()); // Convert position

        RenderEngine.drawHollowCircle(screenPos, radius, 360, 2f, Color.GREEN);
    }

    @Override
    public boolean isPointInObject(Vector2 point) {
        float dx = point.x - position.x;
        float dy = point.y - position.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance <= radius;
    }

    @Override
    public int getWidth() {
        return (int) radius;
    }

    @Override
    public int getHeight(){
        return (int) radius;
    }

    @Override
    public Vector2 getCenter() {
        return this.position;
    }

    @Override
    public void updatePosition(){
        if (body == null) return;
        this.position = worldToScreen(body.getPosition(), onLevel);
        onUpdatePosLogic();
    }

    @Override
    public void setAngle(double angle) {
        if (body != null) {
            body.setTransform(ObjectManager.screenToWorld(position.x, position.y, radius, onLevel), (float) Math.toRadians(angle));
        }
    }


    @Override
    public void addToPos(float x, float y) {
        position.addToVect(x, y);
        body.setTransform(ObjectManager.screenToWorld(position.x, position.y, radius, onLevel), body.getAngle());
    }

    @Override
    public void setPosition(Vector2 position) {
        this.position = position;
        if (body != null) {
            body.setTransform(ObjectManager.screenToWorld(position.x, position.y, radius, onLevel), body.getAngle());
        }
    }

    @Override
    public void setUpPhysicsBody(BodyType type, float friction, float density, float linearDamping, boolean angleCals) {
        if (body != null) {
            world.destroyBody(body);
        }
        this.bodyDef.type = type;
        bodyDef.fixedRotation = angleCals;
        var radius = ObjectManager.toMeters(this.radius);

        this.bodyDef.position.set(ObjectManager.screenToWorld(getPosition().x, getPosition().y, this.radius, getLevel()));
        this.body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        FixtureDef fixture = new FixtureDef();
        fixture.friction = friction;
        fixture.density = density;
        fixture.shape = circleShape;
        this.body.setLinearDamping(linearDamping);
        body.createFixture(fixture);
    }
}
