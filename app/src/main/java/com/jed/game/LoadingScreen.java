package com.jed.game;

import android.graphics.Color;
import android.graphics.Paint;

import com.jed.game.structure.Level;
import com.jed.game.structure.Map;
import com.jed.game.structure.Setting;
import com.jed.game.structure.Terrain;
import com.jed.game.structure.Tile;
import com.kilobolt.framework.FileIO;
import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.Graphics.ImageFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    boolean error = false;
    String  errorText = null;

    @Override
    public void update(float deltaTime) {
        if(error) {
            return;
        }
        Graphics g = game.getGraphics();
//        Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);
//        Assets.click = game.getAudio().createSound("explode.ogg");
//        g.drawImage(Assets.menu, 250, 500);

        // LOAD SHIT HERE

        // load settings
        try {
            parseSettings("settings.cfg");
        } catch(IOException e) {
            return;
        }

        String firstLevel = null;
        String musicFile = null;
        int    musicVol  = 0;
        try {
            firstLevel = getStringSetting("firstLevel");
            musicFile  = getStringSetting("music");
            musicVol   = getIntSetting("musicVol");
        } catch(IOException e) {
            return;
        }

        // load music
        Assets.music = game.getAudio().createMusic(musicFile);
        Assets.music.setLooping(true);
        Assets.music.setVolume((float)musicVol / 100.f);

        // load textures
        Assets.img_terr_dirt = game.getGraphics().newImage("textures/terrain_dirt.png", Graphics.ImageFormat.RGB565, 128, 120);
        Assets.img_terr_none = game.getGraphics().newImage("textures/terrain_none.png", Graphics.ImageFormat.RGB565, 128, 120);

        // load player
        int player_width  = 136;
        int player_height = 216;
        for(int i = 0; i < 4; i++) {
            Assets.player[i] = game.getGraphics().newImageCropped("sprites/player.png", ImageFormat.ARGB4444, i * player_width, 0, player_width, player_height, 81, 120);
        }

        // load terrains
        Terrain voidTerrain = new Terrain("Vd", Assets.img_terr_none, false);
        Terrain dirtTerrain = new Terrain("Di", Assets.img_terr_dirt, true);
        Assets.terrains.add(voidTerrain);
        Assets.terrains.add(dirtTerrain);

        try {
            Level level = parseLevel(firstLevel);
            Assets.levels.add(level);
        } catch(IOException e) {
            return;
        }

/*        // make test level
        String[] lvl1_layout = {
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di",
                "Di", "Di", "Di", "Vd", "Vd", "Di", "Di", "Di",
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di",
                "Di", "Vd", "Di", "Di", "Di", "Di", "Vd", "Di",
                "Di", "Vd", "Di", "Di", "Di", "Di", "Vd", "Di",
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di",
                "Di", "Di", "Di", "Vd", "Vd", "Di", "Di", "Di",
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di",
        };
        int mapWidth  = 8;
        int mapHeight = 8;
        List<List<String>> lvl1_strListList = new ArrayList<>();
        List<String> buffer = new ArrayList<>();
        int x = 0, y = 0;
        for(String str : lvl1_layout) {
            if(y > mapHeight) {
                lvl1_strListList.add(buffer);
                break;
            }
            if(x >= mapWidth) {
                lvl1_strListList.add(buffer);
                x = 0;
                y++;
                buffer = new ArrayList<>();
            }
            buffer.add(str);
            x++;
        }
        if(buffer.size() > 0) {
            lvl1_strListList.add(buffer);
        }
        List<Tile> lvl1_tiles = Util.parseTiles(lvl1_strListList);
        Map lvl1_map = new Map(mapWidth, mapHeight, new int[]{1, 1}, lvl1_tiles);

        Level level1 = new Level("Level 1", null, lvl1_map);
        Assets.levels.add(level1); */


        // load screens
        Screens.mainMenuScreen = new MainMenuScreen(game);
        Screens.gameScreen     = null;

        // show menu
        game.setScreen(Screens.mainMenuScreen);
    }

    public List<String> readLines(String filename) throws IOException {
        FileIO fileIO = game.getFileIO();
        InputStream stream = fileIO.readAsset(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        List<String> lines = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    public void parseSettings(String filename) throws IOException {
        List<String> lines = null;
        try {
            lines = readLines(filename);
        } catch(IOException e) {
            error = true;
            errorText = "ERROR: could not load file: " + filename;
            throw(e);
        }
        for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] splitLine = line.split("[\\t ]", 2);
            if(splitLine.length == 2) {
                Assets.setSetting(splitLine[0], splitLine[1]);
            }
        }
    }

    public String getStringSetting(String name) throws IOException {
        Setting s = Assets.getSetting(name);
        if(s == null) {
            error = true;
            errorText = "ERROR: could not find setting: " + name;
            throw new IOException();
        }
        return s.getValue();
    }

    public int getIntSetting(String name) throws IOException {
        Setting s = Assets.getSetting(name);
        if(s == null) {
            error = true;
            errorText = "ERROR: could not find setting: " + name;
            throw new IOException();
        }
        int n = 0;
        try {
            n = Integer.parseInt(s.getValue());
        } catch(NumberFormatException e) {
            error = true;
            errorText = "ERROR: value for setting '" + name + "' is not a valid integer: " + s.getValue();
            throw new IOException();
        }
        return n;
    }

    public Level parseLevel(String level) throws IOException {
        List<String> lines = null;
        try {
            lines = readLines("levels/" + level + ".lvl");
        } catch(IOException e) {
            error = true;
            errorText = "ERROR: could not load file: levels/" + level + ".lvl";
            throw(e);
        }
        String name = null;
        String next = null;
        int width = 0, height = 0, spawnx = 0, spawny = 0;
        for(int i = 0; i < lines.size(); i++) {
            String[] splitLine = lines.get(i).split("[\\t ]", 2);
            if(splitLine.length == 2) {
                if(splitLine[0].equals("name")) {
                    name = splitLine[1];
                } else if(splitLine[0].equals("next")) {
                    next = splitLine[1];
                } else if(splitLine[0].equals("width")) {
                    width = Integer.parseInt(splitLine[1]);
                } else if(splitLine[0].equals("height")) {
                    height = Integer.parseInt(splitLine[1]);
                } else if(splitLine[0].equals("spawnx")) {
                    spawnx = Integer.parseInt(splitLine[1]);
                } else if(splitLine[0].equals("spawny")) {
                    spawny = Integer.parseInt(splitLine[1]);
                }
            }
        }
        int[] spawn = {spawnx, spawny};
        Map map = parseMap(level, width, height, spawn);
        Level l = new Level(name, next, map);
        return l;
    }

    public Map parseMap(String level, int width, int height, int[] spawn) throws IOException {
        List<String> lines = null;
        try {
            lines = readLines("levels/" + level + ".map");
        } catch(IOException e) {
            error = true;
            errorText = "ERROR: could not load file: levels/" + level + ".map";
            throw(e);
        }
        int y = 0;
        List<List<String>> stringListList = new ArrayList<>();
        List<String> buffer = new ArrayList<>();
        for(int i = 0; i < lines.size(); i++) {
            String[] splitLine = lines.get(i).split("[\\t ]");
            if(splitLine.length > 0) {
                for(int j = 0; j < splitLine.length; j++) {
                    buffer.add(splitLine[j]);
                }
                stringListList.add(buffer);
                buffer = new ArrayList<>();
                y++;
            }
        }
        List<Tile> tileList = Util.parseTiles(stringListList);
        Map map = new Map(width, height, spawn, tileList);
        return map;
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawARGB(255, 50, 100, 200);

        Paint p = new Paint();
        p.setColor(Color.rgb(150, 200, 255));
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(144);

        Paint p2 = new Paint();
        p2.setColor(Color.rgb(150, 200, 255));
        p2.setTextAlign(Paint.Align.CENTER);
        p2.setTextSize(112);

        Paint p3 = new Paint();
        p3.setColor(Color.WHITE);
        p3.setTextAlign(Paint.Align.LEFT);
        p3.setTextSize(36);

        if(error) {
            g.drawStringRel("Something Happened", 0.5, 0.4, p);
            g.drawStringRel("(Something Happened)", 0.5, 0.55, p2);
            g.drawStringRel(errorText, 0.05, 0.7, p3);
        } else {
            g.drawStringRel("Loading...", 0.5, 0.5, p);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}
