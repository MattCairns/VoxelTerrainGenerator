package com.matthewcairns.voxel.Chunks;

import com.matthewcairns.voxel.Constants;
import com.matthewcairns.voxel.Noise.SimplexNoise;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Vector;

/**
* Created by Matthew Cairns on 08/06/2014.
* All rights reserved.
*/
public class ChunkManager {
    private ArrayList<Chunk> chunks;

    private int CHUNKS_LOADED_PER_FRAME = 3;
    private int renderedChunks = 0, numChunks = 0;
    private boolean chunksInitiated = false;

    private Frustum frustum = new Frustum();

    private Vector3f playerPosition;


    SimplexNoise simplexNoise = new SimplexNoise(800, 0.65, 1827387);

    public ChunkManager() {
        chunks = new ArrayList<Chunk>();
    }

    public void update(Vector3f playerPosition) {
        this.playerPosition = playerPosition;
        if(!chunksInitiated)
            initChunks();
        loadChunks();
        createChunks();
        renderChunks();
    }

    public void initChunks() {
        //chunks.add(new Chunk(-playerPosition.getX(), -playerPosition.getZ(), 0, 0, simplexNoise));
        for (int x = 0; x < Constants.VIEW_DISTANCE; x++) {
            for (int z = 0; z < Constants.VIEW_DISTANCE; z++) {
                chunks.add(new Chunk(x, z, simplexNoise));
            }
        }
        chunksInitiated=true;
    }

    public void loadChunks() {
        int chunksCreated = 0;
        for(Chunk chunk : chunks) {
            if (!chunk.isChunkLoaded() && chunksCreated < CHUNKS_LOADED_PER_FRAME) {
                chunk.createBlocks();
                chunk.setChunkLoaded(true);
                chunksCreated++;
            }
        }
    }

    public void createChunks() {
        int chunksCreated = 0;
        for(Chunk chunk : chunks) {
            if (!chunk.isChunkCreated() && chunk.isChunkLoaded() && chunksCreated < CHUNKS_LOADED_PER_FRAME) {
                chunk.createChunk();
                chunk.setChunkCreated(true);
                chunksCreated++;
            }
        }
    }

    public void renderChunks() {
        frustum.calculateFrustum();
        for(Chunk chunk : chunks) {
            if (chunk.isChunkCreated()) {
                if(frustum.cubeInFrustum(chunk.getChunkLocation().getX(), chunk.getChunkLocation().getY(), chunk.getChunkLocation().getZ(), (16*Constants.BLOCK_SIZE)*2)) {
                    chunk.drawChunk();
                    numChunks++;
                }
            }
        }
        renderedChunks = numChunks;
        System.out.println(numChunks);

        numChunks = 0;
    }

    public void removeChunks() {
        int chunksRemoved= 0;
        for(Chunk chunk : chunks) {
            if (!chunk.isChunkCreated() && chunk.isChunkLoaded() && chunksRemoved < CHUNKS_LOADED_PER_FRAME) {
                chunk.createChunk();
                chunk.setChunkCreated(true);
                chunksRemoved++;
            }
        }
    }

    public int getNumberOfChunks() {
        return renderedChunks;
    }

}
