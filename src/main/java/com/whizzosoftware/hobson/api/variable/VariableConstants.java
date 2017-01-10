/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

/**
 * A class the defines static constants for common device variable names.
 *
 * @author Dan Noguerol
 */
public class VariableConstants {
    /**
     * Indicates apparent power in volt-amperes.
     */
    public static final String APPARENT_POWER_VA = "va";
    /**
     * Indicates whether the device is armed (e.g. security panels).
     */
    public static final String ARMED = "armed";
    /**
     * Indicates barometric pressure in inches of mercury.
     */
    public static final String BAROMETRIC_PRESSURE_INHG = "baromInHg";
    /**
     * Indicates current battery level (0-100 representing a percent).
     */
    public static final String BATTERY = "battery";
    /**
     * Indicates carbon dioxide in parts per million.
     */
    public static final String CO2_PPM = "co2ppm";
    /**
     * Indicates a color (e.g. a color LED bulb).
     */
    public static final String COLOR = "color";
    /**
     * Indicates dew point in degrees Celsius.
     */
    public static final String DEW_PT_C = "dewPtC";
    /**
     * Indicates dew point in degrees Fahrenheit.
     */
    public static final String DEW_PT_F = "dewPtF";
    /**
     * Indicates differential pressure in pounds per square inch.
     */
    public static final String DIFFERENTIAL_PRESSURE_PSI = "dpPsi";
    /**
     * Indicates energy consumption in watts.
     */
    public static final String ENERGY_CONSUMPTION_WATTS = "ecw";
    /**
     * Indicates energy consumption in watts.
     */
    public static final String ENERGY_CONSUMPTION_KWH = "eckwh";
    /**
     * Indicates the firmware version of a device.
     */
    public static final String FIRMWARE_VERSION = "firmwareVersion";
    /**
     * Indicates an indoor relative humidity.
     */
    public static final String INDOOR_RELATIVE_HUMIDITY = "inRh";
    /**
     * Indicates an indoor temperature in degrees Celsius.
     */
    public static final String INDOOR_TEMP_C = "inTempC";
    /**
     * Indicates an indoor temperature in degrees Fahrenheit.
     */
    public static final String INDOOR_TEMP_F = "inTempF";
    /**
     * Indicates a URL for an image.
     */
    public static final String IMAGE_STATUS_URL = "imageStatusUrl";
    /**
     * Indicates the current device level (e.g. a dimmer switch).
     */
    public static final String LEVEL = "level";
    /**
     * Indicates a light intensity in lux.
     */
    public static final String LX_LUX = "lxLux";
    /**
     * Indicates an outdoor relative humidity.
     */
    public static final String OUTDOOR_RELATIVE_HUMIDITY = "outRh";
    /**
     * Indicates an outdoor temperature in degrees Celsius.
     */
    public static final String OUTDOOR_TEMP_C = "outTempC";
    /**
     * Indicates an outdoor temperature in degrees Fahrenheit.
     */
    public static final String OUTDOOR_TEMP_F = "outTempF";
    /**
     * Indicates whether a device is on/off.
     */
    public static final String ON = "on";
    /**
     * Indicates power factor (a dimensionless unit from -1..1).
     */
    public static final String POWER_FACTOR = "pf";
    /**
     * Indicates reactive power in volt-amperes reactive.
     */
    public static final String REACTIVE_POWER_VAR = "var";
    /**
     * Indicates relative humidity.
     */
    public static final String RELATIVE_HUMIDITY = "rh";
    /**
     * Indicates a target temperature in degrees Celsius (e.g. a thermostat).
     */
    public static final String TARGET_TEMP_C = "targetTempC";
    /**
     * Indicates a target temperature in degrees Fahrenheit (e.g. a thermostat).
     */
    public static final String TARGET_TEMP_F = "targetTempF";
    /**
     * Indicates a target heating temperature in degrees Celsius.
     */
    public static final String TARGET_HEAT_TEMP_C = "targetHeatTempC";
    /**
     * Indicates a target heating temperature in degrees Fahrenheit.
     */
    public static final String TARGET_HEAT_TEMP_F = "targetHeatTempF";
    /**
     * Indicates a target cooling temperature in degrees Celsius.
     */
    public static final String TARGET_COOL_TEMP_C = "targetCoolTempC";
    /**
     * Indicates a target cooling temperature in degrees Fahrenheit.
     */
    public static final String TARGET_COOL_TEMP_F = "targetCoolTempF";
    /**
     * Indicates a temperature in degrees Fahrenheit.
     */
    public static final String TEMP_F = "tempF";
    /**
     * Indicates a temperature in degrees Celsius.
     */
    public static final String TEMP_C = "tempC";
    /**
     * Indicates the time a device is reporting.
     */
    public static final String TIME = "time";
    /**
     * Indicates the current thermostat fan mode (e.g. on, off or auto).
     */
    public static final String TSTAT_FAN_MODE = "tstatFanMode";
    /**
     * Indicates the current thermostat mode (e.g. heat or cool).
     */
    public static final String TSTAT_MODE = "tstatMode";
    /**
     * Indicates the URL of a video stream (e.g. a camera).
     */
    public static final String VIDEO_STATUS_URL = "videoStatusUrl";
    /**
     * Indicates the wind direction in degrees (0-360).
     */
    public static final String WIND_DIRECTION_DEGREES = "windDirDeg";
    /**
     * Indicates the wind speed in miles per hour.
     */
    public static final String WIND_GUST_MPH = "windGustMph";
    /**
     * Indicates the wind speed in kilometers per hour.
     */
    public static final String WIND_GUST_KMH = "windGustKmh";
    /**
     * Indicates the wind speed in miles per hour.
     */
    public static final String WIND_SPEED_MPH = "windSpdMph";
    /**
     * Indicates the wind speed in kilometers per hour.
     */
    public static final String WIND_SPEED_KMH = "windSpdKmh";
}
