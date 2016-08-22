package com.amazonaws.youruserpools;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;

public class ImageActivity extends AppCompatActivity {
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent i = getIntent();

        // Get the position
        int position = i.getExtras().getInt("position");
        // Get String arrays FilePathStrings
        String[] filepath = i.getStringArrayExtra("filepath");
        // Get String arrays FileNameStrings
        // Locate the ImageView in view_image.xml
        imageview = (ImageView) findViewById(R.id.full_image_view);
        // Decode the filepath with BitmapFactory followed by the position
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
        // Set the decoded bitmap into ImageView
        imageview.setImageBitmap(bmp);
    }
}
