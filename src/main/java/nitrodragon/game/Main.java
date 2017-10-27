package nitrodragon.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import nitrodragon.entity.Entity;
import nitrodragon.entity.Player;
import nitrodragon.entity.Transform;
import nitrodragon.render.*;
import nitrodragon.io.*;
import nitrodragon.world.*;
import org.lwjgl.opengl.GL;

public class Main {
    public Main() {
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
        int frames = 0;

        double time = Timer.getTime();
        double unprocessed = 0;

        while (!window.shouldClose()) {
            boolean can_render = false;
            double time_2 = Timer.getTime();
            double passed = time_2 - time;
            unprocessed += passed; // hasn't been processed yet
            frame_time += passed;
            time = time_2;

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
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }

            if (can_render) {
                glClear(GL_COLOR_BUFFER_BIT);

                /*shader.bind();
                shader.setUniform("sampler", 0);
                shader.setUniform("projection", camera.getProjection().mul(target));
                model.render();
                tex.bind(0);*/

                world.render(tiles, shader, camera, window);

                window.swapBuffers();
                frames++;
            }

        }

        Entity.deleteAsset();

        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main();
    }
}
