/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.color;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * A class that represents a color. The color can either be comprised of hue, saturation and brightness
 * (for non-white colors) or color temperature and brightness (for shades of white).
 *
 * @author Dan Noguerol
 */
public class Color {
    private Integer hue;
    private Integer saturation;
    private Integer brightness;
    private Integer kelvin;

    /**
     * Constructor.
     *
     * @param hue the color's hue value (0-360 degrees)
     * @param saturation the color's saturation value (0-100 percent)
     * @param brightness the color's brightness value (0-100 percent)
     */
    public Color(Integer hue, Integer saturation, Integer brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public Color(Integer kelvin, Integer brightness) {
        this.kelvin = kelvin;
        this.brightness = brightness;
    }

    /**
     * Constructor.
     *
     * @param s the result of a previous Color.toString()
     */
    public Color(String s) {
        if (s != null && s.startsWith("hsb(") && s.endsWith(")")) {
            StringTokenizer tok = new StringTokenizer(s.substring(4, s.length() - 1), ",");
            try {
                hue = Integer.parseInt(tok.nextToken());
                saturation = Integer.parseInt(tok.nextToken());
                brightness = Integer.parseInt(tok.nextToken());
            } catch (NoSuchElementException e) {
                throw new IllegalArgumentException("Invalid HSB value");
            }
        } else if (s != null && s.startsWith("kb(") && s.endsWith(")")) {
            StringTokenizer tok = new StringTokenizer(s.substring(3, s.length() - 1), ",");
            try {
                kelvin = Integer.parseInt(tok.nextToken());
                brightness = Integer.parseInt(tok.nextToken());
            } catch (NoSuchElementException e) {
                throw new IllegalArgumentException("Invalid KB value");
            }
        } else {
            throw new IllegalArgumentException("Invalid color string");
        }
    }

    /**
     * Returns the hue of the color.
     *
     * @return An integer between 0-360 (degrees)
     */
    public int getHue() {
        return hue;
    }

    /**
     * Returns the saturation of the color.
     *
     * @return An integer between 0-100 (percent)
     */
    public int getSaturation() {
        return saturation;
    }

    /**
     * Returns the brightness of the color.
     *
     * @return An integer between 0-100 (percent)
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Returns the color temperature of the color.
     *
     * @return An integer between 2500-9000 (kelvin)
     */
    public Integer getColorTemperature() {
        return kelvin;
    }

    public boolean isColor() {
        return (hue != null && saturation != null && brightness != null);
    }

    public boolean isColorTemperature() {
        return (kelvin != null && brightness != null);
    }

    public String toString() {
        if (isColorTemperature()) {
            return "kb(" + getColorTemperature() + "," + getBrightness() + ")";
        } else {
            return "hsb(" + getHue() + "," + getSaturation() + "," + getBrightness() + ")";
        }
    }
}
