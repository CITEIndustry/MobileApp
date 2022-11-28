package com.evermine.industryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
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

    private Map<String, Block> blocks = new HashMap<>();
    //Elements

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
                MainActivity.client.close();
                Intent intent = new Intent(Manage.this, MainActivity.class);
                startActivity(intent);
            }
        });
        blocks = (HashMap<String, Block>) getIntent().getSerializableExtra("blocks");

        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        ScrollView sv = (ScrollView) findViewById(R.id.scrollv);
        TableLayout tabLayout=new TableLayout(this);
        sv.addView(tabLayout);
        //tabLayout.setBackgroundColor(R.drawable.roundedtable);
        tabLayout.setBackgroundColor(Color.parseColor("#ecf0f1"));
        //For-each for every block
        for (String bl : blocks.keySet()){
            TableRow titleRow = new TableRow(this);
            titleRow.setBackgroundColor(Color.parseColor("#bdc3c7"));
            TextView blockName = new TextView(this);
            //blockName.setTextColor(Color.parseColor("#bdc3c7"));
            blockName.setText(blocks.get(bl).getName());
            titleRow.addView(blockName);
            tabLayout.addView(titleRow);
            //Switch
            Map<Integer, SwitchElm> sortedSwitch = new TreeMap<>(blocks.get(bl).getSwitchList());
            TextView SwitchLabel = new TextView(this);
            SwitchLabel.setText("Lights");
            SwitchLabel.setTypeface(null, Typeface.BOLD);
            tabLayout.addView(SwitchLabel);
            for(Integer sw: sortedSwitch.keySet()){
                TableRow row=new TableRow(this);
                Switch swt = new Switch(this);
                if(blocks.get(bl).getSwitchList().get(sw).getDefaultVal().equals("on")){
                    swt.setChecked(true);
                }
                swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(swt.isChecked()==true){
                            System.out.println("envio datooooos");
                            MainActivity.getInstance().onchange("switch",bl,sw,1);
                        }else{
                            MainActivity.getInstance().onchange("switch",bl,sw,0);
                        }

                    }
                });
                swt.setText(blocks.get(bl).getSwitchList().get(sw).getLabel());
                //switchElemen.put(blocks.get(bl).getSwitchList().get(sw).getId(),swt);
                blocks.get(bl).getSwitchElemen().put(blocks.get(bl).getSwitchList().get(sw).getId(),swt);
                row.addView(swt);
                row.setLayoutParams(params2);
                tabLayout.addView(row);
            }
            //Sliders
            Map<Integer, Slider> sortedSlider = new TreeMap<>(blocks.get(bl).getSliderList());
            TextView SlidersLabel = new TextView(this);
            SlidersLabel.setText("Boilers power");
            SlidersLabel.setTypeface(null, Typeface.BOLD);
            tabLayout.addView(SlidersLabel);
            for(Integer slid: sortedSlider.keySet()){
                TableRow row=new TableRow(this);
                TextView textv = new TextView(this);
                textv.setText(blocks.get(bl).getSliderList().get(slid).getLabel());
                row.addView(textv);
                SeekBar sb = new SeekBar(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sb.setPadding(10,10,10,10);
                    sb.setMin(blocks.get(bl).getSliderList().get(slid).getMin());
                    sb.setMax(blocks.get(bl).getSliderList().get(slid).getMax());
                    sb.setProgress((int) blocks.get(bl).getSliderList().get(slid).getDefaultVal());
                    sb.refreshDrawableState();
                    //System.out.println("ValorDefecto: "+slid.getDefaultVal()+"min:"+slid.getMin()+"max:"+slid.getMax());
                }
                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        MainActivity.getInstance().onchange("slider",bl,slid,sb.getProgress());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                //sliderElemen.put(blocks.get(bl).getSliderList().get(slid).getId(),sb);
                blocks.get(bl).getSliderElemen().put(blocks.get(bl).getSliderList().get(slid).getId(),sb);
                row.addView(sb);
                row.setLayoutParams(params2);
                tabLayout.addView(row);
            }
            //Dropdown
            Map<Integer, Dropdown> sortedDropdown = new TreeMap<>(blocks.get(bl).getDropdownList());
            TextView DropdownLabel = new TextView(this);
            DropdownLabel.setText("Mode");
            DropdownLabel.setTypeface(null, Typeface.BOLD);
            tabLayout.addView(DropdownLabel);
            for(Integer sw: sortedDropdown.keySet()){
                TableRow row=new TableRow(this);
                TextView textv = new TextView(this);
                textv.setText(blocks.get(bl).getDropdownList().get(sw).getLabel());
                row.addView(textv);
                Spinner spinner = new Spinner(this);

                String val[] = new String[blocks.get(bl).getDropdownList().get(sw).getOption().length];
                for(int i = 0; i<blocks.get(bl).getDropdownList().get(sw).getOption().length;i++){
                    val[i]=blocks.get(bl).getDropdownList().get(sw).getOption()[i][1];
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, val);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setSelection(blocks.get(bl).getDropdownList().get(sw).getDefaultVal());
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        MainActivity.getInstance().onchange("dropdown",bl,sw,spinner.getSelectedItemPosition());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                //dropdownElemen.put(blocks.get(bl).getDropdownList().get(sw).getId(),spinner);
                blocks.get(bl).getDropdownElemen().put(blocks.get(bl).getDropdownList().get(sw).getId(),spinner);
                row.addView(spinner);
                tabLayout.addView(row);
            }
            //Sensors
            Map<Integer, Sensor> sortedSensor = new TreeMap<>(blocks.get(bl).getSensorList());
            TextView SensorsLabel = new TextView(this);
            SensorsLabel.setText("Boilers temperature");
            SensorsLabel.setTypeface(null, Typeface.BOLD);
            tabLayout.addView(SensorsLabel);
            for(Integer s : sortedSensor.keySet()){
                TableRow row=new TableRow(this);
                TextView textv = new TextView(this);
                textv.setText(blocks.get(bl).getSensorList().get(s).getLabel());
                row.addView(textv);
                TextView tv = new TextView(this);
                tv.setText(blocks.get(bl).getSensorList().get(s).getValue()+blocks.get(bl).getSensorList().get(s).getUnits());
                tv.setTypeface(null, Typeface.BOLD);
                //tv.setBackgroundColor(Color.parseColor("#ecf0f1"));

                if(blocks.get(bl).getSensorList().get(s).getValue()<blocks.get(bl).getSensorList().get(s).getThresholdlow()){
                    //tv.setTextColor(Color.parseColor("#e74c3c"));
                    tv.setBackgroundColor(Color.parseColor("#3498db"));
                }
                else if(blocks.get(bl).getSensorList().get(s).getValue()>blocks.get(bl).getSensorList().get(s).getThresholdhight()){
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


    }

    public void updateSwitch(String blockid,int id, String val){
        if(val.equals("on")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //switchElemen.get(id).setChecked(true);
                    blocks.get(blockid).getSwitchElemen().get(id).setChecked(true);
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //switchElemen.get(id).setChecked(false);
                    blocks.get(blockid).getSwitchElemen().get(id).setChecked(false);
                }
            });

        }
    }
    public void updateSlider(String blockid, int id, int value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //sliderElemen.get(id).setProgress(value);
                blocks.get(blockid).getSliderElemen().get(id).setProgress(value);
            }
        });

    }
    public void updateDropdown(String blockid, int id, int value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //dropdownElemen.get(id).setSelection(value);
                blocks.get(blockid).getDropdownElemen().get(id).setSelection(value);
            }
        });

    }

    public static Manage getInstance(){
        return manage;
    }

}