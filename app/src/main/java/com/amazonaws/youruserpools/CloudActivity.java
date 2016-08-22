package com.amazonaws.youruserpools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;

public class CloudActivity extends AppCompatActivity {
    private Button btnDownload;
    private Button btnUpload;
    private Button cloud;
    private Button shareBucket;
    private EditText userNametoShare;
    private Button sendtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        Intent intent = getIntent();

        initUI();
    }
    private void initUI() {
        //btnDownload = (Button) findViewById(R.id.buttonDownloadMain);
        btnUpload = (Button) findViewById(R.id.buttonUploadMain);
        cloud = (Button) findViewById(R.id.cloud);
        shareBucket = (Button) findViewById(R.id.shareUserNameButton);
        userNametoShare = (EditText) findViewById(R.id.shareBucket);
        sendtext = (Button) findViewById(R.id.shareUserNameText);
        /*btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(CloudActivity.this, DownloadActivity.class);
                startActivity(intent);
            }
        });*/

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(CloudActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(CloudActivity.this, CloudImageSelection.class);
                startActivity(intent);
            }
        });

        sendtext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View argo) {
                sendSMSMessage();
            }
        });




        shareBucket.setOnClickListener(new View.OnClickListener() {

            public void onClick(View argo) {
                Intent intent = new Intent(CloudActivity.this, DownloadActivity.class);
                intent.putExtra("UserNameToShare", userNametoShare.getText().toString());
                startActivity(intent);


            }
        });

    }
    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        //smsIntent.putExtra("address", new String("01234"));
        smsIntent.putExtra("sms_body", "Hello, Use my ID to Share Photos. ID: "+AppHelper.getCurrUser());

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CloudActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
