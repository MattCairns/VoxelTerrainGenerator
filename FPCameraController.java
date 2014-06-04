package com.matthewcairns.voxel;

import org.lwjgl.opengl.GL11;
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

    public FPCameraController(float x, float y, float z) {
        position = new Vector3f(x,y,z);
    }

    public void yaw(float value) { yaw += value; }
    public void pitch(float value) { pitch += value; }

    public void walkForward(float distance) {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
        position.y += distance * (float)Math.cos(Math.toRadians(yaw));
    }

    public void walkBackwards(float distance) {
        position.x += distance * (float)Math.sin(Math.toRadians(yaw));
        position.y -= distance * (float)Math.cos(Math.toRadians(yaw));
    }

    public void strafeLeft(float distance) {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
        position.y += distance * (float)Math.cos(Math.toRadians(yaw-90));
    }

    public void strafeRight(float distance) {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
        position.y += distance * (float)Math.cos(Math.toRadians(yaw+90));
    }

    public void lookThrough() {
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(position.x, position.y, position.z);
    }

}
