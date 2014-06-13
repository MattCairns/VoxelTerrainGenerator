package com.matthewcairns.voxel.Chunks;

import com.matthewcairns.voxel.Constants;
import com.matthewcairns.voxel.Noise.SimplexNoise;
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

    private int VBOTextureHandle;
    private int VBOVertexHandle;
    private FloatBuffer vertexPositionData;

    private SimplexNoise simplexNoise;

    private float xOffset = 0;
    private float zOffset = 0;

    private boolean xPos = false;
    private boolean yPos = false;
    private boolean zPos = false;
    private boolean xNeg = false;
    private boolean yNeg = false;
    private boolean zNeg = false;

    private Block blocks[][][] = new Block[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];
    private int activateBlocks = 0;

    public Chunk(float x, float z, SimplexNoise simplexNoise) {
        xOffset = x*(Constants.CHUNK_SIZE*2);
        zOffset = z*(Constants.CHUNK_SIZE*2);

        VBOTextureHandle = GL15.glGenBuffers();
        VBOVertexHandle = GL15.glGenBuffers();

        this.simplexNoise = simplexNoise;

        createBlocks();
        createChunk();
    }

    public void createBlocks() {
        Random random = new Random();
        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                for (int y = 0; y < Constants.CHUNK_SIZE; y++) {
                    blocks[x][y][z] = new Block();
                }
            }
        }

        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                int height = (int)(1000*(simplexNoise.getNoise(x+xOffset/2,z+zOffset/2)));
                System.out.println(height);
                if(height <= 0)
                    height = 1;
                if(height >= 16)
                    height = 16;
                for (int y = 0; y < height; y++) {
                    blocks[x][y][z].setActive(true);
                    activateBlocks += 1;
                }
            }
        }
    }


    public void drawChunk() {
            GL11.glPushMatrix();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
            GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
            GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);


            GL11.glDrawArrays(GL11.GL_QUADS, 0, ((24)*activateBlocks));
            GL11.glPopMatrix();
    }

    public void putVertices(float tx, float ty, float tz) {
        float l_length = 1.0f;
        float l_height = 1.0f;
        float l_width = 1.0f;
        vertexPositionData.put(new float[]{
                xOffset + l_length + tx, l_height + ty, zOffset + -l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + -l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + l_width + tz,
                xOffset + l_length + tx, l_height + ty, zOffset + l_width + tz,

                xOffset + l_length + tx, -l_height + ty, zOffset + l_width + tz,
                xOffset + -l_length + tx, -l_height + ty, zOffset + l_width + tz,
                xOffset + -l_length + tx, -l_height + ty, zOffset + -l_width + tz,
                xOffset + l_length + tx, -l_height + ty, zOffset + -l_width + tz,

                xOffset + l_length + tx, l_height + ty, zOffset + l_width + tz,
                xOffset + -l_length + tx, l_height + ty,zOffset +  l_width + tz,
                xOffset + -l_length + tx, -l_height + ty,zOffset +  l_width + tz,
                xOffset + l_length + tx, -l_height + ty, zOffset + l_width + tz,

                xOffset + l_length + tx, -l_height + ty, zOffset + -l_width + tz,
                xOffset + -l_length + tx, -l_height + ty,zOffset +  -l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + -l_width + tz,
                xOffset + l_length + tx, l_height + ty, zOffset + -l_width + tz,

                xOffset + -l_length + tx, l_height + ty, zOffset + l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + -l_width + tz,
                xOffset + -l_length + tx, -l_height + ty, zOffset + -l_width + tz,
                xOffset + -l_length + tx, -l_height + ty,zOffset +  l_width + tz,

                xOffset + l_length + tx, l_height + ty,zOffset +  -l_width + tz,
                xOffset + l_length + tx, l_height + ty, zOffset + l_width + tz,
                xOffset + l_length + tx, -l_height + ty, zOffset + l_width + tz,
                xOffset + l_length + tx, -l_height + ty, zOffset + -l_width + tz

        });
    }

    public void createChunk() {
        vertexPositionData = BufferUtils.createFloatBuffer(((24*3)*activateBlocks));

        Random random = new Random();
        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                for (int y = 0; y < Constants.CHUNK_SIZE; y++) {
                    if(blocks[x][y][z].getActive()) {
                        putVertices(x*2, -y*2, z*2);
                    }
                }
            }
        }

        vertexPositionData.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexPositionData,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);


        float[] cubeColorArray = new float[24*3];
        for(int i=0; i<24*3; i++) {
            cubeColorArray[i] = random.nextFloat();
        }

        FloatBuffer vertexTextureData = BufferUtils.createFloatBuffer(((24*3)*activateBlocks));
        for(int i=0; i<activateBlocks; i++) {
            vertexTextureData.put(cubeColorArray);
        }
        vertexTextureData.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexTextureData,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        glEnd();
    }

//    public static Texture loadTexture(String texName) {
//        try {
//            return TextureLoader.getTexture("png", new FileInputStream(new File("assets/" + texName + ".png")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
