package com.example.ashri.ocrgraphdesigner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PieChartActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    Button btn;



    private static int REQUEST_CODE = 555;


    ArrayList<String> Values = new ArrayList<>();
    ArrayList<String> Numbers = new ArrayList<>();
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Processed = new ArrayList<>();



    PieChart pieChart;

    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_selector);
        Log.d(TAG, "onCreate: starting to create chart");

        String value = getIntent().getExtras().getString("text");
        btn = (Button)findViewById(R.id.saveButton);



btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {



        Bitmap mBitmap = pieChart.getChartBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                mBitmap, "Design", null);

        Toast.makeText(PieChartActivity.this, "Graph Saved Successfully!", Toast.LENGTH_LONG).show();

        Uri uri = Uri.parse(path);


        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, "Pie Chart");
        startActivity(Intent.createChooser(share, "Share Your Design!"));



    }
});


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


        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Pie Chart");
        pieChart.setCenterTextSize(10);


        addDataSet();
    }

    private void addDataSet() {

        try{

        ArrayList<PieEntry> yEntrys = new ArrayList<>();


        for(int i = 0; i < Processed.size(); i++){
            yEntrys.add(new PieEntry(Float.parseFloat(Values.get(i)), Processed.get(i)));
            System.out.print(Processed.get(i));
        }


        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Generated Graph");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
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
