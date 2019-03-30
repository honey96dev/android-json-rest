package com.example.jsonrest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends AsyncTask<String, Void, String> {
    String mMethod = "GET";
    boolean mDoInput = true;
    boolean mDoOutput = false;
    Map<String, Object> mParams = new HashMap<>();
    JSONObject mJson;
    static final int READ_TIMEOUT = 15000;
    static final int CONNECTION_TIMEOUT = 15000;

    public void setMethod(String method) {
        mMethod = method;
    }

    public void setDoInput(boolean bool) {
        mDoInput = bool;
    }

    public void setDoOutput(boolean bool) {
        mDoOutput = bool;
    }

    public void addParam(String key, Object val) {
        mParams.put(key, val);
    }

    public void clearParams() {
        mParams.clear();
    }

    void buildJson() {
        mJson = null;
        mJson = new JSONObject();
        for (Map.Entry<String, Object> entry : mParams.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
            try {
                mJson.accumulate(entry.getKey(), entry.getValue().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String result;
        String inputLine;
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(mMethod);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
//            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoOutput(mDoOutput);
            connection.setDoInput(mDoInput);

            if (mDoOutput) {
                buildJson();
                Log.e("Json-String", mJson.toString());
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(mJson.toString());
                writer.flush();
                writer.close();
                os.close();
            }

            Log.e("url", connection.getURL().toString());
            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            result = stringBuilder.toString();
        }
        catch(IOException e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}
