package com.matthewcairns.voxel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Matthew Cairns on 04/06/2014.
 * All rights reserved.
 */
public class Chunk {

    Texture dirt;
    private int VBOTextureHandle;
    private int VBOVertexHandle;

    private int CHUNK_SIZE = 16;

    private float xOffset = 0;
    private float zOffset = 0;

    private Block blocks[][][] = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

    public Chunk(float x, float z) {
        xOffset = x*32.0f;
        zOffset = z*32.0f;
    }

    public void createBlocks() {
        Random random = new Random();
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    blocks[x][y][z] = new Block();

                    blocks[x][y][z].setActive(random.nextBoolean());
                }
            }
        }
    }

    public void drawChunk() {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    glTranslatef(x * 2, y * 2, z * 2);
                    if(blocks[x][y][z].getActive()) {
                        drawBlock();
                    }
                    glTranslatef(-x * 2, -y * 2, -z * 2);
                }
            }
        }
    }

    public void drawBlock() {
        GL11.glPushMatrix();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
        GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);


        GL11.glDrawArrays(GL11.GL_QUADS, 0, 24);
        GL11.glPopMatrix();

    }

    public void createBlock() {
        VBOTextureHandle = GL15.glGenBuffers();
        VBOVertexHandle = GL15.glGenBuffers();
        FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer(24*3);

        float l_length = 1.0f;
        float l_height = 1.0f;
        float l_width = 1.0f;

        vertexPositionData.put(new float[]{
                xOffset + l_length, l_height, zOffset + -l_width,
                xOffset + -l_length, l_height, zOffset + -l_width,
                xOffset + -l_length, l_height, zOffset + l_width,
                xOffset + l_length, l_height, zOffset + l_width,

                xOffset + l_length, -l_height, zOffset + l_width,
                xOffset + -l_length, -l_height, zOffset + l_width,
                xOffset + -l_length, -l_height, zOffset + -l_width,
                xOffset + l_length, -l_height, zOffset + -l_width,

                xOffset + l_length, l_height, zOffset + l_width,
                xOffset + -l_length, l_height,zOffset +  l_width,
                xOffset + -l_length, -l_height,zOffset +  l_width,
                xOffset + l_length, -l_height, zOffset + l_width,

                xOffset + l_length, -l_height, zOffset + -l_width,
                xOffset + -l_length, -l_height,zOffset +  -l_width,
                xOffset + -l_length, l_height, zOffset + -l_width,
                xOffset + l_length, l_height, zOffset + -l_width,

                xOffset + -l_length, l_height, zOffset + l_width,
                xOffset + -l_length, l_height, zOffset + -l_width,
                xOffset + -l_length, -l_height, zOffset + -l_width,
                xOffset + -l_length, -l_height,zOffset +  l_width,

                xOffset + l_length, l_height,zOffset +  -l_width,
                xOffset + l_length, l_height, zOffset + l_width,
                xOffset + l_length, -l_height, zOffset + l_width,
                xOffset + l_length, -l_height, zOffset + -l_width

        });

        vertexPositionData.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexPositionData,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        Random random = new Random();
        float[] cubeColorArray = new float[24*3];
        for(int i=0; i<24*3; i++) {
            cubeColorArray[i] = random.nextFloat();
        }
        FloatBuffer vertexTextureData = BufferUtils.createFloatBuffer(24 * 3);
        vertexTextureData.put(cubeColorArray);
        vertexTextureData.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexTextureData,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        glEnd();

        createBlocks();
    }

    public static Texture loadTexture(String texName) {
        try {
            return TextureLoader.getTexture("png", new FileInputStream(new File("assets/" + texName + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
