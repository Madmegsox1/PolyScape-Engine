package org.polyscape.object;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.polyscape.rendering.elements.Vector2;

import java.util.Arrays;

public abstract class BindingPoly {
    public Vector2[] bindingPoints;

    public BindingPoly(Vector2[] bindingPoints) {
        this.bindingPoints = bindingPoints;
    }

    public PolygonShape getPolygon() {
        PolygonShape shape = new PolygonShape();
        Vec2[] vec2 = Arrays.stream(bindingPoints).map(Vector2::toVec2).toArray(Vec2[]::new);
        shape.set(vec2, bindingPoints.length);
        return shape;
    }

}
