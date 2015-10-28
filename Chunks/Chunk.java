package com.matthewcairns.voxel.Chunks;

import com.matthewcairns.voxel.Constants;
import com.matthewcairns.voxel.Noise.SimplexNoise;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Created by Matthew Cairns on 04/06/2014.
 * All rights reserved.
 */
public class Chunk {
    private int VBOVertexHandle;
    private int VBONormalHandle;
    private int VBOTextureHandle;

    private Texture textureAtlas;

    private SimplexNoise simplexNoise;

    private float xOffset = 0;
    private float zOffset = 0;

    private boolean[] faceHidden = new boolean[6];

    private boolean chunkCreated = false, chunkLoaded = false, unloadChunk = false;

    private Block blocks[] = new Block[Constants.CHUNK_SIZE * Constants.CHUNK_SIZE * Constants.CHUNK_SIZE];
    private int activateBlocks = 0;

    public Chunk(float playerLocX, float playerLocZ, float bx, float bz, SimplexNoise simplexNoise) {
        xOffset = playerLocX + (bx*(Constants.CHUNK_SIZE*2))*Constants.BLOCK_SIZE;
        zOffset = playerLocZ + (bz*(Constants.CHUNK_SIZE*2))*Constants.BLOCK_SIZE;

        this.simplexNoise = simplexNoise;

        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                for (int y = 0; y < Constants.CHUNK_SIZE; y++) {
                    blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)] = new Block();
                }
            }
        }
    }

    public void createBlocks() {
        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {
            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {
                double height = 16*(simplexNoise.getNoise((x+(xOffset/2)/Constants.BLOCK_SIZE),(z+(zOffset/2)/Constants.BLOCK_SIZE)));
                if(height <= 0)
                    height = 1;
                if(height >= Constants.CHUNK_SIZE)
                    height = Constants.CHUNK_SIZE;

                for (int y = 0; y < height; y++) {
                    if(height <= 1) {
                        blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].setBlockType(Block.BlockType.BlockType_Water);
                    }
                    if(height <= 4 && height > 1) {
                        blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].setBlockType(Block.BlockType.BlockType_Dirt);
                    }
                    if(height > 4) {
                        blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].setBlockType(Block.BlockType.BlockType_Grass);
                    }
                    blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].setActive(true);
                    activateBlocks += 1;

                    blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].setLocation(new Vector3f(x+xOffset, y+(float)height, z+zOffset));
                }
            }
        }

        textureAtlas = loadTexture("terrain.png");
    }

    public void drawChunk() {
        //LOD Mipmapping setup
        glGenerateMipmap(GL_TEXTURE_2D);
      //  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);


        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, VBONormalHandle);
        glNormalPointer(GL_FLOAT, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glTexCoordPointer(2, GL_FLOAT, 0, 0L);

        //Enable Vertex Buffer Object rendering (VBO)
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glDrawArrays(GL_QUADS, 0, ((24)*activateBlocks));

        //Disable Vertex Buffer Object rendering (VBO)
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void createChunk() {
        VBOTextureHandle = glGenBuffers();
        VBONormalHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();

        FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer(((12*6)*activateBlocks));
        FloatBuffer vertexNormalData = BufferUtils.createFloatBuffer(((6*3)*4)*activateBlocks);
        FloatBuffer vertexTextureData = BufferUtils.createFloatBuffer(((8*6)*activateBlocks));

        //Bind the texture to OpenGL
        textureAtlas.bind();

        for (int x = 0; x < Constants.CHUNK_SIZE; x++) {

            for (int z = 0; z < Constants.CHUNK_SIZE; z++) {

                for (int y = 0; y < Constants.CHUNK_SIZE; y++) {

                    if (occlusionCulling(x, y, z)) {
                        continue;
                    }


                    if (blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].getActive()) {
                        vertexNormalData.put(BlockData.cubeNormals());
                        vertexPositionData.put(BlockData.cubeVertices(xOffset, zOffset,(x * 2) * Constants.BLOCK_SIZE, (-y * 2) * Constants.BLOCK_SIZE, (z * 2) * Constants.BLOCK_SIZE));

                        if (blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].type.GetID() == 1) {
                            for (int i = 0; i < 6; i++) {
                                vertexTextureData.put(BlockData.grass());
                            }
                        }

                        if (blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].type.GetID() == 2) {
                            for (int i = 0; i < 6; i++) {
                                vertexTextureData.put(BlockData.dirt());
                            }
                        }

                        if (blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].type.GetID() == 3) {
                            for (int i = 0; i < 6; i++) {
                                vertexTextureData.put(BlockData.water());
                            }
                        }
                    }
                }
            }
        }

        vertexPositionData.flip();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        vertexNormalData.flip();
        glBindBuffer(GL_ARRAY_BUFFER, VBONormalHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexNormalData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        vertexTextureData.flip();
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }


    public boolean occlusionCulling(int x, int y, int z) {
        faceHidden[0] = x > 0 && blocks[(x-1) + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].getActive();
        faceHidden[1] = x < Constants.CHUNK_SIZE - 1 && blocks[(x+1) + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * z)].getActive();
        faceHidden[2] = y > 0 && blocks[x + Constants.CHUNK_SIZE * ((y-1) + Constants.CHUNK_SIZE * z)].getActive();
        faceHidden[3] = y < Constants.CHUNK_SIZE - 1 && blocks[x + Constants.CHUNK_SIZE * ((y+1) + Constants.CHUNK_SIZE * z)].getActive();
        faceHidden[4] = z > 0 && blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * (z-1))].getActive();
        faceHidden[5] = z < Constants.CHUNK_SIZE - 1 && blocks[x + Constants.CHUNK_SIZE * (y + Constants.CHUNK_SIZE * (z+1))].getActive();

        return faceHidden[0] && faceHidden[1]  && faceHidden[2] && faceHidden[3] && faceHidden[4] && faceHidden[5];
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

    public boolean isUnloadChunk() {
        return unloadChunk;
    }

    public void setUnloadChunk(boolean unloadChunk) {
        this.unloadChunk = unloadChunk;
    }

    public Vector3f getChunkLocation() {
        return new Vector3f(xOffset, 0, zOffset);
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
