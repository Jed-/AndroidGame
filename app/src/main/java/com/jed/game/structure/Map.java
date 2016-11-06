package com.jed.game.structure;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private final int width;
    private final int height;

    private int[] spawn;

    private List<Tile> tiles;

    @Nullable
    public Tile getTile(int x, int y) {
        for(Tile t : tiles) {
            int[] pos = t.getPos();
            if(pos[0] == x && pos[1] == y) {
                return t;
            }
        }
        return null;
    }

    public int[] getSpawn() {
        return spawn;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private int getTileIdx(int x, int y) {
        for(int i = 0; i < tiles.size(); i++) {
            int[] pos = tiles.get(i).getPos();
            if(pos[0] == x && pos[1] == y) {
                return i;
            }
        }
        return -1;
    }

    public void setTile(int x, int y, Tile tile) {
        int idx = getTileIdx(x, y);
        if(idx >= 0) {
            // map has a tile in (x, y) - replace
            tiles.set(idx, tile);
        } else {
            // map doesn't have a tile in (x, y) - add new
            tiles.add(tile);
        }
    }

    public Map(int width, int height, int[] spawn, List<Tile> tiles) {
        this.width = width;
        this.height = height;
        this.spawn = new int[2];
        try {
            this.spawn[0] = spawn[0];
            this.spawn[1] = spawn[1];
        } catch(Exception e) {
            // set default spawn position in case we don't provide a valid one
            this.spawn[0] = 0;
            this.spawn[0] = 0;
        }
        this.tiles = new ArrayList<Tile>();
        for(Tile t : tiles) {
            this.tiles.add(t);
        }
    }
}
