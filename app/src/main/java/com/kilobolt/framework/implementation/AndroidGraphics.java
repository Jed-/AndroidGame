package com.kilobolt.framework.implementation;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Image;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap       frameBuffer;
    Canvas       canvas;
    Paint        paint;
    Rect         srcRect = new Rect();
    Rect         dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets      = assets;
        this.frameBuffer = frameBuffer;
        this.canvas      = new Canvas(frameBuffer);
        this.paint       = new Paint();
    }

    @Override
    public Image newImage(String fileName, ImageFormat format) {
        Config config = null;

        if(format==ImageFormat.RGB565) {
            config = Config.RGB_565;
        } else if(format==ImageFormat.ARGB4444) {
            config = Config.ARGB_4444;
        } else {
            config = Config.ARGB_8888;
        }

        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if(bitmap==null) {
                throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
            }
        } catch(IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch(IOException e) {
                    // pass
                }
            }
        }

        if(bitmap.getConfig() == Config.RGB_565) {
            format = ImageFormat.RGB565;
        } else if(bitmap.getConfig() == Config.ARGB_4444) {
            format = ImageFormat.ARGB4444;
        } else {
            format = ImageFormat.ARGB8888;
        }

        return new AndroidImage(bitmap, format);
    }

    @Override
    public void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawLineRel(double x, double y, double x2, double y2, int color) {
        drawLine((int)(getWidth() * x), (int)(getHeight() * y), (int)(getWidth() * x2), (int)(getHeight() * y2), color);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawRectRel(double x, double y, double width, double height, int color) {
        drawRect((int)(getWidth() * x), (int)(getHeight() * y), (int)(getWidth() * width), (int)(getHeight() * height), color);
    }

    @Override
    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    @Override
    public void drawString(String text, int x, int y, Paint paint) {
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void drawStringRel(String text, double x, double y, Paint paint) {
        drawString(text, (int)(getWidth() * x), (int)(getHeight() * y), paint);
    }

    @Override
    public void drawImage(Image Image, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left   = srcX;
        srcRect.top    = srcY;
        srcRect.right  = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left   = x;
        dstRect.top    = y;
        dstRect.right  = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawImageRel(Image image, double x, double y, int srcX, int srcY, int srcWidth, int srcHeight) {
        drawImage(image, (int)(getWidth() * x), (int)(getHeight() * y), srcX, srcY, srcWidth, srcHeight);
    }

    @Override
    public void drawImage(Image image, int x, int y) {
        canvas.drawBitmap(((AndroidImage) image).bitmap, x, y, null);
    }

    @Override
    public void drawImageRel(Image image, double x, double y) {
        drawImage(image, (int)(getWidth() * x), (int)(getHeight() * y));
    }

    @Override
    public void drawScaledImage(Image image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left   = srcX;
        srcRect.top    = srcY;
        srcRect.right  = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left   = x;
        dstRect.top    = y;
        dstRect.right  = x + width;
        dstRect.bottom = y + height;

        canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawScaledImageRel(Image image, double x, double y, double width, double height, int srcX, int srcY, int srcWidth, int srcHeight) {
        drawScaledImage(image, (int)(getWidth() * x), (int)(getHeight() * y), (int)(getWidth() * width), (int)(getHeight() * height), srcX, srcY, srcWidth, srcHeight);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}
