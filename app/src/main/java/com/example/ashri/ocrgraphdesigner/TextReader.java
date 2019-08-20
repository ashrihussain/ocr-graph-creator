package com.example.ashri.ocrgraphdesigner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class TextReader extends AppCompatActivity {

    TextView text;
    ImageView image;
    Button btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reader);

        text = (EditText)findViewById(R.id.textView);
        image = (ImageView)findViewById(R.id.imageview);
        btn = (Button)findViewById(R.id.readbutton);

        String value = getIntent().getExtras().getString("text");

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        image.setImageBitmap(bmp);



        text.setText(value);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String confirmedText = text.getText().toString();
                Intent mIntent = new Intent(TextReader.this, GraphSelection.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("text", confirmedText);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);

            }
        });


    }




}
