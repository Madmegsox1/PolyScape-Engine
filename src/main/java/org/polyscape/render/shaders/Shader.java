package org.polyscape.render.shaders;

import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Madmegsox1
 * @since 10/07/2023
 */

public abstract class Shader {
    private int vertexShaderId, fragmentShaderId, programId;

    private final String vertexFile, fragmentFile;
    public Shader(String vertexFile, String fragmentFile){
        this.vertexFile = vertexFile;
        this.fragmentFile = fragmentFile;
    }

    public abstract void bindAllAttributes();

    public void bindAttribute(int index, String location){
        GL20.glBindAttribLocation(programId, index, location);

    }

    public void create(){
        programId = GL20.glCreateProgram();
        vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShaderId, readFile(vertexFile));
        GL20.glCompileShader(vertexShaderId);


        if(GL20.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE){
            System.err.println("Vertex Shader failed to compile - "+ GL20.glGetShaderInfoLog(vertexShaderId));
        }

        fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShaderId, readFile(fragmentFile));
        GL20.glCompileShader(fragmentShaderId);


        if(GL20.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE){
            System.err.println("Frag Shader failed to compile - "+ GL20.glGetShaderInfoLog(fragmentShaderId));
        }

        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);

        GL20.glLinkProgram(programId); // Links Programme

        if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL20.GL_FALSE){
            System.err.println("Programme failed to link - "+ GL20.glGetProgramInfoLog(programId));
        }

        GL20.glValidateProgram(programId); // Validated Programme

        if(GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == GL20.GL_FALSE){
            System.err.println("Programme failed to validate - "+ GL20.glGetProgramInfoLog(programId));
        }
    }

    public void bind(){
        GL20.glUseProgram(programId);
    }

    public void remove(){
        GL20.glDetachShader(programId, fragmentShaderId);
        GL20.glDetachShader(programId, vertexShaderId);

        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);

        GL20.glDeleteProgram(programId);
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
