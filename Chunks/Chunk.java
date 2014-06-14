package com.matthewcairns.voxel.Chunks;

import com.matthewcairns.voxel.Constants;
import com.matthewcairns.voxel.Noise.SimplexNoise;
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

    private int VBOTextureHandle;
    private Texture dirtTexture;

    private int VBOVertexHandle;

    private SimplexNoise simplexNoise;

    private float xOffset = 0;
    private float zOffset = 0;

    private boolean[] faceHidden = new boolean[6];

    private boolean chunkCreated = false, chunkLoaded = false;

    private Block blocks[][][] = new Block[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];
    private int activateBlocks = 0;

    public Chunk(float ox, float oz, float x, float z, SimplexNoise simplexNoise) {
        xOffset = ox + (x*(Constants.CHUNK_SIZE*2))*Constants.BLOCK_SIZE;
        zOffset = ox + (z*(Constants.CHUNK_SIZE*2))*Constants.BLOCK_SIZE;

        this.simplexNoise = simplexNoise;
    }

    public void createBlocks() {
        VBOTextureHandle = GL15.glGenBuffers();
        VBOVertexHandle = GL15.glGenBuffers();

        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                for (int y = 0; y < Constants.CHUNK_SIZE; y++) {
                    blocks[x][y][z] = new Block();
                }
            }
        }

        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                int height = (int)(1000*(simplexNoise.getNoise((x+xOffset/2)*Constants.BLOCK_SIZE,(z+zOffset/2)*Constants.BLOCK_SIZE)));
                if(height <= 0)
                    height = 1;
                if(height >= Constants.CHUNK_SIZE)
                    height = Constants.CHUNK_SIZE;
                for (int y = 0; y < height; y++) {


                    blocks[x][y][z].setActive(true);
                    activateBlocks += 1;
                }
            }
        }

//        dirtTexture = loadTexture("StoneTile");
//        System.out.println(dirtTexture.getTextureID());
//        System.out.println(dirtTexture.getHeight());
    }


    public void drawChunk() {
        GL11.glPushMatrix();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
        GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
        //GL11.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        GL11.glDrawArrays(GL11.GL_QUADS, 0, ((24)*activateBlocks));

        GL11.glPopMatrix();

    }

    public void putVertices(float tx, float ty, float tz, FloatBuffer vertexPositionData) {
        float l_length = Constants.BLOCK_SIZE;
        float l_height = Constants.BLOCK_SIZE;
        float l_width = Constants.BLOCK_SIZE;
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
        FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer(((12*6)*activateBlocks));
        FloatBuffer vertexTextureData = BufferUtils.createFloatBuffer(((12*6)*activateBlocks));
        Random random = new Random();

        //dirtTexture.bind();

        float[] cubeColorArray = new float[24*3];
        for(int i=0; i<24*3; i++) {
            cubeColorArray[i] = random.nextFloat();
        }

        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                for (int y = 0; y < Constants.CHUNK_SIZE; y++) {

                    if(occlusionCulling(x, y, z)) {
                        continue;
                    }

                    if(blocks[x][y][z].getActive()) {
                        putVertices((x*2)*Constants.BLOCK_SIZE, (-y*2)*Constants.BLOCK_SIZE, (z*2)*Constants.BLOCK_SIZE, vertexPositionData);

//                        vertexTextureData.put(new float[]{
//                                0.0f, 0.0f,
//                                1.0f, 0.0f,
//                                1.0f, 1.0f,
//                                0.0f, 1.0f,
//
//                                0.0f, 0.0f,
//                                1.0f, 0.0f,
//                                1.0f, 1.0f,
//                                0.0f, 1.0f,
//
//                                0.0f, 0.0f,
//                                1.0f, 0.0f,
//                                1.0f, 1.0f,
//                                0.0f, 1.0f,
//
//                                0.0f, 0.0f,
//                                1.0f, 0.0f,
//                                1.0f, 1.0f,
//                                0.0f, 1.0f,
//
//                                0.0f, 0.0f,
//                                1.0f, 0.0f,
//                                1.0f, 1.0f,
//                                0.0f, 1.0f,
//
//                                0.0f, 0.0f,
//                                1.0f, 0.0f,
//                                1.0f, 1.0f,
//                                0.0f, 1.0f,
//                        });

                        vertexTextureData.put(cubeColorArray);
                    }
                }
            }
        }

        vertexPositionData.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexPositionData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        vertexTextureData.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexTextureData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public boolean occlusionCulling(int x, int y, int z) {
        faceHidden[0] = x > 0 && blocks[x - 1][y][z].getActive();
        faceHidden[1] = x < Constants.CHUNK_SIZE - 1 && blocks[x + 1][y][z].getActive();
        faceHidden[2] = y > 0 && blocks[x][y - 1][z].getActive();
        faceHidden[3] = y < Constants.CHUNK_SIZE - 1 && blocks[x][y + 1][z].getActive();
        faceHidden[4] = z > 0 && blocks[x][y][z - 1].getActive();
        faceHidden[5] = z < Constants.CHUNK_SIZE - 1 && blocks[x][y][z + 1].getActive();

        return faceHidden[0] && faceHidden[1]  && faceHidden[2] && faceHidden[3] && faceHidden[4] && faceHidden[5];
    }

    public void dispose() {
        GL15.glDeleteBuffers(VBOTextureHandle);
        GL15.glDeleteBuffers(VBOVertexHandle);
    }

    public boolean isChunkCreated() {
        return chunkCreated;
    }

    public void setChunkCreated(boolean chunkCreated) {
        this.chunkCreated = chunkCreated;
    }

    public boolean isChunkLoaded() {
        return chunkLoaded;
    }

    public void setChunkLoaded(boolean chunkLoaded) {
        this.chunkLoaded = chunkLoaded;
    }

    public static Texture loadTexture(String texName) {
    try {
        return TextureLoader.getTexture("tga", new FileInputStream(new File("assets/" + texName + ".tga")));
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}

}
