package com.example.ashri.ocrgraphdesigner;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView text;
    ImageView image;
    Button btn;
    Button upload;
    Button cropbtn;
    Bitmap selectedBitmap;
    private static final int pickimage = 100;
    Uri imageURI;
    byte[] imageData;


    private final int RESULT_CROP = 400;
    String extractedtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView)findViewById(R.id.textView);
        image = (ImageView)findViewById(R.id.imageview);
        btn = (Button)findViewById(R.id.readbutton);
        upload = (Button)findViewById(R.id.uploadbutton);
        cropbtn = (Button)findViewById(R.id.cropButton);


        cropbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put validation
                try {
                    performCrop(imageURI);
                }
                catch (Exception e){

                }

            }
        });
    }


    public void uploadImage(View v){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, pickimage);
    }


    private void performCrop(Uri picUri) {
        try {


            Intent cropIntent = new Intent("com.android.camera.action.CROP");



            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect ratio of desired crop
            cropIntent.putExtra("aspectX", 1); //1:1 is a rectangle
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y (DPI Scalling)
            cropIntent.putExtra("outputX", 300); //pixels in x and y
            cropIntent.putExtra("outputY", 300);


            cropIntent.putExtra("return-data", true);

            startActivityForResult(cropIntent, RESULT_CROP);


        }

        catch (ActivityNotFoundException anfe) {

            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();


        }
    }




    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == pickimage) {
            imageURI = data.getData();

            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                image.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


                }

        if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
               selectedBitmap = extras.getParcelable("data");

                image.setImageBitmap(selectedBitmap);

            }
        }
            }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        // Resizing the bitmap using the matrix
        matrix.postScale(scaleWidth, scaleHeight);

        // Re-creating the new bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, true);

        return resizedBitmap;
    }






    public void readText(View v) throws IOException {

try {

    Bitmap newBitmap = getResizedBitmap(selectedBitmap, 650, 650);

    Bitmap mutableBitmap = newBitmap.copy(Bitmap.Config.ARGB_8888, true);

    Bitmap newbit = changeBitmapContrastBrightness(mutableBitmap, 3, -400);


    ByteArrayOutputStream streams = new ByteArrayOutputStream();
    newbit.compress(Bitmap.CompressFormat.JPEG, 100, streams);
    imageData = streams.toByteArray();

 //   setDpi(imageData, 300);

    Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);


    TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

    if (!textRecognizer.isOperational()) {
        Toast.makeText(getApplicationContext(), "Please upload a clear image", Toast.LENGTH_SHORT).show();

    } else {
        Frame frame = new Frame.Builder().setBitmap(bmp).build();

        SparseArray<TextBlock> items = textRecognizer.detect(frame);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < items.size(); ++i) {
            TextBlock myItem = items.valueAt(i);
            sb.append(myItem.getValue());
            sb.append("\n");

        }

        extractedtext = sb.toString();

        Intent mIntent = new Intent(this, TextReader.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Bundle mBundle = new Bundle();
        mBundle.putString("text", extractedtext);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        mIntent.putExtras(mBundle);
        mIntent.putExtra("image", byteArray);
        startActivity(mIntent);

    }
}
catch (Exception e){

    Toast.makeText(this, "Please upload an image.",
            Toast.LENGTH_LONG).show();
}



    }


    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;

    }








}
