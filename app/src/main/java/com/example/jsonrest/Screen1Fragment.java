package com.example.jsonrest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Screen1Fragment extends Fragment {
    EditText txt1;
    EditText txt2;
    EditText txt3;
    EditText txt4;
    EditText txt5;

    String txt1Text = "";
    String txt2Text = "";
    String txt3Text = "";
    String txt4Text = "";
    String txt5Text = "";

    String toastMessage = "";

    private Handler updateUIHandler = null;
    private final static int MESSAGE_UPDATE_TEXT_CHILD_THREAD = 1;
    private final static int MESSAGE_SHOW_TOAST_THREAD = 2;

    public Screen1Fragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Screen1Fragment newInstance() {
        Screen1Fragment fragment = new Screen1Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen1, container, false);

        createUpdateUiHandler();
        txt1 = (EditText) rootView.findViewById(R.id.txt1);
        txt2 = (EditText) rootView.findViewById(R.id.txt2);
        txt3 = (EditText) rootView.findViewById(R.id.txt3);
        txt4 = (EditText) rootView.findViewById(R.id.txt4);
        txt5 = (EditText) rootView.findViewById(R.id.txt5);

        Message message = new Message();
        message.what = MESSAGE_UPDATE_TEXT_CHILD_THREAD;
        updateUIHandler.sendMessage(message);

        Button btn = (Button) rootView.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        if (G.SERVER_IP == "" || G.SERVER_PORT == "") {
                            toastMessage = getString(R.string.message_select_server);
                            Message message = new Message();
                            message.what = MESSAGE_SHOW_TOAST_THREAD;
                            updateUIHandler.sendMessage(message);
                            return;
                        }
                        String myUrl = String.format("http://%s:%s/test.json", G.SERVER_IP, G.SERVER_PORT);
                        //String to place our result in
                        String result;
                        //Instantiate new instance of our class
                        HttpRequest request = new HttpRequest();
                        request.setMethod("GET");
                        //Perform the doInBackground method, passing in our url
                        try {
                            result = request.execute(myUrl).get();
                            if (result == null) {
                                toastMessage = getString(R.string.message_error_request_not_executed);
                                Message message = new Message();
                                message.what = MESSAGE_SHOW_TOAST_THREAD;
                                updateUIHandler.sendMessage(message);
                                return;
                            }

                            JSONObject json = new JSONObject(result);
                            JSONObject app = json.getJSONObject("AppName");
                            JSONObject stats = app.getJSONObject("stats");

                            txt1Text = stats.getString("state1");
                            txt2Text = stats.getString("state2");
                            txt3Text = stats.getString("state3");
                            txt4Text = stats.getString("state4");
                            txt5Text = stats.getString("state5");

                            Message message = new Message();
                            message.what = MESSAGE_SHOW_TOAST_THREAD;
                            updateUIHandler.sendMessage(message);
                        } catch (ExecutionException e) {
                            toastMessage = getString(R.string.message_error_request_not_executed);
                            Message message = new Message();
                            message.what = MESSAGE_SHOW_TOAST_THREAD;
                            updateUIHandler.sendMessage(message);
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            toastMessage = getString(R.string.message_error_request_interrupted);
                            Message message = new Message();
                            message.what = MESSAGE_SHOW_TOAST_THREAD;
                            updateUIHandler.sendMessage(message);
                            e.printStackTrace();
                        } catch (JSONException e) {
                            toastMessage = getString(R.string.message_error_invalid_json);
                            Message message = new Message();
                            message.what = MESSAGE_SHOW_TOAST_THREAD;
                            updateUIHandler.sendMessage(message);
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });

        return rootView;
    }

    private void createUpdateUiHandler() {
        if (updateUIHandler == null) {
            updateUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // Means the message is sent from child thread.
                    switch (msg.what) {
                        case MESSAGE_UPDATE_TEXT_CHILD_THREAD:
                            txt1.setText(txt1Text);
                            txt2.setText(txt2Text);
                            txt3.setText(txt3Text);
                            txt4.setText(txt4Text);
                            txt5.setText(txt5Text);
                            break;
                        case MESSAGE_SHOW_TOAST_THREAD:
                            Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };
        }
    }
}