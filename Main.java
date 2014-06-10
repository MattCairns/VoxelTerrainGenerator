package com.matthewcairns.voxel;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;

public class Main {
    FPCameraController camera = new FPCameraController(0,0,0);

    float lastFrame = 0.0f;
    int fps;
    int savedFPS;
    long lastFPS = getTime();

    private HUD hud;
    private ChunkManager cm;

    public Main(){
        try {
            Display.setDisplayMode(new DisplayMode(1280, 720));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }



        InitGL();

        hud = new HUD("Helvetica", 12);

        cm = new ChunkManager(7);
        cm.loadChunks();

        while(!Display.isCloseRequested()) {
            update();
            GL11.glLoadIdentity();

            Display.update();
            //Display.sync(60);
        }

        Display.destroy();

    }

    private void update() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        swap2D();
        updateFPS();

        swap3d();
        controlCamera();
        cm.renderChunks();

    }

    private void InitGL() {
        //Enable Vertex Buffer Object rendering (VBO)
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        //Automatically removes faces not visible to camera.
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.529f, 0.807f, 0.921f, 0.0f);
        GL11.glClearDepth(1.0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GLU.gluPerspective(
                67.0f, //FOV
                1280.0f/720.0f, //Aspect Ratio
                0.1f, //zNear
                10000.0f); //zFar

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
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
        float movementSpeed = 0.05f;



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
            hud.drawFont(100, 50, "FPS: " + fps);
            savedFPS = fps;
            System.out.println(fps);
            fps = 0;
            lastFPS += 1000;
        }
        hud.drawFont(100, 50, "FPS: " + savedFPS);
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

    public static void main(String[] args){
        new Main();
    }
}

