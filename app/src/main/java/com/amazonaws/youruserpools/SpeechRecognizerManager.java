/* ====================================================================
 * Copyright (c) 2014 Alpha Cephei Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY ALPHA CEPHEI INC. ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL CARNEGIE MELLON UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 */

package com.amazonaws.youruserpools;

import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class SpeechRecognizerManager  {

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "ok camera";
    private edu.cmu.pocketsphinx.SpeechRecognizer mPocketSphinxRecognizer;
    private static final String TAG = SpeechRecognizerManager.class.getSimpleName();
    protected AudioManager mAudioManager;
    protected Intent mSpeechRecognizerIntent;
    protected android.speech.SpeechRecognizer mGoogleSpeechRecognizer;
   Cammera_Activity activity;
    private Camera.PictureCallback mPictureCallback1;
    private Camera mCamera;

    public SpeechRecognizerManager(Cammera_Activity activity ) {
        this.activity = activity;
     //   this.mOnResultListener = (OnResultListener)context;
        initPockerSphinx();

       // initGoogleSpeechRecognizer();

    }


    private void initPockerSphinx() {

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(SpeechRecognizerManager.this.activity);

                    //Performs the synchronization of assets in the application and external storage
                    File assetDir = assets.syncAssets();


                    //Creates a new speech recognizer builder with default configuration
                    SpeechRecognizerSetup speechRecognizerSetup = defaultSetup();

                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "cmudict-en-us.dict"));

                    // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                    //    speechRecognizerSetup.setRawLogDir(assetDir)

                    // Threshold to tune for keyphrase to balance between false alarms and misses
                    speechRecognizerSetup.setKeywordThreshold(1e-45f);

                    // Use mContext-independent phonetic search, mContext-dependent is too slow for mobile
                    speechRecognizerSetup.setBoolean("-allphone_ci", true);

                    //Creates a new SpeechRecognizer object based on previous set up.
                    mPocketSphinxRecognizer = speechRecognizerSetup.getRecognizer();
                    mPocketSphinxRecognizer.addListener(new PocketSphinxRecognitionListener());

                    // Create keyword-activation search.
                    mPocketSphinxRecognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                   // Toast.makeText( ,"Failed to init mPocketSphinxRecognizer ", Toast.LENGTH_SHORT).show();
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();

    }




    public void destroy() {
        if (mPocketSphinxRecognizer != null) {
            mPocketSphinxRecognizer.cancel();
            mPocketSphinxRecognizer.shutdown();
            mPocketSphinxRecognizer = null;
        }


        if (mGoogleSpeechRecognizer != null) {
            mGoogleSpeechRecognizer.cancel();
            ;
            mGoogleSpeechRecognizer.destroy();
            mPocketSphinxRecognizer = null;
        }

    }

    private void switchSearch(String searchName) {
        mPocketSphinxRecognizer.stop();

        if (searchName.equals(KWS_SEARCH))
            mPocketSphinxRecognizer.startListening(searchName);

    }


    protected class PocketSphinxRecognitionListener implements edu.cmu.pocketsphinx.RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
        }


        /**
         * In partial result we get quick updates about current hypothesis. In
         * keyword spotting mode we can react here, in other modes we need to wait
         * for final result in onResult.
         */
        @Override
        public void onPartialResult(Hypothesis hypothesis) {
            if (hypothesis == null)
                return;


            String text = hypothesis.getHypstr();
            if (text.equals(KEYPHRASE)) {
                //Toast.makeText(activity.getApplicationContext(),"You said:"+text,Toast.LENGTH_SHORT).show();

                activity.capture();
                Log.i(text, "done");

                switchSearch(KWS_SEARCH);
            }
        }

        @Override
        public void onResult(Hypothesis hypothesis) {
        }


        /**
         * We stop mPocketSphinxRecognizer here to get a final result
         */
        @Override
        public void onEndOfSpeech() {
//            switchSearch(KWS_SEARCH);
//            //since there was no recognized results, restart the recognizer and start listeting again
        }

        public void onError(Exception error) {
        }

        @Override
        public void onTimeout() {
        }

    }




    public void setOnResultListner(OnResultListener onResultListener){
        //mOnResultListener=onResultListener;
    }

    public interface OnResultListener
    {
        public void OnResult(ArrayList<String> commands);
    }
}
