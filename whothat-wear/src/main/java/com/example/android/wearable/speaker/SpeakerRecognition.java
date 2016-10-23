package com.example.android.wearable.speaker;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.StrictMode;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lec on 10/22/16.
 */

public class SpeakerRecognition {

    private static final String TAG = "SpeakerRecognition";
    private Context mContext;
    private String mOutputFileName;

    public SpeakerRecognition(Context context, String voiceFileName) {
        mOutputFileName = voiceFileName;
        mContext = context;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //Log.d(TAG, " "+mOutputFileName);
    }

    public void idVoice() throws Exception {
        String url = "https://api.projectoxford.ai/spid/v1.0/identify?identificationProfileIds=598f3ffd-fc3b-461e-9d60-30ac69959ac3%2Cd39fa51d-1a48-4166-b788-91ead17683d2%2C4dd26ac5-1859-4c6b-88e4-b96bc848e45a%2C73e64453-3351-4b92-abc8-a4e62b336cf3&shortAudio=true";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("ocp-apim-subscription-key", "41c331eb960d4c5599f599b2dc00e4c1");
        con.setRequestProperty("Content-Type", "multipart/form-data");

        // Send post request
        con.setDoOutput(true);

        OutputStream conStream = new BufferedOutputStream(con.getOutputStream());
        InputStream fileStream = mContext.getResources().openRawResource(R.raw.lec_test);
//        FileInputStream fileStream = mContext.openFileInput(mOutputFileName);

        while(fileStream.available() > 0) {
            conStream.write((char) fileStream.read());
        }
        conStream.flush();
        conStream.close();

        // open up file for reading
        // read a byte from the audio file
        // send the byte to the connection stream
        // continue until the end
/*
        wr.writeBytes(mContext.getFileStreamPath(mOutputFileName));
        wr.flush();
        wr.close();
*/

        int responseCode = con.getResponseCode();
        String response = con.getResponseMessage();
        Log.d(TAG, "Response Code: " + responseCode);
        Log.d(TAG, "Response Msg: " + response);

//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();

        //print result
//        Log.d(TAG, "Response: " + response.toString());

        /*
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("")
                .post(null)
                .addHeader("", "")
                .addHeader("content-type", "application/octet-stream")
                .build();

        Response response = client.newCall(request).execute();
        */
    }

    public void getOpsStatus(String opsID)
    {
        /*
        ="167af42c-5f7d-42f7-8483-d0e93b7248a6"
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url+"/operations/"+opsID)
                .get()
                .addHeader("ocp-apim-subscription-key", "41c331eb960d4c5599f599b2dc00e4c1")
                .build();

        Response response = client.newCall(request).execute();
        */
    }
}
