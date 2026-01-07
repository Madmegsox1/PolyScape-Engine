package org.polyscape.rendering.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.polyscape.Profile;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * @author Madmegsox1
 * @since 16/07/2023
 */

public abstract class Shader {
    protected int vertexShaderId, fragmentShaderId, programId;
    private boolean useFrag, useVertex;
    private String vertexFile, fragmentFile;

    public Shader(String vertexFile, String fragmentFile) {
        this.vertexFile = Profile.Shaders.SHADER_LOCATION + vertexFile + "." + Profile.Shaders.SHADER_FILEFORMAT;
        this.fragmentFile = Profile.Shaders.SHADER_LOCATION + fragmentFile + "." + Profile.Shaders.SHADER_FILEFORMAT;
        useFrag = true;
        useVertex = true;
    }

    public Shader(String file, boolean vertex) {
        if (vertex) {
            this.vertexFile = Profile.Shaders.SHADER_LOCATION + file + "." + Profile.Shaders.SHADER_FILEFORMAT;
            useVertex = true;
        } else {
            this.fragmentFile = Profile.Shaders.SHADER_LOCATION + file + "." + Profile.Shaders.SHADER_FILEFORMAT;
            useFrag = true;
        }
    }

    public abstract void bindAllAttributes();

    public void bindAttribute(int index, String location) {
        GL20.glBindAttribLocation(programId, index, location);
    }

    public void create() {
        programId = GL20.glCreateProgram();
        if (useVertex) {
            vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
            GL20.glShaderSource(vertexShaderId, readFile(vertexFile));
            GL20.glCompileShader(vertexShaderId);

            if (GL20.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
                System.err.println("Vertex Shader failed to compile - " + GL20.glGetShaderInfoLog(vertexShaderId));
            }
        }

        if (useFrag) {
            fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
            GL20.glShaderSource(fragmentShaderId, readFile(fragmentFile));
            GL20.glCompileShader(fragmentShaderId);

            if (GL20.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
                System.err.println("Frag Shader failed to compile - " + GL20.glGetShaderInfoLog(fragmentShaderId));
            }
        }

        if (useVertex) {
            GL20.glAttachShader(programId, vertexShaderId);
        }

        if (useFrag) {
            GL20.glAttachShader(programId, fragmentShaderId);
        }

        GL20.glLinkProgram(programId);

        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
            System.err.println("Program failed to link - " + GL20.glGetProgramInfoLog(programId));
        }

        GL20.glValidateProgram(programId);

        if (GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Program failed to validate - " + GL20.glGetProgramInfoLog(programId));
        }

        if (useVertex) {
            bindAllAttributes();
        }
        if (useFrag) {
            getAllUniforms();
        }
    }

    public void bind() {
        GL20.glUseProgram(programId);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void remove() {
        if (useFrag) {
            GL20.glDetachShader(programId, fragmentShaderId);
            GL20.glDeleteShader(fragmentShaderId);
        }
        if (useVertex) {
            GL20.glDetachShader(programId, vertexShaderId);
            GL20.glDeleteShader(vertexShaderId);
        }
        GL20.glDeleteProgram(programId);
    }

    protected abstract void getAllUniforms();

    protected int getUniform(String name) {
        return GL20.glGetUniformLocation(programId, name);
    }

    // Uniform Loaders

    public void setUniform(String name, float value) {
        GL20.glUniform1f(getUniform(name), value);
    }

    public void setUniform(String name, int value) {
        GL20.glUniform1i(getUniform(name), value);
    }

    public void setUniform(String name, boolean value) {
        GL20.glUniform1i(getUniform(name), value ? 1 : 0);
    }

    public void setUniform(String name, Vector2 vector) {
        GL20.glUniform2f(getUniform(name), vector.x, vector.y);
    }
//
//    public void setUniform(String name, Vector3f vector) {
//        GL20.glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
//    }

    public void setUniform(String name, Color color) {
        GL20.glUniform4f(
                getUniform(name),
                color.r / 255.0f,
                color.g / 255.0f,
                color.b / 255.0f,
                color.a / 255.0f
        );
    }

    public void setUniformMatrix(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            matrix.get(fb);
            GL20.glUniformMatrix4fv(getUniform(name), false, fb);
        }
    }

    private String readFile(String fileLocation) {
        StringBuilder string = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                string.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("File was not found");
        }
        return string.toString();
    }

    public void validate(int vaoId) {
        GL30.glBindVertexArray(vaoId); // Rebind VAO for validation
        GL20.glValidateProgram(programId); // Validate the shader program

        if (GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program validation failed: " + GL20.glGetProgramInfoLog(programId));
        }

        GL30.glBindVertexArray(0); // Unbind VAO after validation
    }

}
