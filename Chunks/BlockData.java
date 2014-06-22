package com.matthewcairns.voxel.Chunks;

import com.matthewcairns.voxel.Constants;

import java.nio.FloatBuffer;

/**
 * Created by Matthew Cairns on 22/06/2014.
 * All rights reserved.
 */
public class BlockData {
    public static float[] cubeVertices(float xOffset, float zOffset, float tx, float ty, float tz) {
        float l_length = Constants.BLOCK_SIZE;
        float l_height = Constants.BLOCK_SIZE;
        float l_width = Constants.BLOCK_SIZE;
        return new float[]{
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

        };
    }

    public static float[] cubeNormals() {
        return new float[] {
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
    }

    public static float[] grass() {
        return new float[]{
                0.0f, 0.0f, //Top left
                0.0f, 0.05f, //Bottom left
                0.05f, 0.05f, //Bottom right
                0.05f, 0.0f, //Top right
        };
    }

    public static float[] dirt() {
        return new float[]{
                0.125f, 0.0f,
                0.125f, 0.0625f,
                0.1875f, 0.0625f,
                0.1875f, 0.0f,
        };
    }

    public static float[] water() {
        return new float[]{
                0.9375f, 0.8125f, //Top left
                0.9375f, 0.875f, //Bottom left
                1.0f, 0.875f, //Bottom right
                1.0f, 0.8125f, //Top right
        };
    }
}
