package org.polyscape.rendering.shaders;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;
import org.polyscape.Profile;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @author Madmegsox1
 * @since 16/07/2023
 */

public abstract class Shader {
    protected int vertexShaderId, fragmentShaderId, programId;

    private boolean useFrag, useVertex;

    private String vertexFile, fragmentFile;
    public Shader(String vertexFile, String fragmentFile){
        this.vertexFile = Profile.Shaders.SHADER_LOCATION + vertexFile + "." + Profile.Shaders.SHADER_FILEFORMAT;
        this.fragmentFile = Profile.Shaders.SHADER_LOCATION + fragmentFile + "." + Profile.Shaders.SHADER_FILEFORMAT;
        useFrag = true;
        useVertex = true;
    }

    public Shader(String file, boolean vertex){
        if(vertex) {
            this.vertexFile = Profile.Shaders.SHADER_LOCATION + file + "." + Profile.Shaders.SHADER_FILEFORMAT;
            useVertex = true;
        }else{
            this.fragmentFile = Profile.Shaders.SHADER_LOCATION + file + "." + Profile.Shaders.SHADER_FILEFORMAT;
            useFrag = true;
        }
    }


    public abstract void bindAllAttributes();

    public void bindAttribute(int index, String location){
        GL20.glBindAttribLocation(programId, index, location);
    }

    public void create(){
        programId = GL20.glCreateProgram();
        if(useVertex) {
            vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
            GL20.glShaderSource(vertexShaderId, readFile(vertexFile));
            GL20.glCompileShader(vertexShaderId);


            if (GL20.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
                System.err.println("Vertex Shader failed to compile - " + GL20.glGetShaderInfoLog(vertexShaderId));
            }
        }

        if(useFrag) {

            fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
            GL20.glShaderSource(fragmentShaderId, readFile(fragmentFile));
            GL20.glCompileShader(fragmentShaderId);


            if (GL20.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
                System.err.println("Frag Shader failed to compile - " + GL20.glGetShaderInfoLog(fragmentShaderId));
            }
        }

        if(useVertex) {
            GL20.glAttachShader(programId, vertexShaderId);
        }

        if(useFrag) {
            GL20.glAttachShader(programId, fragmentShaderId);
        }

        GL20.glLinkProgram(programId); // Links Programme

        if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL20.GL_FALSE){
            System.err.println("Programme failed to link - "+ GL20.glGetProgramInfoLog(programId));
        }

        GL20.glValidateProgram(programId); // Validated Programme

        if(GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == GL20.GL_FALSE){
            System.err.println("Programme failed to validate - "+ GL20.glGetProgramInfoLog(programId));
        }
        if(useVertex){
            bindAllAttributes();
        }
        if(useFrag) {
            getAllUniforms();
        }
    }

    public void bind(){
        GL20.glUseProgram(programId);
    }

    public void unbind(){GL20.glUseProgram(0);}

    public void remove(){
        if(useFrag) {
            GL20.glDetachShader(programId, fragmentShaderId);
            GL20.glDeleteShader(fragmentShaderId);
        }
        if(useVertex) {
            GL20.glDetachShader(programId, vertexShaderId);
            GL20.glDeleteShader(vertexShaderId);
        }

        GL20.glDeleteProgram(programId);
    }

    protected abstract void getAllUniforms();

    protected int getUniform(String name){
        return GL20.glGetUniformLocation(programId, name);
    }

    protected void loadFloatUniform(int location, float value){
        GL20.glUniform1f(location, value);
    }

    protected void loadIntUniform(int location, int value){
        GL20.glUniform1i(location, value);
    }

    protected void loadVectorUniform(int location, Vector2 value){
        GL20.glUniform2f(location, value.x, value.y);
    }

    protected void loadColorUniform(int location, Color value){
        GL20.glUniform3f(location, value.r, value.g, value.b);
    }

    protected void loadColorAUniform(int location, Color value){
        GL20.glUniform4f(location, value.r, value.g, value.b, value.a);
    }

    protected void loadColorAUniform(int location, float[] value){
        GL20.glUniform4f(location, value[0], value[1], value[2], value[3]);
    }

    private String readFile(String fileLocation){
        BufferedReader bufferedReader = null;
        StringBuilder string = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(fileLocation));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                string.append(line).append("\n");
            }
        }catch (IOException e){
            System.err.println("File was not found");
        }
        return string.toString();
    }
}
