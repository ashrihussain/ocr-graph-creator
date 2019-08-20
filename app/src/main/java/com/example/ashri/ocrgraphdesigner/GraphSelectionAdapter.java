package com.example.ashri.ocrgraphdesigner;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GraphSelectionAdapter extends ArrayAdapter<String> {
    public GraphSelectionAdapter(@NonNull Context context,  @NonNull String[] objects) {
        super(context, R.layout.custom_listview , objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater Inf1 = LayoutInflater.from(getContext());
        View customeView = Inf1.inflate(R.layout.custom_listview,parent, false);

        String ClotheItem = getItem(position);
        TextView MainText1 =(TextView) customeView.findViewById(R.id.DisplayText);
        ImageView MainImg1 = (ImageView) customeView.findViewById(R.id.DisplayImage);

        MainText1.setText(ClotheItem);
        if( ClotheItem.equals("BarChart")) {
            MainImg1.setImageResource(R.drawable.barchartimg);
        }
        else if( ClotheItem.equals("PieChart")) {
            MainImg1.setImageResource(R.drawable.piechartimg);
                 }

        else if( ClotheItem.equals("LineChart")) {
            MainImg1.setImageResource(R.drawable.linecharts);
        }

        return customeView;
    }
}
