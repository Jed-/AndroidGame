package com.jed.game.structure;

import com.kilobolt.framework.Image;

public class Terrain {
    private String  name;
    private Image   texture;
    private boolean reachable;

    public String getName() {
        return name;
    }

    public Image getTexture() {
        return texture;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public Terrain(String name, Image texture) {
        this.name = name;
        this.texture = texture;
        this.reachable = true;
    }

    public Terrain(String name, Image texture, boolean reachable) {
        this.name = name;
        this.texture = texture;
        this.reachable = reachable;
    }
}
