package nitrodragon.game;

import nitrodragon.entity.Entity;
import nitrodragon.render.*;
import nitrodragon.io.*;
import nitrodragon.world.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    private Main() {
        Window.setCallbacks();

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW failed to initialize.");
        }

        Window window = new Window();
        window.setSize(640, 480);
        window.setFullscreen(false);
        window.createWindow("Game");

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Camera camera = new Camera(window.getWidth(), window.getHeight());
        glEnable(GL_TEXTURE_2D);

        TileRenderer tiles = new TileRenderer();

        Entity.initModel();

        Shader shader = new Shader("shader");

        World world = new World("test_level");
        double frame_cap = 1.0 / 60.0;

        double frame_time = 0;

        double time = Timer.getTime();
        double unprocessed = 0;

        float frame = 0;

        while (!window.shouldClose()) {
            frame += 0.01;
            boolean can_render = false;
            double time_2 = Timer.getTime();
            double passed = time_2 - time;
            unprocessed += passed; // hasn't been processed yet
            frame_time += passed;
            time = time_2;
            shader.setUniform("time", frame);

            while (unprocessed >= frame_cap) {
                unprocessed -= frame_cap;
                can_render = true;

                if(window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(window.getWindow(), true);
                }

                world.update((float) frame_cap, window, camera);

                world.correctCamera(camera, window);

                window.update();
                if (frame_time >= 1.0) {
                    frame_time = 0;
                }
            }

            if (can_render) {
                glClear(GL_COLOR_BUFFER_BIT);
                world.render(tiles, shader, camera, window);

                window.swapBuffers();
            }

        }

        Entity.deleteAsset();

        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main();
    }
}
