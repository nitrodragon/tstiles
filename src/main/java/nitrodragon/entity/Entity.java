package nitrodragon.entity;

import nitrodragon.collision.AABB;
import nitrodragon.collision.Collision;
import nitrodragon.io.Window;
import nitrodragon.render.Animation;
import nitrodragon.render.Camera;
import nitrodragon.render.Model;
import nitrodragon.render.Shader;
import nitrodragon.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Entity {
    private static Model model;
    protected AABB hitbox;
    private Animation[] animations;
    private int use_animation;
    protected Vector3f tint;
    Transform transform;


    Entity(int max_animations, Transform transform) {
        this.animations = new Animation[max_animations];
        this.tint = new Vector3f(1, 1, 1);
        this.transform = transform;
        this.use_animation = 0;

        hitbox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
    }

    void setAnimation(int index, Animation animation) {
        animations[index] = animation;
    }

    void useAnimation(int index) {
        this.use_animation = index;
    }

    void move(Vector2f direction) {
        transform.pos.add(new Vector3f(direction, 0));

        hitbox.getCenter().set(transform.pos.x, transform.pos.y);
    }

    public void collideWithTiles(World world) {
        AABB[] boxes = new AABB[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i + j * 5] = world.getTileBoundingBox(
                        (int) (((transform.pos.x / 2) + 0.5f) - (5/2)) + i,
                        (int) (((-transform.pos.y / 2) + 0.5f) - (5/2)) + j
                );
            }
        }

        AABB box = null;
        for (AABB selected : boxes) {
            if (selected != null) {
                if (box == null) {
                    box = selected;
                }
                Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 = selected.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                // Don't want to waste CPU power to find length's sqrt
                if (length1.lengthSquared() > length2.lengthSquared()) {
                    box = selected;
                }
            }
        }
        if (box != null) {
            Collision data = hitbox.getCollision(box);
            if (data.isIntersecting) {
                hitbox.correctPosition(box, data);
                transform.pos.set(hitbox.getCenter(), 0);
            }

            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i] != null) {
                    if (box == null) {
                        box = boxes[i];
                    }
                    Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                    if(length1.lengthSquared() > length2.lengthSquared()) {
                        box = boxes[i];
                    }
                }
            }
            if (box != null) {
                data = hitbox.getCollision(box);
                if (data.isIntersecting) {
                    hitbox.correctPosition(box, data);
                    transform.pos.set(hitbox.getCenter(), 0);
                }
            }
        }
    }

    public abstract void update(float delta, Window window, Camera camera, World world);

    public void render(Shader shader, Camera camera, World world) {
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix());

        shader.bind();
        shader.setUniform("tint", tint);
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(target));
        animations[use_animation].bind(0);
        model.render();
    }

    public static void initModel() {
        float[] vertices = new float[] {
                -1f, 1f,  0,
                1f, 1f,  0,
                1f, -1f, 0,
                -1f, -1f, 0,
        };

        float[] texture = new float[] {
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };

        int[] indices = new int[] {
                0, 1, 2,
                2, 3, 0
        };
        model = new Model(vertices, texture, indices);
    }
    public static void deleteAsset() {
        model = null;
    }
    public void collideWithEntity(Entity entity) {
        Collision collision = hitbox.getCollision(entity.hitbox);

        if (collision.isIntersecting) {
            collision.distance.x /= 2;
            collision.distance.y /= 2;

            hitbox.correctPosition(entity.hitbox, collision);
            transform.pos.set(hitbox.getCenter().x, hitbox.getCenter().y, 0);

            entity.hitbox.correctPosition(hitbox, collision);
            entity.transform.pos.set(entity.hitbox.getCenter().x, entity.hitbox.getCenter().y, 0);
        }
    }

    public void setTint(Vector3f tint) {
        this.tint = tint;
    }
}
