package nitrodragon.entity;

import nitrodragon.collision.AABB;
import nitrodragon.collision.Collision;
import nitrodragon.io.Window;
import nitrodragon.render.*;
import nitrodragon.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    private static final int speed = 10;
    public static final int ANIM_IDLE = 0;
    public static final int ANIM_WALKING = 1;
//    public static final int ANIM_LEFT = 2;
//    public static final int ANIM_RIGHT = 3;
//    public static final int ANIM_UP = 4;
//    public static final int ANIM_DOWN = 5;
    public static final int ANIM_SIZE = 2; // 6
    public Player(Transform transform) {
        super(ANIM_SIZE, transform);
        setAnimation(ANIM_IDLE, new Animation(1, 2, "player/idle"));
        setAnimation(ANIM_WALKING, new Animation(4, 2, "player/walking"));
//        setAnimation(ANIM_LEFT, new Animation(1, 2, "Ira/WalkLeft"));
//        setAnimation(ANIM_RIGHT, new Animation(4, 2, "Ira/WalkRight"));
//        setAnimation(ANIM_DOWN, new Animation(1, 2, "Ira/WalkDown"));
//        setAnimation(ANIM_UP, new Animation(4, 2, "Ira/WalkUp"));
    }

    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        Vector2f movement = new Vector2f();
        if (window.getInput().isKeyDown(GLFW_KEY_LEFT)) {
            movement.add(-speed * delta, 0);
        }
        if (window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
            movement.add(speed * delta, 0);
        }
        if (window.getInput().isKeyDown(GLFW_KEY_UP)) {
            movement.add(0, speed * delta);
        }
        if (window.getInput().isKeyDown(GLFW_KEY_DOWN)) {
            movement.add(0, -speed * delta);
        }
        move(movement);

        // TODO
        // If movement == (speed * delta) useAnimation
        if(movement.x != 0 || movement.y != 0) {
            useAnimation(ANIM_WALKING);
        } else {
            useAnimation(ANIM_IDLE);
        }

        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
    }

}
