package com.evermine.industryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Manage extends AppCompatActivity {

    private ArrayList<SwitchElm> switchList;
    private ArrayList<Slider> sliderList;
    private ArrayList<Dropdown> dropdownList;
    private ArrayList<Sensor> sensorList;
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
        switchList = (ArrayList<SwitchElm>) getIntent().getSerializableExtra("switch");
        sliderList = (ArrayList<Slider>) getIntent().getSerializableExtra("slider");
        dropdownList = (ArrayList<Dropdown>) getIntent().getSerializableExtra("dropdown");
        sensorList = (ArrayList<Sensor>) getIntent().getSerializableExtra("sensor");
        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableLayout tabLayout=(TableLayout) findViewById(R.id.tableElements);
        for(SwitchElm sw: switchList){
            TableRow row=new TableRow(this);
            Switch swt = new Switch(this);
            if(sw.getDefaultVal().equals("on")){
                swt.setChecked(true);
            }
            row.addView(swt);
            row.setLayoutParams(params2);
            tabLayout.addView(row);
            System.out.println("me ejecuto");
        }

        for(Slider slid: sliderList){
            TableRow row=new TableRow(this);
            SeekBar sb = new SeekBar(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sb.setPadding(10,10,10,10);
                sb.setMin(slid.getMin());
                sb.setMax(slid.getMax());
                sb.setProgress((int) slid.getDefaultVal());
                sb.refreshDrawableState();
                System.out.println("ValorDefecto: "+slid.getDefaultVal()+"min:"+slid.getMin()+"max:"+slid.getMax());
            }
            row.addView(sb);
            row.setLayoutParams(params2);
            tabLayout.addView(row);
        }

        for(Dropdown sw: dropdownList){
            TableRow row=new TableRow(this);
            Spinner spinner = new Spinner(this);

            String val[] = new String[sw.getOption().length];
            for(int i = 0; i<sw.getOption().length;i++){
                val[i]=sw.getOption()[i][1];
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, val);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setSelection(sw.getDefaultVal());
            row.addView(spinner);
            tabLayout.addView(row);
        }

        for(Sensor s : sensorList){
            TableRow row=new TableRow(this);
            TextView tv = new TextView(this);
            int val = 7;
            tv.setText(String.valueOf(val)+s.getUnits());
            tv.setTypeface(null, Typeface.BOLD);
            tv.setBackgroundColor(Color.parseColor("#ecf0f1"));
            if(val<s.getThresholdlow()){
                tv.setTextColor(Color.parseColor("#e74c3c"));
            }
            else if(val>s.getThresholdhight()){
                tv.setTextColor(Color.parseColor("#3498db"));
            }
            else {
                tv.setTextColor(Color.parseColor("#2ecc71"));
            }
            row.addView(tv);
            tabLayout.addView(row);
        }






    }
    public static Manage getInstance(){
        return manage;
    }

}