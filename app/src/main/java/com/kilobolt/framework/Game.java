package com.kilobolt.framework;

public interface Game {
    public Audio    getAudio();
    public Input    getInput();
    public FileIO   getFileIO();
    public Graphics getGraphics();
    public void     setScreen(Screen screen);
    public Screen   getCurrentScreen();
    public Screen   getInitScreen();

    public int      getState();
    public boolean  Resumed();
    public int      getPosx();
    public int      getPosy();

    public void     setState(int state);
    public void     setResumed(boolean resumed);
    public void     setPosx(int posx);
    public void     setPosy(int posy);
}