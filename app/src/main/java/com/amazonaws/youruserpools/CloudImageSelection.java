package com.amazonaws.youruserpools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;

public class CloudImageSelection extends AppCompatActivity {

    private Button cloud;
    private EditText userNametoShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_image_selection);
        cloud = (Button) findViewById(R.id.shareUserNameButton1);
        userNametoShare = (EditText) findViewById(R.id.shareBucket1);
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(CloudImageSelection.this, CloudSelections.class);
                intent.putExtra("UserNameToShare", userNametoShare.getText().toString());
                startActivity(intent);
            }
        });
    }
}
