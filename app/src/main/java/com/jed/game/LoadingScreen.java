package com.jed.game;

import android.graphics.Color;
import android.graphics.Paint;

import com.jed.game.structure.Level;
import com.jed.game.structure.Map;
import com.jed.game.structure.Terrain;
import com.jed.game.structure.Tile;
import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.Graphics.ImageFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
//        Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);
//        Assets.click = game.getAudio().createSound("explode.ogg");
//        g.drawImage(Assets.menu, 250, 500);

        // LOAD SHIT HERE
        g.drawARGB(255, 0, 0, 0);

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(144);

        g.drawStringRel("Loading...", 0.5, 0.5, p);

        // load music
        Assets.music = game.getAudio().createMusic("music.mp3");
        Assets.music.setLooping(true);
        Assets.music.setVolume(0.85f);

        // load textures
        Assets.img_terr_dirt = game.getGraphics().newImage("textures/terrain_dirt.png", Graphics.ImageFormat.RGB565, 128, 120);
        Assets.img_terr_none = game.getGraphics().newImage("textures/terrain_none.png", Graphics.ImageFormat.RGB565, 128, 120);
        Assets.player = game.getGraphics().newImage("sprites/player_left.png", ImageFormat.ARGB4444);

        Screens.mainMenuScreen = new MainMenuScreen(game);
        Screens.gameScreen     = new GameScreen(game);

        // load terrains
        Terrain voidTerrain = new Terrain("Vd", Assets.img_terr_none, false);
        Terrain dirtTerrain = new Terrain("Di", Assets.img_terr_dirt, true);
        Assets.terrains.add(voidTerrain);
        Assets.terrains.add(dirtTerrain);

        // make test level
        String[] lvl1_layout = {
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di",
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di",
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di",
                "Di", "Vd", "Di", "Di", "Di", "Di", "Vd", "Di",
                "Di", "Vd", "Di", "Di", "Di", "Di", "Vd", "Di",
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di",
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di",
                "Di", "Di", "Di", "Di", "Di", "Di", "Di", "Di"
        };
        int mapWidth  = 8;
        int mapHeight = 8;
        List<List<String>> lvl1_strListList = new ArrayList<>();
        List<String> buffer = new ArrayList<>();
        int x = 0, y = 0;
        for(String str : lvl1_layout) {
            if(y >= mapHeight) {
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
        List<Tile> lvl1_tiles = Util.parseTiles(lvl1_strListList);
        Map lvl1_map = new Map(mapWidth, mapHeight, new int[]{1, 1}, lvl1_tiles);

        Level level1 = new Level("Level 1", null, lvl1_map);
        Assets.levels.add(level1);

        // show menu
        game.setScreen(Screens.mainMenuScreen);
    }

    @Override
    public void paint(float deltaTime) {

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
