package com.matthewcairns.voxel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Matthew Cairns on 04/06/2014.
 * All rights reserved.
 */
public class FPCameraController {
    //3D Vector storing cameras pos
    private Vector3f position = null;
    //Rotation around the Y axis
    private float yaw = 0.0f;
    //Rotation around the X axis
    private float pitch = 0.0f;
}
