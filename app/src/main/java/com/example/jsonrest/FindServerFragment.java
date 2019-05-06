package com.example.jsonrest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FindServerFragment extends Fragment {
    static final String METHOD_GET = "GET";
    static final int READ_TIMEOUT = 15000;
    static final int CONNECTION_TIMEOUT = 1000;

    ListView mListView;
    ArrayList<ServerDataModel> mServers = new ArrayList<ServerDataModel>();
    FindServerListAdapter mServersAdapter;
    ProgressDialog mProgressDialog;
    int pendingServerCnt;
    String mProgressMsg;

    private Handler updateUIHandler = null;
    private final static int MESSAGE_UPDATE_TEXT_CHILD_THREAD = 1;
    private final static int MESSAGE_SHOW_TOAST_THREAD = 2;
    private final static int MESSAGE_SHOW_PROGRESS_DIALOG_THREAD = 3;
    private final static int MESSAGE_HIDE_PROGRESS_DIALOG_THREAD = 4;
    private final static int MESSAGE_UPDATE_PROGRESS_DIALOG_THREAD = 5;

    public FindServerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FindServerFragment newInstance() {
        FindServerFragment fragment = new FindServerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_server, container, false);

        createUpdateUiHandler();

        Button okButton = (Button) rootView.findViewById(R.id.ok_button);
        Button refreshButton = (Button) rootView.findViewById(R.id.refresh_button);
        Button toggleSelectButton = (Button) rootView.findViewById(R.id.toggle_select_button);
        mListView = (ListView) rootView.findViewById(R.id.list);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle(R.string.title_finding);
        mProgressDialog.setCancelable(false);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ServerDataModel model:
                        mServers) {
                    if (!model.checked) {
                        continue;
                    }
                    model.id = String.valueOf(G.dbHelper.insertServer(model));
                    if (model.id.equals("-1")) {
                        continue;
                    }
                    model.checked = false;
                    G.serverList.add(model);
                }
                getActivity().finish();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findServers();
            }
        });

        toggleSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServersAdapter.toggle();
            }
        });

        mServersAdapter = new FindServerListAdapter(getContext(), mServers);
        mListView.setAdapter(mServersAdapter);

//        findServers();
//        mServers.add(new ServerDataModel("test1", "127.0.0.1", "60"));
//        mServers.add(new ServerDataModel("test2", "127.0.1.1", "60"));
//        mServers.add(new ServerDataModel("test3", "127.0.2.1", "60"));
//        mServers.add(new ServerDataModel("test4", "127.0.3.1", "60"));

        return rootView;
    }

    void findServers() {
        pendingServerCnt = 0;
        Thread thread = new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = MESSAGE_SHOW_PROGRESS_DIALOG_THREAD;
                updateUIHandler.sendMessage(message);

                Context context = getActivity ().getApplicationContext();
                WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
                String myIp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                String[] ipNumbers = myIp.split("\\.");
                Log.e("ip", ipNumbers.toString());
                String ip;
                String port = "60";
                for (int i = 1; i < 255; i++) {
                    ip = String.format("%s.%s.%s.%d", ipNumbers[0], ipNumbers[1], ipNumbers[2], i);
                    if (ip.equals(myIp)) {
                        continue;
                    }
                    findServerByIp(ip, port);
                    mProgressMsg = String.format("%d%s completed", Math.round(i / 2.54), "%");
                    message = new Message();
                    message.what = MESSAGE_UPDATE_PROGRESS_DIALOG_THREAD;
                    updateUIHandler.sendMessage(message);
                }
                message = new Message();
                message.what = MESSAGE_HIDE_PROGRESS_DIALOG_THREAD;
                updateUIHandler.sendMessage(message);
                mServersAdapter.notifyDataSetChanged();
            }
        };
        thread.start();
    }

    private void createUpdateUiHandler() {
        if (updateUIHandler == null) {
            updateUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // Means the message is sent from child thread.
                    switch (msg.what) {
                        case MESSAGE_UPDATE_TEXT_CHILD_THREAD:
                            break;
                        case MESSAGE_SHOW_TOAST_THREAD:
                            break;
                        case MESSAGE_SHOW_PROGRESS_DIALOG_THREAD:
                            mProgressDialog.show();
                            break;
                        case MESSAGE_HIDE_PROGRESS_DIALOG_THREAD:
                            mProgressDialog.cancel();
                            break;
                        case MESSAGE_UPDATE_PROGRESS_DIALOG_THREAD:
                            mProgressDialog.setMessage(mProgressMsg);
                            break;
                    }
                }
            };
        }
    }

    void findServerByIp(String ip, String port) {
        String stringUrl = String.format("http://%s:%s/locate", ip, port);
        String result;
        String inputLine;

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(METHOD_GET);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("Accept","application/json");

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

        if (result == null) {
            return;
        }
        try {
            Log.e("products-api", result);
            JSONObject json = new JSONObject(result);
            JSONObject myApplication = json.getJSONObject("MyApplication");
            JSONObject location = myApplication.getJSONObject("location");
            mServers.add(new ServerDataModel(
                    location.getString("name"),
                    ip,
                    port
            ));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}