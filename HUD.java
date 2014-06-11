package com.matthewcairns.voxel;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;

/**
 * Created by Matthew Cairns on 10/06/2014.
 * All rights reserved.
 */
public class HUD {

    TrueTypeFont font;

    public HUD(String typeFace, int fontSize) {
        Font awtFont = new Font(typeFace, Font.PLAIN, fontSize);
        font = new TrueTypeFont(awtFont, false);
    }

    public void drawFont(int x, int y, String s) {


        font.drawString(x,y,s, Color.white);


    }
}
