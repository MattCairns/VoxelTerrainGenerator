package com.matthewcairns.voxel;

import com.matthewcairns.voxel.Chunks.ChunkManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;

public class Main {
    public Main(){
        try {
            Display.setDisplayMode(new DisplayMode((int)Constants.SCREEN_WIDTH, (int)Constants.SCREEN_HEIGHT));
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
        //Automatically removes faces not visible to camera.
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.529f, 0.807f, 0.921f, 0.0f);
        GL11.glClearDepth(1.0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GLU.gluPerspective(
                67.0f, //FOV
                Constants.SCREEN_WIDTH/Constants.SCREEN_HEIGHT, //Aspect Ratio
                0.1f, //zNear
                10000.0f); //zFar

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    private static FloatBuffer asFloatBuffer(float[] values) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;
    }

    public static void main(String[] args){
        new Main();
    }
}

