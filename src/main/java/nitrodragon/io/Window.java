package nitrodragon.io;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;

    private int width, height = 0;
    private boolean fullscreen;

    private Input input;

    public static void setCallbacks() {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
    }

    public Window() {
        setSize(640, 480);
        setFullscreen(false);
    }

    public void createWindow(String title) {
        window = glfwCreateWindow(
                width,
                height,
                title,
                fullscreen ? glfwGetPrimaryMonitor() : 0,
                0);

        if (window == 0) {
            throw new IllegalStateException("GLFW failed to create window!");
        }
        if (!fullscreen) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vid.width() - width) / 2, (vid.height() - height) / 2);

            glfwShowWindow(window);
        }
        glfwMakeContextCurrent(window);

        input = new Input(window);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void update() {
        input.update();
        glfwPollEvents();
    }

    public boolean isFullscreen() { return fullscreen; }
    public long getWindow() { return window; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Input getInput() { return input; }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }
}
