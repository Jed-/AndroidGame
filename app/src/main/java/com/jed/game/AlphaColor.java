package com.jed.game;

import android.graphics.Color;

public class AlphaColor {
    private static final int opacity = 0x88;
    private static int mktrans(int color) {
        return color | (opacity << 24);
    }
    static int red   = mktrans(Color.RED);
    static int green = mktrans(Color.GREEN);
    static int blue  = mktrans(Color.BLUE);

    static int white = mktrans(Color.WHITE);
}
