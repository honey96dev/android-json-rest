package com.example.jsonrest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Screen2Fragment extends Fragment {
    EditText txt1;
    EditText txt2;

    private Handler updateUIHandler = null;
    private final static int MESSAGE_UPDATE_TEXT_CHILD_THREAD =1;
    private final static int MESSAGE_SHOW_TOAST_THREAD =2;

    public Screen2Fragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Screen2Fragment newInstance() {
        Screen2Fragment fragment = new Screen2Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen2, container, false);

        createUpdateUiHandler();
        txt1 = (EditText) rootView.findViewById(R.id.txt1);
        txt2 = (EditText) rootView.findViewById(R.id.txt2);

        Button btn1 = (Button) rootView.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        if (G.SERVER_IP == "" || G.SERVER_PORT == "") {
                            Message message = new Message();
                            message.what = MESSAGE_SHOW_TOAST_THREAD;
                            updateUIHandler.sendMessage(message);
                            return;
                        }
                        String myUrl = String.format("http://%s:%s/update.php", G.SERVER_IP, G.SERVER_PORT);
                        String result;
                        HttpRequest request = new HttpRequest();
                        request.setMethod("POST");
                        request.setDoOutput(true);
                        request.addParam("txt1", txt1.getText().toString());
                        try {
                            result = request.execute(myUrl).get();
                            if (result != null) {
                                Log.e("post-result1", result);
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });

        Button btn2 = (Button) rootView.findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        if (G.SERVER_IP == "" || G.SERVER_PORT == "") {
                            Message message = new Message();
                            message.what = MESSAGE_SHOW_TOAST_THREAD;
                            updateUIHandler.sendMessage(message);
                            return;
                        }
                        String myUrl = String.format("http://%s:%s/update.php", G.SERVER_IP, G.SERVER_PORT);
                        String result;
                        HttpRequest request = new HttpRequest();
                        request.setMethod("POST");
                        request.setDoOutput(true);
                        request.addParam("txt2", txt2.getText().toString());
                        try {
                            result = request.execute(myUrl).get();
                            if (result != null) {
                                Log.e("post-result2", result);
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });

        return rootView;
    }

    private void createUpdateUiHandler()
    {
        if(updateUIHandler == null)
        {
            updateUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // Means the message is sent from child thread.
                    switch (msg.what) {
                        case MESSAGE_UPDATE_TEXT_CHILD_THREAD:
                            break;
                        case MESSAGE_SHOW_TOAST_THREAD:
                            Toast.makeText(getContext(), "Select Server", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };
        }
    }
}