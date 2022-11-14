package com.evermine.industryapp;

import java.io.Serializable;

public class Slider implements Serializable {
    private int id;
    private float defaultVal;
    private int min;
    private int max;
    private float step;

    public Slider(int id, float defaultVal, int min, int max, float step) {
        this.id = id;
        this.defaultVal = defaultVal;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(float defaultVal) {
        this.defaultVal = defaultVal;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public float getStep() {
        return step;
    }

    public void setStep(float step) {
        this.step = step;
    }
}
