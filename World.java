package com.matthewcairns.voxel;

import com.matthewcairns.voxel.Chunks.ChunkManager;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/**
 * Created by Matthew Cairns on 13/06/2014.
 * All rights reserved.
 */
public class World {
    private FPCameraController camera = new FPCameraController(0,30,0);

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
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        controlCamera();
        cm.update(camera.getPosition());
    }


    private void overlayUI() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 1280, 720, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glDisable(GL_DEPTH_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);



        swap3d();
    }

    private void swap3d() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(
                67.0f, //FOV
                Constants.SCREEN_WIDTH / Constants.SCREEN_HEIGHT, //Aspect Ratio
                0.1f, //zNear
                10000.0f); //zFar
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);

    }

    private void controlCamera() {
        float dt = getDeltaTime();

        float dx = Mouse.getDX();
        float dy = Mouse.getDY();

        float mouseSpeed = 0.5f;
        float movementSpeed = 10.0f;

        camera.yaw(dx*mouseSpeed);
        camera.pitch(dy*mouseSpeed);

        if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
        {
            camera.walkForward(movementSpeed);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
        {
            camera.walkBackwards(movementSpeed);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left
        {
            camera.strafeLeft(movementSpeed);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right
        {
            camera.strafeRight(movementSpeed);
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
