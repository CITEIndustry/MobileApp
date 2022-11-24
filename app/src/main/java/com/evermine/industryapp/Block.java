package com.evermine.industryapp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Block implements Serializable {
    private String name;
    private Map<Integer, SwitchElm> switchList;
    private Map<Integer, Slider> sliderList;
    private Map<Integer, Dropdown> dropdownList;
    private Map<Integer, Sensor> sensorList;
    public Block(String name){
        this.name=name;
        switchList = new HashMap<Integer, SwitchElm>();
        sliderList = new HashMap<Integer,Slider>();
        dropdownList = new HashMap<Integer,Dropdown>();
        sensorList = new HashMap<Integer,Sensor>();
    }

    public void addSwitch(String[] values){
        switchList.put(Integer.parseInt(values[2]),new SwitchElm(Integer.parseInt(values[2]),values[3],values[4]));
    }
    public void addSlider(String[] values){
        sliderList.put(Integer.parseInt(values[2]),new Slider(Integer.parseInt(values[2]),Float.parseFloat(values[3]),Integer.parseInt(values[4]),Integer.parseInt(values[5]),Float.parseFloat(values[6]),values[7]));
    }
    public void addDropdown(String[] values){
        for (String str:values){
            System.out.println(str);
        }
        String[] options = values[5].split("/");
        Dropdown dp = new Dropdown(Integer.parseInt(values[2]),Integer.parseInt(values[3]),options.length,values[4]);
        for(int i = 0 ;i<options.length;i++){
            String value[] = options[i].split(":");
            dp.setOption(i,0,value[0]);
            dp.setOption(i,1,value[1]);
        }
        dropdownList.put(dp.getId(), dp);
    }
    public void addSensor(String[] values){
        sensorList.put(Integer.parseInt(values[2]),new Sensor(Integer.parseInt(values[2]),values[3],Integer.parseInt(values[4]),Integer.parseInt(values[5]),Integer.parseInt(values[6]),values[7]));

    }

    public String getName() {
        return name;
    }

    public Map<Integer, SwitchElm> getSwitchList() {
        return switchList;
    }

    public Map<Integer, Slider> getSliderList() {
        return sliderList;
    }

    public Map<Integer, Dropdown> getDropdownList() {
        return dropdownList;
    }

    public Map<Integer, Sensor> getSensorList() {
        return sensorList;
    }
}
