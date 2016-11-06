package com.jed.game;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.Input.TouchEvent;

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
                    if (Util.inBoundsRel(event, 0, 0, 0.25, 0.25)) {
                        // START GAME
                        if(Screens.gameScreen == null) {
                            Screens.gameScreen = new GameScreen(game);
                        }
                        game.setScreen(Screens.gameScreen);
                    } else if(Util.inBoundsRel(event, 0.75, 0, 0.25, 0.25)) {
                        // QUIT
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }
            } catch(Exception ignored) {
            }
        }
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
        g.drawRectRel(0, 0, 0.25, 0.25, Color.BLUE);
        g.drawStringRel("PLAY", (0 + 0.25) / 2, (0 + 0.25) / 2, p);

        // make "QUIT" button
        g.drawRectRel(0.75, 0, 0.25, 0.25, Color.RED);
        g.drawStringRel("QUIT", (0.75 + 1) / 2, (0 + 0.25) / 2, p);
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
