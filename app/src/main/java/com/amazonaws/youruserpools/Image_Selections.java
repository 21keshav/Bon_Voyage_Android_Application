package com.amazonaws.youruserpools;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;

import java.io.File;
import java.util.ArrayList;

public class Image_Selections extends AppCompatActivity {

    private LazyAdapter imageAdapter;
    ArrayList<String> imageList1 = new ArrayList<String>();
    private String[] FilePathStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image__selections);
        // Check for SD Card
        GridView gridview = (GridView) findViewById(R.id.gridview);
        imageAdapter = new LazyAdapter(this);
        gridview.setAdapter(imageAdapter);
        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        //String targetPath = ExternalStorageDirectoryPath + "/BonVoyageCloud";
        String targetPath = ExternalStorageDirectoryPath + "/BonVoyageImages";
        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);
        File[] files = targetDirector.listFiles();
        FilePathStrings = new String[files.length];
        for (File file : files){
            imageAdapter.add(file.getAbsolutePath());

        }
        for (int i = 0; i < files.length; i++) {
            // Get the path of the image file
            FilePathStrings[i] = files[i].getAbsolutePath();
            // Get the name image file

        }
        // Capture gridview item click
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(Image_Selections.this, ImageActivity.class);
                // Pass String arrays FilePathStrings
                i.putExtra("filepath", FilePathStrings);
                // Pass String arrays FileNameStrings
                // Pass click position
                i.putExtra("position", position);
                startActivity(i);
            }

        });


    }

}
