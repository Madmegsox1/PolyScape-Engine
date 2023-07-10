package org.polyscape.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.polyscape.render.model.Model;

/**
 * @author Madmegsox1
 * @since 09/07/2023
 */

public class Renderer {
    public void renderModel(Model model){
        GL30.glBindVertexArray(model.getVertexArrayId());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
