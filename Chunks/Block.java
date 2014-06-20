package com.matthewcairns.voxel.Chunks;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.Vector;

/**
 * Created by Matthew Cairns on 04/06/2014.
 * All rights reserved.
 */
public class Block {
    private Vector3f blockLocation;

    public BlockType type;
    public enum BlockType {
        BlockType_Default(0),
        BlockType_Grass(1),
        BlockType_Dirt(2),
        BlockType_Water(3),
        BlockType_Stone(4),
        BlockType_Wood(5),
        BlockType_Sand(6),
        BlockType_NumTypes(7);
        private int BlockID;
        BlockType(int i) {
            BlockID=i;
        }
        public int GetID(){
            return BlockID;
        }
    }
    public Block() {
        type = BlockType.BlockType_Default;
    }

    public void setBlockType(BlockType type) {
        this.type = type;
    }

    private boolean IsActive = false;

    public boolean getActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public void setLocation(Vector3f loc) { blockLocation = loc; }


}
