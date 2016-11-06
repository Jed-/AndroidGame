package com.jed.game.structure;

import com.jed.game.Assets;

public class Tile {
    // each 'square' on a map is a tile. Tiles always have a terrain, and may have items and special properties
    private final int x;
    private final int y;

    private Terrain terrain;

    public int[] getPos() {
        int[] pos = new int[2];
        pos[0] = x;
        pos[1] = y;
        return pos;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public void setTerrain(String terrainName) {
        this.terrain = Assets.getTerrain(terrainName);
    }

    public Tile(int x, int y, Terrain terrain) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
    }

    public Tile(int x, int y, String terrainName) {
        this.x = x;
        this.y = y;
        this.terrain = Assets.getTerrain(terrainName);
    }
}
