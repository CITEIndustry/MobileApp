package com.evermine.industryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Manage extends AppCompatActivity {

    private Map<Integer, SwitchElm> switchList;
    private Map<Integer, Slider> sliderList;
    private Map<Integer, Dropdown> dropdownList;
    private Map<Integer, Sensor> sensorList;
    //Elements
    private Map<Integer, Switch> switchElemen;
    private Map<Integer, SeekBar> sliderElemen;
    private Map<Integer, Spinner> dropdownElemen;

    static Manage manage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        manage=this;
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        switchList = (HashMap<Integer, SwitchElm>) getIntent().getSerializableExtra("switch");
        sliderList = (HashMap<Integer, Slider>) getIntent().getSerializableExtra("slider");
        dropdownList = (HashMap<Integer, Dropdown>) getIntent().getSerializableExtra("dropdown");
        sensorList = (HashMap<Integer, Sensor>) getIntent().getSerializableExtra("sensor");
        switchElemen=new HashMap<Integer, Switch>();
        sliderElemen=new HashMap<Integer, SeekBar>();
        dropdownElemen=new HashMap<Integer, Spinner>();
        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableLayout tabLayout=(TableLayout) findViewById(R.id.tableElements);
        //Switch
        Map<Integer, SwitchElm> sortedSwitch = new TreeMap<>(switchList);
        TextView SwitchLabel = new TextView(this);
        SwitchLabel.setText("Lights");
        SwitchLabel.setTypeface(null, Typeface.BOLD);
        tabLayout.addView(SwitchLabel);
        for(Integer sw: switchList.keySet()){
            TableRow row=new TableRow(this);
            Switch swt = new Switch(this);
            if(switchList.get(sw).getDefaultVal().equals("on")){
                swt.setChecked(true);
            }
            swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(swt.isChecked()==true){
                        MainActivity.getInstance().onchange("switch",sw,1);
                    }else{
                        MainActivity.getInstance().onchange("switch",sw,0);
                    }

                }
            });
            swt.setText(switchList.get(sw).getLabel());
            switchElemen.put(switchList.get(sw).getId(),swt);
            row.addView(swt);
            row.setLayoutParams(params2);
            tabLayout.addView(row);
        }
        //Sliders
        Map<Integer, Slider> sortedSlider = new TreeMap<>(sliderList);
        TextView SlidersLabel = new TextView(this);
        SlidersLabel.setText("Boilers power");
        SlidersLabel.setTypeface(null, Typeface.BOLD);
        tabLayout.addView(SlidersLabel);
        for(Integer slid: sliderList.keySet()){
            TableRow row=new TableRow(this);
            TextView textv = new TextView(this);
            textv.setText(sliderList.get(slid).getLabel());
            row.addView(textv);
            SeekBar sb = new SeekBar(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sb.setPadding(10,10,10,10);
                sb.setMin(sliderList.get(slid).getMin());
                sb.setMax(sliderList.get(slid).getMax());
                sb.setProgress((int) sliderList.get(slid).getDefaultVal());
                sb.refreshDrawableState();
                //System.out.println("ValorDefecto: "+slid.getDefaultVal()+"min:"+slid.getMin()+"max:"+slid.getMax());
            }
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    MainActivity.getInstance().onchange("slider",slid,sb.getProgress());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            sliderElemen.put(sliderList.get(slid).getId(),sb);
            row.addView(sb);
            row.setLayoutParams(params2);
            tabLayout.addView(row);
        }
        //Dropdown
        Map<Integer, Dropdown> sortedDropdown = new TreeMap<>(dropdownList);
        TextView DropdownLabel = new TextView(this);
        DropdownLabel.setText("Mode");
        DropdownLabel.setTypeface(null, Typeface.BOLD);
        tabLayout.addView(DropdownLabel);
        for(Integer sw: sortedDropdown.keySet()){
            TableRow row=new TableRow(this);
            TextView textv = new TextView(this);
            textv.setText(dropdownList.get(sw).getLabel());
            row.addView(textv);
            Spinner spinner = new Spinner(this);

            String val[] = new String[dropdownList.get(sw).getOption().length];
            for(int i = 0; i<dropdownList.get(sw).getOption().length;i++){
                val[i]=dropdownList.get(sw).getOption()[i][1];
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, val);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setSelection(dropdownList.get(sw).getDefaultVal());
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    MainActivity.getInstance().onchange("dropdown",sw,spinner.getSelectedItemPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            dropdownElemen.put(dropdownList.get(sw).getId(),spinner);
            row.addView(spinner);
            tabLayout.addView(row);
        }
        //Sensors
        Map<Integer, Sensor> sortedSensor = new TreeMap<>(sensorList);
        TextView SensorsLabel = new TextView(this);
        SensorsLabel.setText("Boilers temperature");
        SensorsLabel.setTypeface(null, Typeface.BOLD);
        tabLayout.addView(SensorsLabel);
        for(Integer s : sortedSensor.keySet()){
            TableRow row=new TableRow(this);
            TextView textv = new TextView(this);
            textv.setText(sensorList.get(s).getLabel());
            row.addView(textv);
            TextView tv = new TextView(this);
            tv.setText(sensorList.get(s).getValue()+sensorList.get(s).getUnits());
            tv.setTypeface(null, Typeface.BOLD);
            //tv.setBackgroundColor(Color.parseColor("#ecf0f1"));

            if(sensorList.get(s).getValue()<sensorList.get(s).getThresholdlow()){
                //tv.setTextColor(Color.parseColor("#e74c3c"));
                tv.setBackgroundColor(Color.parseColor("#3498db"));
            }
            else if(sensorList.get(s).getValue()>sensorList.get(s).getThresholdhight()){
                //tv.setTextColor(Color.parseColor("#3498db"));
                tv.setBackgroundColor(Color.parseColor("#E74C3C"));
            }
            else {
                //tv.setTextColor(Color.parseColor("#2ecc71"));
                tv.setBackgroundColor(Color.parseColor("#2ecc71"));
            }
            row.addView(tv);
            tabLayout.addView(row);
        }






    }

    public void updateSwitch(int id, String val){
        if(val.equals("on")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switchElemen.get(id).setChecked(true);
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switchElemen.get(id).setChecked(false);
                }
            });

        }
    }
    public void updateSlider(int id, int value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sliderElemen.get(id).setProgress(value);
            }
        });

    }
    public void updateDropdown(int id, int value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dropdownElemen.get(id).setSelection(value);
            }
        });

    }

    public static Manage getInstance(){
        return manage;
    }

}