package com.jed.game;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.Input.TouchEvent;
import com.kilobolt.framework.implementation.AndroidGame;


public class GameScreen extends Screen {
    enum GameState {
        Ready, Running, Paused, GameOver
    }

    GameState state = GameState.Ready;

    // Variable Setup
    // You would create game objects here.

    Graphics g = game.getGraphics();

    int livesLeft = 1;
    Paint paint;

    float fps = 0;

    int  frame         = 0;
    long lastmillis    = System.currentTimeMillis();
    long lastfpsupdate = 0;

    public GameScreen(Game game) {
        super(game);

        // Initialize game objects here
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(24);

    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        // We have four separate update methods in this example.
        // Depending on the state of the game, we call different update methods.
        // Refer to Unit 3's code. We did a similar thing without separating the
        // update methods.

        if (state == GameState.Ready)
            updateReady(touchEvents);
        if (state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if (state == GameState.Paused)
            updatePaused(touchEvents);
        if (state == GameState.GameOver)
            updateGameOver(touchEvents);

        // make black background
        g.drawARGB(255, 0, 0, 0);

        // print fps
        long newmillis = System.currentTimeMillis();
        if(frame > 0 && newmillis - lastfpsupdate >= 1000) {
            fps = 1000.f / (newmillis - lastmillis);
            lastfpsupdate = newmillis;
        }
        frame++;
        g.drawString("frame: " + frame + "\nfps: " + String.format("%.1f", fps), g.getWidth() / 64, g.getHeight() / 32, paint);
        lastmillis = newmillis;
    }

    private void updateReady(List<TouchEvent> touchEvents) {

        // This example starts with a "Ready" screen.
        // When the user touches the screen, the game begins.
        // state now becomes GameState.Running.
        // Now the updateRunning() method will be called!

//        if (touchEvents.size() > 0)
//            state = GameState.Running;

        // start game
        if(state != GameState.Running) {
            state = GameState.Running;
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

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        //This is identical to the update() method from our Unit 2/3 game.

        // 1. All touch input is handled here:
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            if (event.type == TouchEvent.TOUCH_DOWN) {

                if (event.x <= g.getWidth() / 2) {
                    // Move left.
                }

                else if (event.x > g.getWidth() / 2) {
                    // Move right.
                }

            }

            if (event.type == TouchEvent.TOUCH_UP) {

                // check quit button
                if(inBounds(event, g.getWidth() * 7 / 8, g.getHeight() * 7 / 8, g.getWidth() / 8, g.getHeight() / 8)) {
                    // nullify();
                    dispose();
                    game.setScreen(new MainMenuScreen(game));
                }

                if (event.x <= g.getWidth() / 2) {
                    // Stop moving left.
                }

                else if (event.x > g.getWidth() / 2) {
                    // Stop moving right. }
                }
            }
        }
        // 2. Check miscellaneous events like death:

        if (livesLeft == 0) {
            state = GameState.GameOver;
        }

        // 3. Call individual update() methods here.
        // This is where all the game updates happen.
        // For example, robot.update();
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {

            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 300 && event.x < 980 && event.y > 100
                        && event.y < 500) {
                    nullify();
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }

    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        // First draw the game elements.

        // Example:
        // g.drawImage(Assets.background, 0, 0);
        // g.drawImage(Assets.character, characterX, characterY);

        // Secondly, draw the UI above the game elements.
        if (state == GameState.Ready)
            drawReadyUI();
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Paused)
            drawPausedUI();
        if (state == GameState.GameOver)
            drawGameOverUI();

    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        paint = null;

        // Call garbage collector to clean up memory.
        System.gc();
    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap each side of the screen to move in that direction.",
                640, 300, paint);

    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();

        Paint p = new Paint();
        p.setTextSize(48);
        p.setAntiAlias(true);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.WHITE);

        // make back button
        g.drawRect(g.getWidth() * 7 / 8, g.getHeight() * 7 / 8, g.getWidth() / 8, g.getHeight() / 8, Color.RED);
        g.drawString("BACK", (g.getWidth() * 7 / 8 + g.getWidth()) / 2, (g.getHeight() * 7 / 8 + g.getHeight()) / 2, p);

    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(155, 0, 0, 0);

    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString("GAME OVER.", 640, 300, paint);

    }

    @Override
    public void pause() {
        if (state == GameState.Running)
            state = GameState.Paused;

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        // nullify();
        dispose();
        game.setScreen(new MainMenuScreen(game));
    }
}
