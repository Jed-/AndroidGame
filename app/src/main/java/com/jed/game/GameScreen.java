package com.jed.game;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.Input.TouchEvent;


public class GameScreen extends Screen {
    private enum GameState {
        Ready, Running, Paused, GameOver
    }

    private GameState state = GameState.Ready;

    // Variable Setup
    // You would create game objects here.

    private Graphics g = game.getGraphics();

    private int livesLeft = 1;
    private Paint paint;

    private float fps           = 0;
    private int   frame         = 0;
    private long  lastmillis    = System.currentTimeMillis();
    private long  lastfpsupdate = 0;

    private long lastPaused = 0;
    private long lastResumed = 0;

    // to avoid resuming immediately after pausing, like, in the same frame
    private int stateChangeDelay = 20;

    public GameScreen(Game game) {
        super(game);

        // Initialize game objects here
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(24);

        Assets.music.play();

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

    /* private boolean inBounds(TouchEvent event, int x, int y, int width,
                             int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    } */

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        //This is identical to the update() method from our Unit 2/3 game.

        // 1. All touch input is handled here:
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            // game controls
/*            if (event.type == TouchEvent.TOUCH_DOWN) {

                if (event.x <= g.getWidth() / 2) {
                    // Move left.
                }

                else if (event.x > g.getWidth() / 2) {
                    // Move right.
                }

            } */

            if (event.type == TouchEvent.TOUCH_UP) {

//                if(Util.inBoundsRel(event, 0, 0.875, 0.125, 0.125)) {
//                    // pause button
//                    if(System.currentTimeMillis() - lastResumed > stateChangeDelay) {
//                        pause();
//                    }
//                }

                // game controls released
                /* else if (event.x <= g.getWidth() / 2) {
                    // Stop moving left.
                } else if (event.x > g.getWidth() / 2) {
                    // Stop moving right. }
                } */
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
                if(Util.inBoundsRel(event, 0, 0.875, 0.125, 0.125)) {
                    // resume button
                    if(System.currentTimeMillis() - lastPaused >= stateChangeDelay) {
                        resume();
                    }
                } else if(Util.inBoundsRel(event, 0.4375 /* 7 / 16 - center if width is 1 / 8 */, 0.875, 0.125, 0.125)) {
                    // menu button
                    game.setScreen(new MainMenuScreen(game));
                } else if(Util.inBoundsRel(event, 0.875, 0.875, 0.125, 0.125)) {
                    // quit button
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
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

    // test
    private int posx = 4, posy = 4;
    private final int grid_cols = 32;
    private int cell_size = g.getWidth() / grid_cols;
    private final int grid_rows = (int)Math.ceil(g.getHeight() / cell_size);

    private int posx_shown = grid_cols / 2;
    private int posy_shown = grid_rows / 2;

    private void draw_cell(int x, int y, Image terrain) {
        g.drawScaledImageRel(terrain, (double)x / (double)grid_cols, (double)y / (double)grid_rows, 1.0 / (double)grid_cols, 1.0 / (double)grid_rows, 0, 0, 152, 152);
    }

    private void draw_item(int x, int y, int width, int height, Image image) {
        if(Math.abs(x - posx) > grid_cols / 2 || Math.abs(y - posy) > grid_rows / 2) {
            return;
        }
        double x_factor = 1, y_factor = 1;
        if(width > height) {
            y_factor = (double)height / (double)width;
        } else if(width < height) {
            x_factor = (double)width / (double)height;
        }
        g.drawScaledImageRel(image, (double)(posx_shown + x - posx) / (double)grid_cols, (double)(posy_shown + y - posy) / (double)grid_rows, x_factor / (double)grid_cols, y_factor / (double)grid_rows, 0, 0, width, height);
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        // First draw the game elements.

        // test
        // draw map
        for(int x = 0; x < grid_cols; x++) {
            for(int y = 0; y < grid_rows; y++) {
                if((x - posx_shown + posx) >= 0 && (x - posx_shown + posx) < 8 && (y - posy_shown + posy) >= 0 && (y - posy_shown + posy) < 8) {
                    draw_cell(x, y, Assets.dirt);
                } else {
                    draw_cell(x, y, Assets.none);
                }
            }
        }
        // draw player
        draw_item(posx, posy, 152, 115, Assets.player);

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

        g.drawString("frame: " + frame + "\nfps: " + String.format("%.1f", fps) + "\nstate: " + state.name(), g.getWidth() / 64, g.getHeight() / 32, paint);

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

//        // make pause button
//        g.drawRectRel(0, 0.875, 0.125, 0.125, AlphaColor.blue);
//        g.drawStringRel("PAUSE", (0 + 0.125) / 2, (0.875 + 1) / 2, p);
    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(155, 0, 0, 0);

        Paint p = new Paint();
        p.setTextSize(48);
        p.setAntiAlias(true);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(AlphaColor.white);

        // make resume button (bottom left)
        g.drawRectRel(0, 0.875, 0.125, 0.125, AlphaColor.blue);
        g.drawStringRel("RESUME", (0 + 0.125) / 2, (0.875 + 1) / 2, p);

        // make quit button (bottom right)
        g.drawRectRel(0.875, 0.875, 0.125, 0.125, AlphaColor.red);
        g.drawStringRel("QUIT", (0.875 + 1) / 2, (0.875 + 1) / 2, p);

        // make menu button (bottom center)
        g.drawRectRel(0.4375 /* 7 / 16, center if width is 1 / 8 */, 0.875, 0.125, 0.125, AlphaColor.green);
        g.drawStringRel("MENU", (0.4375 + 0.5625 /* 9 / 16 */) / 2, (0.875 + 1) / 2, p);

    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString("GAME OVER.", 640, 300, paint);

    }

    @Override
    public void pause() {
        state = GameState.Paused;
        lastPaused = System.currentTimeMillis();

        Assets.music.pause();
    }

    @Override
    public void resume() {
        if(state == GameState.Paused) {
            state = GameState.Running;
        }
        lastResumed = System.currentTimeMillis();

        Assets.music.play();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        pause();
    }
}
