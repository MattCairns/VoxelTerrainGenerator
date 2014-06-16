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
    private int VBOVertexHandle;
    private int VBONormalHandle;
    private int VBOTextureHandle;
    private Texture dirtTexture;

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
                    if(height <= 6) {
                        blocks[x][y][z].setBlockType(Block.BlockType.BlockType_Dirt);
                    }
                    if(height > 6) {
                        blocks[x][y][z].setBlockType(Block.BlockType.BlockType_Grass);
                    }
                    blocks[x][y][z].setActive(true);
                    activateBlocks += 1;
                }
            }
        }

        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                for (int y = 0; y < Constants.CHUNK_SIZE; y++) {
                    if(occlusionCulling(x, y, z)) {
                        activateBlocks--;
                    }                }
            }
        }



        dirtTexture = loadTexture("terrain.png");
    }


    public void drawChunk() {
        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBONormalHandle);
        GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0L);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0L);

        //Enable Vertex Buffer Object rendering (VBO)
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GL11.glDrawArrays(GL11.GL_QUADS, 0, ((24)*activateBlocks));

        //Disable Vertex Buffer Object rendering (VBO)
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void createChunk() {
        VBOTextureHandle = GL15.glGenBuffers();
        VBONormalHandle = GL15.glGenBuffers();
        VBOVertexHandle = GL15.glGenBuffers();

        FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer(((12*6)*activateBlocks));
        FloatBuffer vertexNormalData = BufferUtils.createFloatBuffer(((6*3)*4)*activateBlocks);
        FloatBuffer vertexTextureData = BufferUtils.createFloatBuffer(((8*6)*activateBlocks));

        dirtTexture.bind();

        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                for (int y = 0; y < Constants.CHUNK_SIZE; y++) {

                    if(occlusionCulling(x, y, z)) {
                        continue;
                    }

                    if(blocks[x][y][z].getActive()) {
                        putNormals(vertexNormalData);

                        putVertices((x*2)*Constants.BLOCK_SIZE, (-y*2)*Constants.BLOCK_SIZE, (z*2)*Constants.BLOCK_SIZE, vertexPositionData);

                        if(blocks[x][y][z].type.GetID() == 1) {
                            vertexTextureData.put(new float[]{
                                    0.0f, 0.0f,
                                    0.0f, 0.0625f,
                                    0.0625f, 0.0625f,
                                    0.0625f, 0.0f,

                                    0.0f, 0.0f,
                                    0.0f, 0.0625f,
                                    0.0625f, 0.0625f,
                                    0.0625f, 0.0f,

                                    0.0f, 0.0f,
                                    0.0f, 0.0625f,
                                    0.0625f, 0.0625f,
                                    0.0625f, 0.0f,

                                    0.0f, 0.0f,
                                    0.0f, 0.0625f,
                                    0.0625f, 0.0625f,
                                    0.0625f, 0.0f,

                                    0.0f, 0.0f,
                                    0.0f, 0.062f,
                                    0.0625f, 0.0625f,
                                    0.0625f, 0.0f,

                                    0.0f, 0.0f,
                                    0.0f, 0.0625f,
                                    0.0625f, 0.0625f,
                                    0.0625f, 0.0f
                            });
                        }

                        if(blocks[x][y][z].type.GetID() == 2) {
                            vertexTextureData.put(new float[]{
                                    0.125f, 0.0f,
                                    0.125f, 0.0625f,
                                    0.1875f, 0.0625f,
                                    0.1875f, 0.0f,

                                    0.125f, 0.0f,
                                    0.125f, 0.0625f,
                                    0.1875f, 0.0625f,
                                    0.1875f, 0.0f,

                                    0.125f, 0.0f,
                                    0.125f, 0.0625f,
                                    0.1875f, 0.0625f,
                                    0.1875f, 0.0f,

                                    0.125f, 0.0f,
                                    0.125f, 0.0625f,
                                    0.1875f, 0.0625f,
                                    0.1875f, 0.0f,

                                    0.125f, 0.0f,
                                    0.125f, 0.0625f,
                                    0.1875f, 0.0625f,
                                    0.1875f, 0.0f,

                                    0.125f, 0.0f,
                                    0.125f, 0.0625f,
                                    0.1875f, 0.0625f,
                                    0.1875f, 0.0f,

                            });
                        }
                    }
                }
            }
        }

        vertexPositionData.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexPositionData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        vertexNormalData.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBONormalHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexNormalData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        vertexTextureData.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexTextureData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void putVertices(float tx, float ty, float tz, FloatBuffer vertexPositionData) {
        float l_length = Constants.BLOCK_SIZE;
        float l_height = Constants.BLOCK_SIZE;
        float l_width = Constants.BLOCK_SIZE;
        vertexPositionData.put(new float[]{
                xOffset + l_length + tx, -l_height + ty, zOffset + -l_width + tz,
                xOffset + -l_length + tx, -l_height + ty, zOffset + -l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + -l_width + tz,
                xOffset + l_length + tx, l_height + ty, zOffset + -l_width + tz,

                xOffset + -l_length + tx, -l_height + ty, zOffset + l_width + tz,
                xOffset + l_length + tx, -l_height + ty, zOffset + l_width + tz,
                xOffset + l_length + tx, l_height + ty, zOffset + l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + l_width + tz,

                xOffset + l_length + tx, -l_height + ty, zOffset + l_width + tz,
                xOffset + l_length + tx, -l_height + ty,zOffset +  -l_width + tz,
                xOffset + l_length + tx, l_height + ty,zOffset +  -l_width + tz,
                xOffset + l_length + tx, l_height + ty, zOffset + l_width + tz,

                xOffset + -l_length + tx, -l_height + ty, zOffset + -l_width + tz,
                xOffset + -l_length + tx, -l_height + ty,zOffset +  l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + -l_width + tz,

                xOffset + -l_length + tx, -l_height + ty, zOffset + -l_width + tz,
                xOffset + l_length + tx, -l_height + ty, zOffset + -l_width + tz,
                xOffset + l_length + tx, -l_height + ty, zOffset + l_width + tz,
                xOffset + -l_length + tx, -l_height + ty,zOffset +  l_width + tz,

                xOffset + l_length + tx, l_height + ty,zOffset +  -l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + -l_width + tz,
                xOffset + -l_length + tx, l_height + ty, zOffset + l_width + tz,
                xOffset + l_length + tx, l_height + ty, zOffset + l_width + tz

        });
    }

    public void putNormals(FloatBuffer vertexNormalData) {
        float[] normals = new float[] {
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        };

        vertexNormalData.put(normals);

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
        return TextureLoader.getTexture("png", new FileInputStream(new File("assets/" + texName)));
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}

}
