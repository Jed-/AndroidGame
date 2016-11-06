package com.jed.game;

import com.jed.game.structure.Level;
import com.jed.game.structure.Map;
import com.jed.game.structure.Terrain;
import com.jed.game.structure.Tile;
import com.kilobolt.framework.FileIO;
import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Input;
import com.kilobolt.framework.implementation.FrameBufferSize;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Util {
    private static int width = FrameBufferSize.width;
    private static int height = FrameBufferSize.height;

    private static int tile_w = 128;
    private static int tile_h = 120;

    public static boolean inBounds(Input.TouchEvent event, int x, int y, int width,
                     int height) {
        if(event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    }
    public static boolean inBoundsRel(Input.TouchEvent event, double x, double y, double _width, double _height) {
        return inBounds(event, (int)(width * x), (int)(height * y), (int)(width * _width), (int)(height * _height));
    }

    public static List<Tile> parseTiles(int height, List<List<String>> terrains) {
        List<Tile> tileList = new ArrayList<Tile>();
        int x = 0, y = 0;
        for(List<String> row : terrains) {
            for(String name : row) {
                Tile t = new Tile(x, y, name);
                tileList.add(t);
                x++;
            }
            y++;
        }
        return tileList;
    }
}
