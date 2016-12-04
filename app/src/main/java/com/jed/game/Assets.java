package com.jed.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.jed.game.structure.Level;
import com.jed.game.structure.Setting;
import com.jed.game.structure.Terrain;
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Music;
import com.kilobolt.framework.Sound;

import java.util.ArrayList;
import java.util.List;

public class Assets {
    public static Music music;

    public static Image[] player = new Image[4];

    public static Terrain default_terrain;

//    public static List<Terrain> terrains = new ArrayList<Terrain>();
    public static List<Level>   levels   = new ArrayList<Level>();

    public static List<Setting> settings = new ArrayList<Setting>();

    public static Bundle instanceState;

/*    @Nullable
    public static Terrain getTerrain(String name) {
        for(Terrain t : terrains) {
            if(t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    } */

    public static Setting getSetting(String name) {
        for(Setting s : settings) {
            if(s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    public static void setSetting(String name, String value) {
        Setting s = getSetting(name);
        if(s == null) {
            s = new Setting(name, value);
            settings.add(s);
        } else {
            s.setValue(value);
        }
    }
}