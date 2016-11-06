package com.jed.game;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import com.jed.game.structure.Level;
import com.jed.game.structure.Map;
import com.jed.game.structure.Tile;
import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.Input.TouchEvent;
import com.kilobolt.framework.implementation.FrameBufferSize;


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

    // game rendering

    private final int grid_cols = 15;
    private final int grid_rows = 9;

    private int cell_width = g.getWidth() / grid_cols;
    private int cell_height = g.getHeight() / grid_rows;

    // position where the player is shown on the screen
    private int posx_shown = grid_cols / 2;
    private int posy_shown = grid_rows / 2;

    // player position
    private int posx;
    private int posy;

    // level
    private Level curLevel;
    private Map   curMap;

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
        // load test level
        curLevel = Assets.levels.get(0);
        curMap = curLevel.getMap();

        posx = curMap.getSpawn()[0];
        posy = curMap.getSpawn()[1];

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
                // CONTROLS

                // UP
                if(Util.inBounds(event, 2 * cell_width, FrameBufferSize.height - 4 * cell_height, cell_width, cell_height)) {
                    // move up
                    try_move(posx, posy - 1);
                } else
                // DOWN
                if(Util.inBounds(event, 2 * cell_width, FrameBufferSize.height - 2 * cell_height, cell_width, cell_height)) {
                    // move down
                    try_move(posx, posy + 1);
                } else
                // LEFT
                if(Util.inBounds(event, 1 * cell_width, FrameBufferSize.height - 3 * cell_height, cell_width, cell_height)) {
                    // move left
                    try_move(posx - 1, posy);
                } else
                // RIGHT
                if(Util.inBounds(event, 3 * cell_width, FrameBufferSize.height - 3 * cell_height, cell_width, cell_height)) {
                    // move right
                    try_move(posx + 1, posy);
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
                if(Util.inBoundsRel(event, 0, 0.875, 0.125, 0.125)) {
                    // resume button
                    if(System.currentTimeMillis() - lastPaused >= stateChangeDelay) {
                        resume();
                    }
                } else if(Util.inBoundsRel(event, 0.4375 /* 7 / 16 - center if width is 1 / 8 */, 0.875, 0.125, 0.125)) {
                    // menu button
                    game.setScreen(Screens.mainMenuScreen);
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
                    game.setScreen(Screens.mainMenuScreen);
                    return;
                }
            }
        }

    }

    // show test level

    private void draw_cell(int x, int y, Image terrain) {
        g.drawImage(terrain, x * cell_width, y * cell_height, 0, 0, cell_width, cell_height);
    }

    private void draw_item(int x, int y, int width, int height, Image image) {
        if(Math.abs(x - posx) > grid_cols / 2 || Math.abs(y - posy) > grid_rows / 2) {
            return;
        }
        double scale_factor = 1; // don't keep different factors for X and Y axes to keep proportions
        if(width > height) {
            scale_factor = (double)height / (double)width;
        } else if(width < height) {
            scale_factor = (double)width / (double)height;
        }
        g.drawScaledImageRel(image, (double)(posx_shown + x - posx) / (double)grid_cols, (double)(posy_shown + y - posy) / (double)grid_rows, scale_factor / (double)grid_cols, scale_factor / (double)grid_rows, 0, 0, width, height);
    }

    private void try_move(int x, int y) {
        if(x < 0 || x >= curMap.getWidth() || y < 0 || y >= curMap.getHeight()) {
            return;
        }
        Tile t = curMap.getTile(x, y);
        if(t == null || !t.getTerrain().isReachable()) {
            return;
        }
        posx = x;
        posy = y;
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        // First draw the game elements.

        // test
        // draw map
        for(int x = 0; x < grid_cols; x++) {
            for(int y = 0; y < grid_rows; y++) {
                Tile tile = curMap.getTile(x - posx_shown + posx, y - posy_shown + posy);
                if(tile != null) {
                    draw_cell(x, y, tile.getTerrain().getTexture());
                } else {
                    draw_cell(x, y, Assets.getTerrain("Vd").getTexture());
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
        p.setTextSize(72);
        p.setAntiAlias(true);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.BLUE);

        // CONTROLS

        // UP
        g.drawRect(2 * cell_width, FrameBufferSize.height - 4 * cell_height, cell_width, cell_height, AlphaColor.white);
        g.drawString("^", (int)(2.5 * cell_width), (int)(FrameBufferSize.height - 3.25 * cell_height), p);

        // DOWN
        g.drawRect(2 * cell_width, FrameBufferSize.height - 2 * cell_height, cell_width, cell_height, AlphaColor.white);
        g.drawString("v", (int)(2.5 * cell_width), (int)(FrameBufferSize.height - 1.25 * cell_height), p);

        // LEFT
        g.drawRect(1 * cell_width, FrameBufferSize.height - 3 * cell_height, cell_width, cell_height, AlphaColor.white);
        g.drawString("<", (int)(1.5 * cell_width), (int)(FrameBufferSize.height - 2.25 * cell_height), p);

        // RIGHT
        g.drawRect(3 * cell_width, FrameBufferSize.height - 3 * cell_height, cell_width, cell_height, AlphaColor.white);
        g.drawString(">", (int)(3.5 * cell_width), (int)(FrameBufferSize.height - 2.25 * cell_height), p);
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
