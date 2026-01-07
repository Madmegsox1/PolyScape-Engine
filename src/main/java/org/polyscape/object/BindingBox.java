package org.polyscape.object;

import org.jbox2d.collision.shapes.PolygonShape;
import org.polyscape.rendering.elements.Vector2;

public class BindingBox extends BindingPoly{
    public BindingBox(float width, float height) {
        super(new Vector2[] {
                new Vector2(ObjectManager.toMeters(width/2), ObjectManager.toMeters(height/2))
        });
    }

    @Override
    public PolygonShape getPolygon() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bindingPoints[0].x, bindingPoints[0].y);
        return shape;
    }
}
