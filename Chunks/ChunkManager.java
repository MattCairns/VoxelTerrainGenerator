package com.matthewcairns.voxel.Chunks;

import com.matthewcairns.voxel.Constants;
import com.matthewcairns.voxel.Noise.SimplexNoise;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
* Created by Matthew Cairns on 08/06/2014.
* All rights reserved.
*/
public class ChunkManager {
    private ArrayList<Chunk> chunks;

    private int CHUNKS_LOADED_PER_FRAME = 1;

    SimplexNoise simplexNoise = new SimplexNoise(100, 0.05, 5000);

    public ChunkManager() {
        chunks = new ArrayList<Chunk>();
        initChunks();
    }

    public void update(Vector3f playerPosition) {
        loadChunks();
        createChunks();
        renderChunks();
    }

    public void initChunks() {
        for (int x = 0; x < Constants.VIEW_DISTANCE; x++) {
            for (int z = 0; z < Constants.VIEW_DISTANCE; z++) {
                chunks.add(new Chunk(x, z, simplexNoise));
            }
        }
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
        for(Chunk chunk : chunks) {
            if (chunk.isChunkCreated()) {
                chunk.drawChunk();
            }
        }
    }
//
//    public int getNumberOfChunks() {
//        return renderChunk.length*renderChunk.length;
//    }

}
