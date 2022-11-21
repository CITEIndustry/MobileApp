package com.evermine.industryapp;

import java.io.Serializable;

public class Dropdown implements Serializable {
    private int id;
    private int defaultVal;
    private String[][] option;
    private String label;

    public Dropdown(int id, int defaultVal, int options, String label) {
        this.id = id;
        this.defaultVal = defaultVal;
        this.option = new String[options][2];
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(int defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String[][] getOption() {
        return option;
    }

    public void setOption(int opt,int pos,String value) {
        this.option[opt][pos]=value;
    }

    public void setOption(String[][] option) {
        this.option = option;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
