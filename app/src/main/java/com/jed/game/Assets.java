package com.jed.game;

import android.support.annotation.Nullable;

import com.jed.game.structure.Level;
import com.jed.game.structure.Terrain;
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Music;
import com.kilobolt.framework.Sound;

import java.util.ArrayList;
import java.util.List;

public class Assets {

    public static Image menu;
    public static Sound click;
    public static Music music;

    public static Image img_terr_dirt;
    public static Image img_terr_none;
    public static Image player;

    public static List<Terrain> terrains = new ArrayList<Terrain>();
    public static List<Level>   levels   = new ArrayList<Level>();

    @Nullable
    public static Terrain getTerrain(String name) {
        for(Terrain t : terrains) {
            if(t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }
}