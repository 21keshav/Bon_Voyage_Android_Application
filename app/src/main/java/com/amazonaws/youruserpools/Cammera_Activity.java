package com.amazonaws.youruserpools;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cammera_Activity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private FrameLayout mPreviewFrameLayout;
    private Button mCaptureButton;
    private Camera.PictureCallback mPictureCallback;
    protected static final int RESULT_SPEECH = 1;
    private Button btnSpeak;
    private TextView Text;
    private SpeechRecognizerManager mSpeechRecognizerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cammera_);
        Intent intent = getIntent();
        mPreviewFrameLayout = (FrameLayout) findViewById(R.id.camera_preview);

        mSpeechRecognizerManager =new SpeechRecognizerManager(this);
        // Add a listener to the Capture button
        mCaptureButton = (Button) findViewById(R.id.button_capture);
        mCaptureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera

                        if (mCamera != null) {
                            mPictureCallback = new PictureCallBack();
                            mCamera.takePicture(null, null, mPictureCallback);

                        }

                    }
                }
        );








    }

    public void capture()
    {

        if (mCamera != null) {

            mPictureCallback = new PictureCallBack();
            mCamera.takePicture(null, null, mPictureCallback);

        }
        return;


    }

    public  void onClickvoice(View v){
        Intent intent = new Intent(this,Image_Selections.class);
        startActivity(intent);
    }






    @Override
    protected void onResume() {
        super.onResume();

        // Create an instance of Camera
        mCamera = getCameraInstance();
        mSpeechRecognizerManager =new SpeechRecognizerManager(this);
        if (mCamera !=null) {
            mCameraPreview = new CameraPreview(Cammera_Activity.this, mCamera);
            mPreviewFrameLayout.addView(mCameraPreview);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        stopPreviewAndFreeCamera();
    }


    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * When this function returns, mCamera will be null.
     */
    private void stopPreviewAndFreeCamera() {

        if (mCamera != null) {
            // Call stopPreview() to stop updating the mPreviewFrameLayout surface.
            mCamera.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();
            mCamera = null;
            mPreviewFrameLayout.removeAllViews();
            mCameraPreview =null;
        }


    }
    private class PictureCallBack implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.w(TAG, "onPictureTaken.");
            Toast.makeText(getApplicationContext(),"Picture taken", Toast.LENGTH_SHORT).show();
            File pictureFile = CameraUtil.getOutputMediaFile(CameraUtil.MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: "
                );
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }

            Camera.Parameters parameters = camera.getParameters();
            camera.setDisplayOrientation(90);

            camera.startPreview();
        }
    }
}
