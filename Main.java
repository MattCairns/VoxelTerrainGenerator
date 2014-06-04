package com.matthewcairns.voxel;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {
    public Main(){
        try {
            Display.setDisplayMode(new DisplayMode(1280, 720));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        InitGL();

        while(!Display.isCloseRequested()) {
            update();
            Display.update();
            Display.sync(60);
        }

        Display.destroy();

    }

    private void update() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glLoadIdentity();

        Block.multiCreateBlocks(5);


    }

    private void InitGL() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GLU.gluPerspective(
                67.0f, //FOV
                1280.0f/720.0f, //Aspect Ratio
                0.1f, //zNear
                1000.0f); //zFar

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }

    public static void main(String[] args){
        new Main();
    }
}

