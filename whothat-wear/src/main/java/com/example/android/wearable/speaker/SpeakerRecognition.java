package com.example.android.wearable.speaker;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import android.os.StrictMode;

import com.google.android.gms.common.api.Api;
import com.microsoft.cognitive.speakerrecognition.SpeakerIdentificationRestClient;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Identification;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.OperationLocation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Profile;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Status;

import org.json.JSONObject;
import android.text.TextUtils;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lec on 10/22/16.
 */

public class SpeakerRecognition {

    private static final String TAG = "SpeakerRecognition";
    private static final int SAMPLE_RATE = 16000;
    private Context mContext;
    private String mOutputFileName;
    private String mOutputFileNameWav;
    private HashMap<UUID, String> profiles = new HashMap<>();
    private String ApiKey = "41c331eb960d4c5599f599b2dc00e4c1"; // Lec
//    private String ApiKey = "8ebd40618b7440bdb973b1e96c06c02d"; // Oren


    public SpeakerRecognition(Context context, String voiceFileName, String voiceFileNameWav) {
        mOutputFileName = voiceFileName;
        mOutputFileNameWav = voiceFileNameWav;
        mContext = context;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Store default profiles in HashMap
//        profiles.put("00000000-0000-0000-0000-000000000000", "Unknown");

        // Lec's API Key
        profiles.put(UUID.fromString("e54db49a-0182-42b5-8aa7-79087ece737d"), "Lec Maj");
        profiles.put(UUID.fromString("2ae624c3-b4bb-4ef5-98f8-7cb89c38d0a1"), "Aishwarya Pillai");
        profiles.put(UUID.fromString("fca93b1f-b981-439e-b9ee-619ef7a4aa2e"), "William Chen");
//        profiles.put(UUID.fromString("80a06f85-1fd7-400f-9b65-58f5adc1a4cf"), "Raphael Leung");
//        profiles.put(UUID.fromString("178c2604-0750-4bbc-a6fc-8f30591d17cb"), "Oren Kanner");

        // Oren's API Key - Rate limit issues?
//        profiles.put(UUID.fromString("9b430640-1a34-4ad7-be1b-ce9dd849d9c1"), "Lec Maj");
//        profiles.put(UUID.fromString("66c09150-2a14-46aa-b146-6b27dc5f983f"), "Aishwarya Pillai");
//        profiles.put(UUID.fromString("efef9cf8-936a-4003-a779-f3237b3e99c8"), "William Chen");
//        profiles.put(UUID.fromString("7009a622-0a58-4eb7-ab97-83dc1ece6c4c"), "Raphael Leung");
//        profiles.put(UUID.fromString("5389d462-22c2-4778-8831-476b3e1d36f6"), "Oren Kanner");
    }

    public String idVoice() throws Exception {
        // convert raw file to wav
        //File wav = new File(mOutputFileNameWav);
        rawToWave(mContext.getFileStreamPath(mOutputFileName), mContext.getFileStreamPath(mOutputFileNameWav));

        // NOT CURRENTLY WORKING, NOT SURE WHY
//        // Set up API Client
//        SpeakerIdentificationRestClient client = new SpeakerIdentificationRestClient(ApiKey);
//
//        // Set up identification request
//        List<Profile> profileList = client.getProfiles();
//        Thread.sleep(250);
//        List<UUID> uuidList = new ArrayList();
//        for(Profile p : profileList) {
//            uuidList.add(p.identificationProfileId);
//        }
//        FileInputStream fileStream = mContext.openFileInput(mOutputFileNameWav);
//        OperationLocation checkUrl = client.identify(fileStream, uuidList, true);
//        String personName;
//
//        // Check operation
//        while(true) {
//            Thread.sleep(1000);
//            IdentificationOperation op = client.checkIdentificationStatus(checkUrl);
//            if(op.status.equals(Status.SUCCEEDED)) {
//                Identification id = op.processingResult;
//
//                if(id.identifiedProfileId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
//                    personName = "Unknown";
//                } else {
//                    personName = profiles.get(id.identifiedProfileId);
//                }
//                break;
//            }
//        }
//
//        Log.d(TAG, "identified as: " + personName);

        // Set up POST request connection
        String listOfIds = TextUtils.join(",",profiles.keySet());
        String url = "https://api.projectoxford.ai/spid/v1.0/identify?identificationProfileIds="+listOfIds+"&shortAudio=true";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Add request headers
        con.setRequestMethod("POST");
        con.setRequestProperty("ocp-apim-subscription-key", ApiKey);
        con.setRequestProperty("Content-Type", "application/octet-stream");

        // Send post request
        con.setDoOutput(true);
        OutputStream conStream = new BufferedOutputStream(con.getOutputStream());
//        InputStream fileStream = mContext.getResources().openRawResource(R.raw.lec_test);
        FileInputStream fileStream = mContext.openFileInput(mOutputFileNameWav);

        while(fileStream.available() > 0) {
            conStream.write((char) fileStream.read());
        }
        conStream.flush();
        conStream.close();

        // Get HTTP response
        int responseCode = con.getResponseCode();
        String responseMsg = con.getResponseMessage();
        Log.d(TAG, "Response Code: " + responseCode);
        Log.d(TAG, "Response Msg: " + responseMsg);

        // Extract header / new URL
        String newUrl = con.getHeaderField("Operation-Location");
        Log.d(TAG, "New URL: " + newUrl);

        // Close first connection
        con.disconnect();

        String profileID, confidence, personName;

        while(true) {
            Thread.sleep(1000);
            String response = getAnalysisOutput(newUrl);
            JSONObject reader = new JSONObject(response);
            Log.d(TAG, "status: " + reader.getString("status"));
            if(reader.getString("status").equals("succeeded")) {
                //Log.d(TAG, "We win!");
                JSONObject results = reader.getJSONObject("processingResult");

                profileID = results.getString("identifiedProfileId");
                confidence = results.getString("confidence");

                if(profileID.equals("00000000-0000-0000-0000-000000000000")) {
                    personName = "Unknown";
                } else {
                    personName = profiles.get(UUID.fromString(profileID));
                }
                break;
            }
        }

        Log.d(TAG, "person: " + personName);
        Log.d(TAG, "confidence: " + confidence);
        return personName;
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

    private String getAnalysisOutput(String newUrl) {
        try {
            URL newObj = new URL(newUrl);
            HttpURLConnection newCon = (HttpURLConnection) newObj.openConnection();
            newCon.setRequestProperty("ocp-apim-subscription-key", ApiKey);
            InputStream in = new BufferedInputStream(newCon.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            String response = total.toString();
            newCon.disconnect();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    // convert raw file captured to WAV before sending
    private void rawToWave(final File rawFile, final File waveFile) throws IOException {
        Log.d(TAG, "got here");
        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, SAMPLE_RATE); // sample rate
            writeInt(output, SAMPLE_RATE * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }
            output.write(bytes.array());
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }
    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }
    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

}
