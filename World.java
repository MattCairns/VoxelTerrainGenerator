package com.matthewcairns.voxel;

import com.matthewcairns.voxel.Chunks.ChunkManager;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * Created by Matthew Cairns on 13/06/2014.
 * All rights reserved.
 */
public class World {
    private FPCameraController camera = new FPCameraController(100,200,100);

    float lastFrame = 0.0f;
    int fps, savedFPS;
    long lastFPS = getTime();

    private HUD hud;
    private ChunkManager cm;


    public World() {
        hud = new HUD("Helvetica", 12);

        cm = new ChunkManager();
        cm.loadChunks();
    }

    public void update() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        swap2D();
        updateFPS();
        hud.drawFont(100, 25, "chunk_count: " + cm.getNumberOfChunks());
        hud.drawFont(100, 10, "block_count: " + cm.getNumberOfChunks()*(16*16));

        swap3d();
        controlCamera();
        cm.update(camera.getPosition());
    }

    private void swap2D() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glOrtho(0, 1280, 720, 0, 1, -1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void swap3d() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(
                67.0f, //FOV
                1280.0f / 720.0f, //Aspect Ratio
                0.1f, //zNear
                10000.0f); //zFar
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    private void controlCamera() {
        float dt = getDeltaTime();

        float dx = Mouse.getDX();
        float dy = Mouse.getDY();

        float mouseSpeed = 0.5f;
        float movementSpeed = 0.5f;

        camera.yaw(dx*mouseSpeed);
        camera.pitch(dy*mouseSpeed);

        if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
        {
            camera.walkForward(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
        {
            camera.walkBackwards(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left
        {
            camera.strafeLeft(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right
        {
            camera.strafeRight(movementSpeed*dt);
        }

        camera.lookThrough();
    }

    private void updateFPS() {
        if(getTime() - lastFPS > 1000) {
            hud.drawFont(100, 50, "fps: " + fps);
            savedFPS = fps;
            fps = 0;
            lastFPS += 1000;
        }
        hud.drawFont(100, 50, "fps: " + savedFPS);
        fps++;
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private float getDeltaTime() {
        long time = getTime();
        float dt = (time - lastFrame);
        lastFrame = time;

        return dt;
    }

}
