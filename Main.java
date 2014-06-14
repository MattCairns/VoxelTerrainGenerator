package com.matthewcairns.voxel;

import com.matthewcairns.voxel.Chunks.ChunkManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {
    public Main(){
        try {
            Display.setDisplayMode(new DisplayMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        InitGL();
        World world = new World();

        while(!Display.isCloseRequested()) {
            world.update();

            GL11.glLoadIdentity();
            Display.update();
            //Display.sync(30);
        }
        Display.destroy();
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
                Constants.SCREEN_WIDTH/Constants.SCREEN_HEIGHT, //Aspect Ratio
                0.1f, //zNear
                10000.0f); //zFar

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }

    public static void main(String[] args){
        new Main();
    }
}

