package com.evermine.industryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class Manage extends AppCompatActivity {

    private ArrayList<SwitchElm> switchList;
    private ArrayList<Slider> sliderList;
    private ArrayList<Dropdown> dropdownList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        switchList = (ArrayList<SwitchElm>) getIntent().getSerializableExtra("switch");
        sliderList = (ArrayList<Slider>) getIntent().getSerializableExtra("slider");
        dropdownList = (ArrayList<Dropdown>) getIntent().getSerializableExtra("dropdown");
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
                sb.setMin(slid.getMin());
                sb.setMax(slid.getMax());
                sb.setProgress((int) slid.getDefaultVal());
            }
            row.addView(sb);
            row.setLayoutParams(params2);
            tabLayout.addView(row);
        }

        for(Dropdown sw: dropdownList){
            TableRow row=new TableRow(this);
            Switch swt = new Switch(this);
            System.out.println("Dropdown");
            tabLayout.addView(row);
        }





    }
}