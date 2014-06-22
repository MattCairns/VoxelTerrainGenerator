package com.matthewcairns.voxel.Chunks;

import com.matthewcairns.voxel.Constants;
import com.matthewcairns.voxel.Noise.SimplexNoise;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;

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
    private int playerChunkLocation=0;

    SimplexNoise simplexNoise = new SimplexNoise(800, 0.71, 1234);

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
        checkForNewChucks();
        unloadChunks();

        checkCollisions();
    }

    public void initChunks() {
        //chunks.add(new Chunk(-playerPosition.getX(), -playerPosition.getZ(), 0, 0, simplexNoise));
        for (int x = 0; x < Constants.VIEW_DISTANCE/2; x++) {
            for (int z = 0; z < Constants.VIEW_DISTANCE/2; z++) {
                chunks.add(new Chunk(-playerPosition.getX(), -playerPosition.getZ(), x, z, simplexNoise));
                chunks.add(new Chunk(-playerPosition.getX(), -playerPosition.getZ(), -x, -z, simplexNoise));
                chunks.add(new Chunk(-playerPosition.getX(), -playerPosition.getZ(), x, -z, simplexNoise));
                chunks.add(new Chunk(-playerPosition.getX(), -playerPosition.getZ(), -x, z, simplexNoise));
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
        numChunks = 0;
    }

    public void unloadChunks() {
        Iterator<Chunk> itr = chunks.iterator();
        while(itr.hasNext()) {
            if (getPlayerDistanceFromChunk(itr.next()) > Constants.VIEW_DISTANCE*200) {
                itr.remove();

            }
        }
    }

    private void checkForNewChucks() {
//        chunkPlayerIsIn();

    }

    private boolean isChunkAtPosition(Vector3f pos) {
        for(Chunk chunk : chunks) {
            if(chunk.getChunkLocation() == pos) {
                return true;
            }
        }
        return false;
    }

    public double getPlayerDistanceFromChunk(Chunk chunk) {
        return Math.sqrt((playerPosition.getX()+chunk.getChunkLocation().getX())*(playerPosition.getX()+chunk.getChunkLocation().getX()) +
                        (playerPosition.getZ()+chunk.getChunkLocation().getZ())*(playerPosition.getZ()+chunk.getChunkLocation().getZ()));
    }

//    private float chunkPlayerIsIn() {
////        int xx = playerPosition.getX() / Constants.CHUNK_SIZE;
////        int yy = playerPosition.getY() / Constants.CHUNK_SIZE;
////        int zz = playerPosition.getZ() / Constants.CHUNK_SIZE;
//
//
//        Vector3f v = new Vector3f(xx, yy, zz);
//        for(Chunk chunk : chunks) {
//            v = chunk.getChunkLocation();
//            if(v.getX() + Constants.CHUNK_SIZE*Constants.BLOCK_SIZE > xx && v.getZ() + Constants.CHUNK_SIZE*Constants.BLOCK_SIZE > zz  &&
//                    v.getX() < playerPosition.getX() && v.getZ() < playerPosition.getZ()) {
//                System.out.println(chunk);
//            }
//        }
//
//
//        return xx;
//    }

    private void checkCollisions() {

    }

    public int getNumberOfChunks() {
        return renderedChunks;
    }

}
