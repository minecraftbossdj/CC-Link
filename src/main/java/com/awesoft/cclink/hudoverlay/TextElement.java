package com.awesoft.cclink.hudoverlay;

public class TextElement {
    private String text;
    private int x, y;
    private int color;
    private float scale; // Scale factor for the text

    public TextElement(String text, int x, int y, int color, float scale) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.scale = scale;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
