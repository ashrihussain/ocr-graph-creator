package com.example.ashri.ocrgraphdesigner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BarChartActivity extends AppCompatActivity {


    private BarChart bChart;
    Button savebtn;

    private static int REQUEST_CODE = 555;

    ArrayList<String> Values = new ArrayList<>();
    ArrayList<String> Numbers = new ArrayList<>();
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Processed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        savebtn = (Button)findViewById(R.id.saveButtonBar);

        bChart = (BarChart) findViewById(R.id.barChart);
        bChart.getDescription().setEnabled(false);
        String value = getIntent().getExtras().getString("text");


        String[] lines = value.split("\\r?\\n");

        for (String cont:lines

        ) {


            if(cont.matches("(0|[1-9]\\d*)")){

                Numbers.add(cont);

            }else if(cont.matches("[a-zA-Z]+")){

                Names.add(cont);

            }
            else{
                Processed.add(cont);
            }
        }

        if(Numbers.isEmpty()){

        }
        else {
            for (int x = 0; x < Names.size(); x++) {

                Processed.add(Names.get(x) + Numbers.get(x) + "\n");

            }
        }



        for (String var:Processed
        ) {
            String numberOnly= var.replaceAll("[^?!\\.0-9]", "");
            Values.add(numberOnly);

        }

        addData();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap mBitmap = bChart.getChartBitmap();

                String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                        mBitmap, "Design", null);

                Uri uri = Uri.parse(path);

                Toast.makeText(BarChartActivity.this, "Graph Saved Successfully!", Toast.LENGTH_LONG).show();

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, "Bar Chart");
                startActivity(Intent.createChooser(share, "Share Your Design!"));
            }
        });




    }





    public void addData(){

        try {
            ArrayList<BarEntry> yEntrys = new ArrayList<>();
            ArrayList<String> xEntrys = new ArrayList<>();


            for (int i = 0; i < Processed.size(); i++) {
                yEntrys.add(new BarEntry(i, Float.parseFloat(Values.get(i))));

            }

            for (int x = 0; x < Processed.size(); x++) {
                xEntrys.add(String.valueOf(Processed.get(x)));

            }

            BarDataSet barDataSet = new BarDataSet(yEntrys, "Data set");

            BarData data = new BarData(barDataSet);
            bChart.setData(data);
        }
        catch (Exception e){

            Toast.makeText(this, "Error creating Graph. Please Re-check your input data",
                    Toast.LENGTH_LONG).show();
        }


    }


    public String createRandomCode() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder(20);
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }



}
