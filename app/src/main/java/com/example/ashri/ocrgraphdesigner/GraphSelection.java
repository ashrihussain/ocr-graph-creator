package com.example.ashri.ocrgraphdesigner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GraphSelection extends AppCompatActivity {


    String Titles[] = {"BarChart","PieChart","LineChart"};
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_selection);


            ListView listView = (ListView) findViewById(R.id.list);
            value = getIntent().getExtras().getString("text");

            GraphSelectionAdapter graphSelectionAdapter = new GraphSelectionAdapter(this, Titles);
            listView.setAdapter(graphSelectionAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(position==0){
                        Intent mIntent = new Intent(GraphSelection.this, BarChartActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("text", value);
                        mIntent.putExtras(mBundle);
                        startActivity(mIntent);
                    }

                    if(position==1){
                        Intent mIntent = new Intent(GraphSelection.this, PieChartActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("text", value);
                        mIntent.putExtras(mBundle);
                        startActivity(mIntent);
                    }

                    if(position==2){
                        Intent mIntent = new Intent(GraphSelection.this, LineChartActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("text", value);
                        mIntent.putExtras(mBundle);
                        startActivity(mIntent);
                    }






                }
            });


    }


}
