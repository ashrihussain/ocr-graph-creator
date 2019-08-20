package com.example.ashri.ocrgraphdesigner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.Random;

public class LineChartActivity extends AppCompatActivity{

    private LineChart lchart;
    Button savebtn;


    private static int REQUEST_CODE = 555;

    ArrayList<String> Values = new ArrayList<>();
    ArrayList<String> Numbers = new ArrayList<>();
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Processed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);


        savebtn = (Button)findViewById(R.id.saveButtonline);
        lchart = (LineChart) findViewById(R.id.lineCharts);
        lchart.setDragEnabled(true);
        lchart.setScaleEnabled(true);

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

                Bitmap mBitmap = lchart.getChartBitmap();

                String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                        mBitmap, "Design", null);

                Toast.makeText(LineChartActivity.this, "Graph Saved Successfully!", Toast.LENGTH_LONG).show();

                Uri uri = Uri.parse(path);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, "Line Chart");
                startActivity(Intent.createChooser(share, "Share Your Design!"));
            }
        });

    }

    public void addData(){

        try {
            ArrayList<Entry> yEntrys = new ArrayList<>();

            for (int i = 0; i < Processed.size(); i++) {
                yEntrys.add(new Entry(i, Float.parseFloat(Values.get(i))));
                System.out.print(Processed.get(i));
            }


            LineDataSet set1 = new LineDataSet(yEntrys, "Data");

            set1.setFillAlpha(110);

            ArrayList<ILineDataSet> datasets = new ArrayList<>();
            datasets.add(set1);

            LineData data = new LineData(datasets);

            lchart.setData(data);
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
