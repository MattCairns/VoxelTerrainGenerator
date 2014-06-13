package com.matthewcairns.voxel.Chunks;

import com.matthewcairns.voxel.Noise.SimplexNoise;

/**
* Created by Matthew Cairns on 08/06/2014.
* All rights reserved.
*/
public class ChunkManager {
    //private ArrayList<Chunk> loadChunks;
    private Chunk renderChunk[][];

    SimplexNoise simplexNoise = new SimplexNoise(100, 0.05, 5000);

    private int viewDistance;

    public ChunkManager(int viewDistance) {
        //loadChunks = new ArrayList<Chunk>();
        renderChunk = new Chunk[viewDistance][viewDistance];
        this.viewDistance = viewDistance;
    }

    public void loadChunks() {
        for (int x = 0; x < viewDistance; x++) {
                for (int z = 0; z < viewDistance; z++) {
                    renderChunk[x][z] = new Chunk(x, z, simplexNoise);
            }
        }
    }

    public void renderChunks() {
        for (int x = 0; x < viewDistance; x++) {
                for (int z = 0; z < viewDistance; z++) {
                    renderChunk[x][z].drawChunk();
                }

        }
    }

    public int getNumberOfChunks() {
        return renderChunk.length*renderChunk.length;
    }

}
