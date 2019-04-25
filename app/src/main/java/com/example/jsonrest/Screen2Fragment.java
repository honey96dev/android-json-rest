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

import java.util.concurrent.ExecutionException;

public class Screen2Fragment extends Fragment {
    EditText txt1View;
    EditText txt2View;

    String toastMessage = "";

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
        txt1View = (EditText) rootView.findViewById(R.id.txt1_edit_text);
        txt2View = (EditText) rootView.findViewById(R.id.txt2_edit_text);

        Button btn1 = (Button) rootView.findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
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
                        String myUrl = String.format("http://%s:%s/one", G.SERVER_IP, G.SERVER_PORT);
                        String result;
                        HttpRequest request = new HttpRequest();
                        request.setMethod("POST");
                        request.setDoOutput(true);
                        request.addParam("a", txt1View.getText().toString());
                        try {
                            result = request.execute(myUrl).get();
                            if (result != null) {
                                toastMessage = getString(R.string.message_success_update);
                                Message message = new Message();
                                message.what = MESSAGE_SHOW_TOAST_THREAD;
                                updateUIHandler.sendMessage(message);
                                Log.e("post-result2", result);
                            } else {
                                toastMessage = getString(R.string.error_request_not_executed);
                                Message message = new Message();
                                message.what = MESSAGE_SHOW_TOAST_THREAD;
                                updateUIHandler.sendMessage(message);
                            }
                        } catch (ExecutionException e) {
                            toastMessage = getString(R.string.error_request_not_executed);
                            Message message = new Message();
                            message.what = MESSAGE_SHOW_TOAST_THREAD;
                            updateUIHandler.sendMessage(message);
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            toastMessage = getString(R.string.error_request_interrupted);
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

        Button btn2 = (Button) rootView.findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
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
                        String myUrl = String.format("http://%s:%s/two", G.SERVER_IP, G.SERVER_PORT);
                        String result;
                        HttpRequest request = new HttpRequest();
                        request.setMethod("POST");
                        request.setDoOutput(true);
                        request.addParam("a", txt2View.getText().toString());
                        try {
                            result = request.execute(myUrl).get();
                            if (result != null) {
                                toastMessage = getString(R.string.message_success_update);
                                Message message = new Message();
                                message.what = MESSAGE_SHOW_TOAST_THREAD;
                                updateUIHandler.sendMessage(message);
                                Log.e("post-result2", result);
                            } else {
                                toastMessage = getString(R.string.error_request_not_executed);
                                Message message = new Message();
                                message.what = MESSAGE_SHOW_TOAST_THREAD;
                                updateUIHandler.sendMessage(message);
                            }
                        } catch (ExecutionException e) {
                            toastMessage = getString(R.string.error_request_not_executed);
                            Message message = new Message();
                            message.what = MESSAGE_SHOW_TOAST_THREAD;
                            updateUIHandler.sendMessage(message);
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            toastMessage = getString(R.string.error_request_interrupted);
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
                            Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };
        }
    }
}