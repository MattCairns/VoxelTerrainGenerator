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

            GL11.glLoadIdentity();
            Display.update();
            Display.sync(50);
        }
        Display.destroy();
    }


    private void InitGL() {
        //Textures setup
//        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);


        //Automatically removes faces not visible to camera.
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.529f, 0.807f, 0.921f, 0.0f);
        GL11.glClearDepth(1.0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        float lightAmbient[] = { 2.0f, 2.0f, 2.0f, 2.0f };
        float lightDiffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };
        float lightPosition[] = { 0.0f, 1.0f, 1.0f, 0.0f };

        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, asFloatBuffer(lightAmbient));              // Setup The Ambient Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, asFloatBuffer(lightDiffuse));              // Setup The Diffuse Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,asFloatBuffer(lightPosition));

        GL11.glEnable(GL11.GL_LIGHT1);
        GL11.glEnable ( GL11.GL_LIGHTING ) ;

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

