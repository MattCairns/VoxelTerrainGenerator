package com.matthewcairns.voxel;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
* Created by Matthew Cairns on 08/06/2014.
* All rights reserved.
*/
public class ChunkManager {
    //private ArrayList<Chunk> loadChunks;
    private Chunk renderChunk[][];

    private int viewDistance;

    public ChunkManager(int viewDistance) {
        //loadChunks = new ArrayList<Chunk>();
        renderChunk = new Chunk[viewDistance][viewDistance];
        this.viewDistance = viewDistance;
    }

    public void loadChunks() {
        for (int x = 0; x < viewDistance; x++) {
                for (int z = 0; z < viewDistance; z++) {
                    renderChunk[x][z] = new Chunk(x, z);
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
