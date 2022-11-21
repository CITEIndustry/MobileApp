package com.evermine.industryapp;

import java.io.Serializable;

public class SwitchElm implements Serializable {
    private int id;
    private String defaultVal;
    private String label;

    public SwitchElm(int id, String defaultVal, String label) {
        this.id = id;
        this.defaultVal = defaultVal;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

}
