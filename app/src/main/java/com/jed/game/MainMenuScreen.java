package com.jed.game;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.Input.TouchEvent;
import com.kilobolt.framework.implementation.AndroidGraphics;

public class MainMenuScreen extends Screen {
    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            try {
                TouchEvent event = touchEvents.get(i);
                if (event.type == TouchEvent.TOUCH_UP) {
                    if (inBounds(event, 0, 0, g.getWidth() / 4, g.getHeight() / 4)) {
                        // START GAME
                        game.setScreen(new GameScreen(game));
                    } else if(inBounds(event, g.getWidth() * 3 / 4, 0, g.getWidth() / 4, g.getHeight() / 4)) {
                        // QUIT
                        System.exit(0);
                    }
                }
            } catch(Exception e) {
                    // pass
            }
        }
    }


    private boolean inBounds(TouchEvent event, int x, int y, int width,
                             int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    }


    @Override
    public void paint(float deltaTime) {
        // MAIN MENU HERE
        Graphics g = game.getGraphics();
//        g.drawImage(Assets.menu, 0, 0);

        // set white background
        g.drawARGB(255, 255, 255, 255);

        // make paint for big texts
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(48);
        p.setAntiAlias(true);
        p.setTextAlign(Paint.Align.CENTER);

        // make "PLAY" button
        g.drawRect(0, 0, g.getWidth() / 4, g.getHeight() / 4, Color.BLUE);
        g.drawString("PLAY", (0 + g.getWidth() / 4) / 2, (0 + g.getHeight() / 4) / 2, p);

        // make "QUIT" button
        g.drawRect(g.getWidth() * 3 / 4, 0, g.getWidth() / 4, g.getHeight() / 4, Color.RED);
        g.drawString("QUIT", (g.getWidth() * 3 / 4 + g.getWidth()) / 2, (0 + g.getHeight() / 4) / 2, p);
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
        // Display "Exit Game?" Box
    }
}
