#version 330 core

layout(location = 0) in vec2 aPos;         // Position attribute
layout(location = 1) in vec2 aTexCoord;     // Texture coordinate attribute

uniform mat4 projection;                    // Projection matrix
uniform mat4 transform;                     // Transformation matrix for position, rotation, scaling

out vec2 TexCoord;                          // Pass texture coordinates to the fragment shader

void main() {
    // Apply projection and transformation matrices
    gl_Position = projection * transform * vec4(aPos, 0.0, 1.0);
    TexCoord = aTexCoord;  // Pass texture coordinate to fragment shader
}