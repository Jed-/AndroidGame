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
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.Graphics.ImageFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

        // load settings
        if(game.getState() == 0) {
            try {
                parseSettings("settings.cfg");
            } catch (IOException e) {
                return;
            }
            String firstLevel = null;
            String musicFile = null;
            String defaultTerrainImageName = null;
            int musicVol = 0;
            try {
                firstLevel = getStringSetting("firstLevel");
                musicFile = getStringSetting("music");
                musicVol = getIntSetting("musicVol");

                defaultTerrainImageName = getStringSetting("defaultTerrain");
            } catch (IOException e) {
                return;
            }

            // load default terrain
            Assets.default_terrain = new Terrain(null, game.getGraphics().newImage("textures/terrains/" + defaultTerrainImageName, ImageFormat.RGB565, 128, 120), false);

            // load music
            Assets.music = game.getAudio().createMusic(musicFile);
            Assets.music.setLooping(true);
            Assets.music.setVolume((float) musicVol / 100.f);

            // load textures
            //        Assets.img_terr_dirt = game.getGraphics().newImage("textures/terrain_dirt.png", Graphics.ImageFormat.RGB565, 128, 120);
            //        Assets.img_terr_none = game.getGraphics().newImage("textures/terrain_none.png", Graphics.ImageFormat.RGB565, 128, 120);

            // load player
            int player_width = 136;
            int player_height = 216;
            for (int i = 0; i < 4; i++) {
                Assets.player[i] = game.getGraphics().newImageCropped("sprites/player.png", ImageFormat.ARGB4444, i * player_width, 0, player_width, player_height, 81, 120);
            }

            // load terrains
            //        Terrain voidTerrain = new Terrain("Vd", Assets.img_terr_none, false);
            //        Terrain dirtTerrain = new Terrain("Di", Assets.img_terr_dirt, true);
            //        Assets.terrains.add(voidTerrain);
            //        Assets.terrains.add(dirtTerrain);

            // parse first level
            try {
                Level level = parseLevel(firstLevel);
                Assets.levels.add(level);
            } catch (IOException e) {
                return;
            }
        }

        // load screens
        Screens.mainMenuScreen = new MainMenuScreen(game);
        Screens.gameScreen = null;

        if(game.getState() == 2) {
            Screens.gameScreen = new GameScreen(game);
            game.setScreen(Screens.gameScreen);
        } else {
            // show menu
            game.setState(1);
            game.setScreen(Screens.mainMenuScreen);
        }
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
        List<Terrain> terrains = parseTerrains(name);
        Map map = parseMap(level, terrains, width, height, spawn);
        Level l = new Level(name, next, map);
        return l;
    }

    public Map parseMap(String level, List<Terrain> terrains, int width, int height, int[] spawn) throws IOException {
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
            if(splitLine.length >= 2) {
                for(int j = 0; j < splitLine.length; j++) {
                    buffer.add(splitLine[j]);
                }
                stringListList.add(buffer);
                buffer = new ArrayList<>();
                y++;
            }
        }
        List<Tile> tileList = Util.parseTiles(stringListList, terrains);
        Map map = new Map(width, height, spawn, tileList);
        return map;
    }

    public List<Terrain> parseTerrains(String levelName) throws IOException {
        List<String> lines = null;
        try {
            lines = readLines("levels/" + levelName + ".ter");
        } catch(IOException e) {
            error = true;
            errorText = "ERROR: could not load file: levels/" + levelName + ".ter";
            throw(e);
        }
        List<Terrain> terrainList = new ArrayList<>();
        for(int i = 0; i < lines.size(); i++) {
            String[] splitLine = lines.get(i).split("[\\t ]", 3);
            if (splitLine.length >= 2) {
                Image image = game.getGraphics().newImage("textures/terrains/" + splitLine[1], ImageFormat.RGB565, 128, 120);
                Terrain t = null;
                if (splitLine.length == 2) {
                    t = new Terrain(splitLine[0], image, true);
                } else {
                    try {
                        int reachable = Integer.parseInt(splitLine[2]);
                        t = new Terrain(splitLine[0], image, reachable != 0);
                    } catch (NumberFormatException e) {
                        error = true;
                        errorText = "ERROR: in file 'levels/" + levelName + ".ter', line " + (i + 1) + ": '" + splitLine + "' is not a valid number!";
                        throw new IOException();
                    }
                }
                if(t != null) {
                    terrainList.add(t);
                }
            }
        }
        return terrainList;
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawARGB(255, 50, 100, 200);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.rgb(150, 200, 255));
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(144);

        Paint p2 = new Paint();
        p2.setAntiAlias(true);
        p2.setColor(Color.rgb(150, 200, 255));
        p2.setTextAlign(Paint.Align.CENTER);
        p2.setTextSize(112);

        Paint p3 = new Paint();
        p3.setAntiAlias(true);
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
