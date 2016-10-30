package com.kilobolt.framework;

import android.graphics.Paint;

public interface Graphics {
    public static enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Image newImage(String fileName, ImageFormat format);

    public void clearScreen(int color);

    public void drawLine(int x, int y, int x2, int y2, int color);
    public void drawLineRel(double x, double y, double x2, double y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);
    public void drawRectRel(double x, double y, double width, double height, int color);

    public void drawImage(Image image, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
    public void drawImageRel(Image image, double x, double y, int srcX, int srcY, int srcWidth, int srcHeight);

    public void drawImage(Image image, int x, int y);
    public void drawImageRel(Image image, double x, double y);

    public void drawString(String text, int x, int y, Paint paint);
    public void drawStringRel(String text, double x, double y, Paint paint);

    public void drawScaledImage(Image image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight);
    public void drawScaledImageRel(Image image, double x, double y, double width, double height, int srcX, int srcY, int srcWidth, int srcHeight);

    public int  getWidth();
    public int  getHeight();

    public void drawARGB(int i, int j, int k, int l);
}
