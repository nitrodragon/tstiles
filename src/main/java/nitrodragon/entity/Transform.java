package nitrodragon.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform { // Handles size and position relative to size
    public Vector3f pos;
    public Vector3f scale;

    public Transform() {
        pos = new Vector3f();
        scale = new Vector3f(1, 1, 1);
    }

    public Matrix4f getProjection(Matrix4f target) {
        // Translate, THEN scale.
        target.translate(pos);
        target.scale(scale);
        return target;
    }
}
