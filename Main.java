package com.matthewcairns.voxel;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

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

            glLoadIdentity();
            Display.update();
            Display.sync(50);
        }
        Display.destroy();
    }


    private void InitGL() {
        //Automatically removes faces not visible to camera.
        glEnable(GL_CULL_FACE);
        glShadeModel(GL_SMOOTH);
        glClearColor(0.529f, 0.807f, 0.921f, 0.0f);
        glClearDepth(1.0);
        glEnable(GL_DEPTH_TEST);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        float lightAmbient[] = { 2.0f, 2.0f, 2.0f, 2.0f };
        float lightDiffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };
        float lightPosition[] = { 0.0f, 1.0f, 1.0f, 0.0f };

        glLight(GL_LIGHT1, GL_AMBIENT, asFloatBuffer(lightAmbient));              // Setup The Ambient Light
        glLight(GL_LIGHT1, GL_DIFFUSE, asFloatBuffer(lightDiffuse));              // Setup The Diffuse Light
        glLight(GL_LIGHT1, GL_POSITION,asFloatBuffer(lightPosition));

        glEnable(GL_LIGHT1);
        glEnable ( GL_LIGHTING ) ;

        GLU.gluPerspective(
                67.0f, //FOV
                Constants.SCREEN_WIDTH/Constants.SCREEN_HEIGHT, //Aspect Ratio
                0.1f, //zNear
                10000.0f); //zFar

        glMatrixMode(GL_MODELVIEW);
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

