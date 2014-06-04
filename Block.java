package com.matthewcairns.voxel;

import org.lwjgl.opengl.GL11;

/**
 * Created by Matthew Cairns on 04/06/2014.
 * All rights reserved.
 */
public class Block {

    public static void multiCreateBlocks(int n) {
        for(int x = 0; x<n;x++){
            for(int y = 0; y<n;y++){
                for(int z = 0; z<n;z++){
                    createBlock();
                    GL11.glTranslatef(0f, 0.0f, 2f);
                }
                GL11.glTranslatef(0f, 2f, -6f);
            }
            GL11.glTranslatef(2f, -6f, 0);
        }

    }

    public static void createBlock() {//float x, float y, float z) {
        float l_length = 1.0f;
        float l_height = 1.0f;
        float l_width = 1.0f;


        GL11.glRotatef(45f,0.5f,1.0f,0.0f);
        GL11.glColor3f(0.5f,0.5f,1.0f);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glColor3f(1.0f,1.0f,0.0f);
        GL11.glVertex3f( l_length, l_height,-l_width);
        GL11.glVertex3f(-l_length, l_height,-l_width);
        GL11.glVertex3f(-l_length, l_height, l_width);
        GL11.glVertex3f( l_length, l_height, l_width);

        GL11.glColor3f(1.0f,0.5f,0.0f);
        GL11.glVertex3f( l_length,-l_height, l_width);
        GL11.glVertex3f(-l_length,-l_height, l_width);
        GL11.glVertex3f(-l_length,-l_height,-l_width);
        GL11.glVertex3f( l_length,-l_height,-l_width);

        GL11.glColor3f(1.0f,0.0f,0.0f);
        GL11.glVertex3f( l_length, l_height, l_width);
        GL11.glVertex3f(-l_length, l_height, l_width);
        GL11.glVertex3f(-l_length,-l_height, l_width);
        GL11.glVertex3f( l_length,-l_height, l_width);

        GL11.glColor3f(1.0f,1.0f,0.0f);
        GL11.glVertex3f( l_length,-l_height,-l_width);
        GL11.glVertex3f(-l_length,-l_height,-l_width);
        GL11.glVertex3f(-l_length, l_height,-l_width);
        GL11.glVertex3f( l_length, l_height,-l_width);

        GL11.glColor3f(0.0f,0.0f,1.0f);
        GL11.glVertex3f(-l_length, l_height, l_width);
        GL11.glVertex3f(-l_length, l_height,-l_width);
        GL11.glVertex3f(-l_length,-l_height,-l_width);
        GL11.glVertex3f(-l_length,-l_height, l_width);

        GL11.glColor3f(1.0f,0.0f,1.0f);
        GL11.glVertex3f( l_length, l_height,-l_width);
        GL11.glVertex3f( l_length, l_height, l_width);
        GL11.glVertex3f( l_length,-l_height, l_width);
        GL11.glVertex3f( l_length,-l_height,-l_width);
        GL11.glEnd();
    }
}
