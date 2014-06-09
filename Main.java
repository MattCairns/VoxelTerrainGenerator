package com.matthewcairns.voxel;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {
    FPCameraController camera = new FPCameraController(0,0,0);
    Chunk chunk = new Chunk(0,0);
    Chunk chunk0 = new Chunk(1, 0);
    Chunk chunk1 = new Chunk(1, 1);
    float lastFrame = 0.0f;


    public Main(){
        try {
            Display.setDisplayMode(new DisplayMode(1280, 720));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        chunk.createBlock();
        chunk0.createBlock();
        chunk1.createBlock();

        InitGL();

        while(!Display.isCloseRequested()) {
            update();
            GL11.glLoadIdentity();

            Display.update();
            Display.sync(60);
        }

        Display.destroy();

    }

    private void update() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        controlCamera();


        chunk.drawChunk();
        chunk0.drawChunk();
        chunk1.drawChunk();


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

