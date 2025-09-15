package com.awesoft.cclink.libs;

public class ColorUtil {

    /**
     * Converts RGB color and transparency percentage to ARGB color.
     *
     * @param rgbColor      RGB color value (e.g., 0xFFFFFF for white)
     * @param transparency  Transparency percentage (0-100)
     * @return ARGB color value
     */
    public static int toArgb(int rgbColor, int transparency) {
        transparency = Math.max(0, Math.min(100, transparency));

        int alpha = (int) ((transparency / 100.0) * 255);

        return (alpha << 24) | (rgbColor & 0xFFFFFF);
    }

    /**
     * Converts RGB color to ARGB color with full opacity.
     *
     * @param rgbColor RGB color value
     * @return ARGB color value
     */
}