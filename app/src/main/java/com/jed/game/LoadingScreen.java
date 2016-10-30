package com.jed.game;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.Graphics.ImageFormat;

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

        // load music
        Assets.music = game.getAudio().createMusic("music.mp3");
        Assets.music.setLooping(true);
        Assets.music.setVolume(0.85f);

        // load textures
        Assets.dirt = game.getGraphics().newImage("textures/terrain_dirt.png", Graphics.ImageFormat.RGB565);
        Assets.none = game.getGraphics().newImage("textures/terrain_none.png", Graphics.ImageFormat.RGB565);
        Assets.player = game.getGraphics().newImage("sprites/player_left.png", ImageFormat.ARGB4444);

        game.setScreen(new MainMenuScreen(game));
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
