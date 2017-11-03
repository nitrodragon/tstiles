#version 120

uniform sampler2D sampler;
uniform vec3 tint;
uniform float time;
varying vec2 tex_coords;

void main() {
    vec4 outt = texture2D(sampler, tex_coords) * vec4(tint, 1.0);
//    outt *= vec4(sin(time / 10));
    gl_FragColor = outt;
}