package com.matthewcairns.voxel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Matthew Cairns on 04/06/2014.
 * All rights reserved.
 */
public class Chunk {

    private int VBOColorHandle;
    private int VBOVertexHandle;

    private int CHUNK_SIZE = 16;

    private Block blocks[][][] = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

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
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
        GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 24);
        GL11.glPopMatrix();
    }

    public void createBlock() {//float x, float y, float z) {
        VBOColorHandle = GL15.glGenBuffers();
        VBOVertexHandle = GL15.glGenBuffers();
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer(24*3);

        float l_length = 1.0f;
        float l_height = 1.0f;
        float l_width = 1.0f;

        VertexPositionData.put(new float[]{
                l_length, l_height, -l_width,
                -l_length, l_height, -l_width,
                -l_length, l_height, l_width,
                l_length, l_height, l_width,

                l_length, -l_height, l_width,
                -l_length, -l_height, l_width,
                -l_length, -l_height, -l_width,
                l_length, -l_height, -l_width,

                l_length, l_height, l_width,
                -l_length, l_height, l_width,
                -l_length, -l_height, l_width,
                l_length, -l_height, l_width,

                l_length, -l_height, -l_width,
                -l_length, -l_height, -l_width,
                -l_length, l_height, -l_width,
                l_length, l_height, -l_width,

                -l_length, l_height, l_width,
                -l_length, l_height, -l_width,
                -l_length, -l_height, -l_width,
                -l_length, -l_height, l_width,

                l_length, l_height, -l_width,
                l_length, l_height, l_width,
                l_length, -l_height, l_width,
                l_length, -l_height, -l_width

        });

        VertexPositionData.flip();

        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer(24 * 3);
        VertexColorData.put(new float[] { 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, });
        VertexColorData.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexPositionData,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorData,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        glEnd();

        createBlocks();
    }

}
