package org.polyscape.object;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class FluidObject extends BaseObject {

    ArrayList<FluidParticle> particles = new ArrayList<>();

    float radius;

    public FluidObject(float radius){
        this.radius = radius;
    }

    public void createFluid( float startX, float startY, int count) {
        for (int i = 0; i < count; i++) {
            // Randomly position particles around the start position
            float x = startX + (float)(Math.random() - 0.5) * this.radius * 2; // Spread particles around startX
            float y = startY + (float)(Math.random() - 0.5) * this.radius * 2; // Spread particles around startY
            particles.add(new FluidParticle(x, y, radius));
        }
    }



    @Override
    public void render(float alpha) {


        for (FluidParticle particle : particles){
            glBegin(GL_POLYGON);
            glColor3f(0.0f, 0.5f, 1.0f);
            Vec2 pos = particle.body.getPosition();
            Vector2 screenPos = ObjectManager.worldToScreen(pos, getLevel());
            glVertex2f(screenPos.x, screenPos.y);
            glVertex2f(screenPos.x + this.radius, screenPos.y);
            glVertex2f(screenPos.x + this.radius, screenPos.y+ this.radius);
            glVertex2f(screenPos.x, screenPos.y+ this.radius);
            glEnd();
        }

    }

    public static class FluidParticle {
        public Body body;

        public FluidParticle(float x, float y, float radius) {
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            //bd.position.set(ObjectManager.screenToWorld(x, y, radius*2, radius*2));

            CircleShape cs = new CircleShape();
            cs.m_radius = ObjectManager.toMeters(radius / 2f);

            FixtureDef fd = new FixtureDef();
            fd.shape = cs;
            fd.density = 1.0f;
            fd.friction = 0.0f; // Low friction
            fd.restitution = 0.3f; // Some restitution to simulate bounciness of water
            // Adjust these properties to simulate fluid viscosity or density
            //fd.filter.groupIndex = 0; // To ensure particles don't collide with each other
            //fd.isSensor = true;

            this.body = ObjectManager.world.createBody(bd);
            this.body.createFixture(fd);
            this.body.setLinearDamping(2.0f); // Damping to slow down particles over time, simulating fluid resistance
            this.body.setUserData(this); // Optional: for identifying the body in collision callbacks
        }
    }

}


