package com.awesoft.cclink.hudoverlay;

public class RightboundTextElement {
    private final String text;
    private final int x;
    private final int y;
    private final int color;
    private final float scale;

    public RightboundTextElement(String text, int x, int y, int color, float scale) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.scale = scale;
    }

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColor() {
        return color;
    }

    public float getScale() {
        return scale;
    }
}
