package nitrodragon.entity;

import nitrodragon.io.Window;
import nitrodragon.render.*;
import nitrodragon.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    private static final int speed = 10;
    private static final int ANIM_IDLE = 0;
    private static final int ANIM_LEFT = 1;
    private static final int ANIM_RIGHT = 2;
    private static final int ANIM_UP = 3;
    private static final int ANIM_DOWN = 4;
    private static final int ANIM_SIZE = 5;
    private static byte anim = 0;

    public Player(Transform transform) {
        super(ANIM_SIZE, transform);
        setAnimation(ANIM_IDLE, new Animation(4, 8, "Ira/Idle"));
        setAnimation(ANIM_LEFT, new Animation(4, 8, "Ira/WalkLeft"));
        setAnimation(ANIM_RIGHT, new Animation(4, 8, "Ira/WalkRight"));
        setAnimation(ANIM_DOWN, new Animation(4, 8, "Ira/WalkDown"));
        setAnimation(ANIM_UP, new Animation(4, 8, "Ira/WalkUp"));
    }

    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        Vector2f movement = new Vector2f();
        if (window.getInput().isKeyDown(GLFW_KEY_LEFT)) {
            movement.add(-speed * delta, 0);
            anim = 1;
        }
        if (window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
            movement.add(speed * delta, 0);
            anim = 2;
        }
        if (window.getInput().isKeyDown(GLFW_KEY_UP)) {
            movement.add(0, speed * delta);
            anim = 3;
        }
        if (window.getInput().isKeyDown(GLFW_KEY_DOWN)) {
            movement.add(0, -speed * delta);
            anim = 4;
        }
        if (!window.getInput().isKeyDown(GLFW_KEY_LEFT) && !window.getInput().isKeyDown(GLFW_KEY_RIGHT) && !window.getInput().isKeyDown(GLFW_KEY_UP) && !window.getInput().isKeyDown(GLFW_KEY_DOWN)) {
            anim = 0;
        }
        move(movement);
        System.out.println(anim);

        // TODO
        // If movement == (speed * delta) useAnimation
        if (anim == 2) {
            useAnimation(ANIM_RIGHT);
        } else if (anim == 1) {
            useAnimation(ANIM_LEFT);
        } else if (anim == 4){
            useAnimation(ANIM_DOWN);
        } else if (anim == 3) {
            useAnimation(ANIM_UP);
        } else {
            useAnimation(ANIM_IDLE);
        }

        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
    }

}
