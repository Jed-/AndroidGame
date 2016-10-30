package com.jed.game;

import com.kilobolt.framework.Input;
import com.kilobolt.framework.implementation.FrameBufferSize;

public class Util {
    private static int width = FrameBufferSize.width;
    private static int height = FrameBufferSize.height;
    public static boolean inBounds(Input.TouchEvent event, int x, int y, int width,
                     int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    }
    public static boolean inBoundsRel(Input.TouchEvent event, double x, double y, double _width, double _height) {
        return inBounds(event, (int)(width * x), (int)(height * y), (int)(width * _width), (int)(height * _height));
    }
}
