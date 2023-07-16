package org.polyscape.rendering.graphing;


import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.polyscape.font.Font;
import org.polyscape.Engine;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;


public final class Graph {

    public ArrayList<Vector2> graphData;
    public Vector2 pos;

    public Vector2 maxAxis;

    public Vector2 minAxis;

    public float mx = 2;

    public float my = 2;

    public float mv = 1;

    public int interval = 1;

    public boolean dots = true;

    public boolean lines = true;

    public boolean xAxis = true;

    public boolean yAxis = true;

    public Color dotColor = Color.BLUE;

    public Color lineColor = Color.BLACK;

    public String title = null;

    public Font font = new Font(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10), true);

    public boolean toolTip = true;

    public Vector2 titleVector;


    public Graph(float maxX, float maxY, float minX, float minY, Vector2 pos) {
        this.graphData = new ArrayList<>();
        this.maxAxis = new Vector2(maxX, maxY);
        this.minAxis = new Vector2(minX, minY);
        this.pos = pos;
    }

    public Graph(ArrayList<Vector2> graphData, Vector2 pos) {
        this.graphData = graphData;

        float mX = 0;
        float mY = 0;

        float miX = Float.MAX_VALUE;
        float miY = Float.MAX_VALUE;

        for (Vector2 v : graphData) {
            if (v.x > mX) {
                mX = v.x;
            }

            if (v.y > mY) {
                mY = v.y;
            }
        }

        for (Vector2 v : graphData) {
            if (v.x < miX) {
                miX = v.x;
            }

            if (v.y < miY) {
                miY = v.y;
            }
        }

        maxAxis = new Vector2(mX, mY);
        minAxis = new Vector2(miX, miY);

        this.pos = pos;
    }


    public void moveLeft() {
        if (graphData.size() > 0) {
            graphData.remove(0);

            for (Vector2 graphDatum : graphData) {
                graphDatum.x--;
            }
        }
    }

    public void moveRight() {
        if (graphData.size() > 0) {
            graphData.remove(graphData.size() - 1);

            for (Vector2 graphDatum : graphData) {
                graphDatum.x++;
            }
        }
    }

    public void addLast(Vector2 v) {
        graphData.add(v);
    }

    public void addFirst(Vector2 v) {
        graphData.add(0, v);
    }


    public void scrollLeft(Vector2 v) {
        if (graphData.size() >= maxAxis.x) {
            moveLeft();
        }
        v.x = graphData.size() - 1;

        addLast(v);
    }


    public void mouseEvent() {
        if(!toolTip) return;

        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(Engine.getDisplay().getWindow(), x, y);

        Vector2 vector2 = new Vector2(x.get(0), y.get(0));

        x.clear();
        y.clear();


        for (Vector2 v : graphData) {
            Vector2 v2 = new Vector2(pos.x + ((v.x * mv) * mx), pos.y - ((v.y * mv) * my));
            if (((vector2.y <= v2.y && vector2.y + 10 >= v2.y) || (vector2.y >= v2.y && vector2.y - 10 <= v2.y)) && ((vector2.x <= v2.x && vector2.x + 10 >= v2.x) || (vector2.x >= v2.x && vector2.x - 10 <= v2.x))) {
                RenderEngine.drawQuad(new Vector2(v2.x, v2.y - 14), font.getWidth(v.x + ", " + v.y) + 2, 10, Color.BLACK);
                font.drawText(v.x + ", " + v.y, new Vector2(v2.x, v2.y - 16), Color.WHITE);
                break;
            }

        }
    }


    public void renderGraph() {


        if (dots) {
            final float[] c = Color.convertColorToFloatAlpha(Color.BLUE);
            glColor4f(c[0], c[1], c[2], c[3]);
            glPointSize(4f);
            glEnable(GL_POINT_SMOOTH);
            glBegin(GL_POINTS);

            for (Vector2 v : graphData) {
                glVertex2f(pos.x + ((v.x * mv) * mx), pos.y - ((v.y * mv) * my));
            }


            glEnd();
            glDisable(GL_POINT_SMOOTH);
        }

        if (lines) {
            final float[] c2 = Color.convertColorToFloatAlpha(Color.BLACK);
            glColor4f(c2[0], c2[1], c2[2], c2[3]);

            glLineWidth(1f);
            glEnable(GL_LINE_SMOOTH);
            glBegin(GL_LINE_STRIP);

            for (Vector2 v : graphData) {
                glVertex2f(pos.x + ((v.x * mv) * mx), pos.y - ((v.y * mv) * my));
            }

            glEnd();
            glDisable(GL_LINE_SMOOTH);
        }


        if (xAxis) {
            for (float x = minAxis.x; x < maxAxis.x + 1; x += interval) {
                font.drawText(String.valueOf((int) x), new Vector2(pos.x + ((x * mv) * mx) - 6, pos.y + 20), Color.BLACK);
            }
        }


        if (yAxis) {
            for (float y = minAxis.y; y < maxAxis.y + 1; y += interval) {
                font.drawText(String.valueOf((int) y), new Vector2(pos.x - 20, pos.y - ((y * mv) * my) - 6), Color.BLACK);
            }
        }

        RenderEngine.drawLine(pos, new Vector2(pos.x + ((maxAxis.x * mv) * mx), pos.y), 2f, Color.BLACK);
        RenderEngine.drawLine(pos, new Vector2(pos.x, pos.y - ((maxAxis.y * mv) * my)), 2f, Color.BLACK);

        if (title != null) {
            titleVector = new Vector2(pos.x + (((maxAxis.x * mv) * mx) / 2), pos.y - ((maxAxis.y * mv) * my));
            font.drawText(title, titleVector, Color.BLACK);
        }

        mouseEvent();
    }


    public boolean isToolTip() {
        return toolTip;
    }

    public void setToolTip(boolean toolTip) {
        this.toolTip = toolTip;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getMx() {
        return mx;
    }

    public void setMx(float mx) {
        this.mx = mx;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isDots() {
        return dots;
    }

    public void setDots(boolean dots) {
        this.dots = dots;
    }

    public boolean isLines() {
        return lines;
    }

    public void setLines(boolean lines) {
        this.lines = lines;
    }

    public boolean isxAxis() {
        return xAxis;
    }

    public void setxAxis(boolean xAxis) {
        this.xAxis = xAxis;
    }

    public boolean isyAxis() {
        return yAxis;
    }

    public void setyAxis(boolean yAxis) {
        this.yAxis = yAxis;
    }

    public Color getDotColor() {
        return dotColor;
    }

    public void setDotColor(Color dotColor) {
        this.dotColor = dotColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
}
