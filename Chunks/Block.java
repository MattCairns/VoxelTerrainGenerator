package com.matthewcairns.voxel.Chunks;

import org.lwjgl.opengl.GL11;

/**
 * Created by Matthew Cairns on 04/06/2014.
 * All rights reserved.
 */
public class Block {
    private boolean IsActive = false;

    public boolean getActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }


}
