package com.example.jsonrest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Screen3Fragment extends Fragment {

    ListView mLst;
    ArrayList<ServerDataModel> mServers = new ArrayList<ServerDataModel>();
    Screen3ListAdapter mServersAdapter;

    public Screen3Fragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Screen3Fragment newInstance() {
        Screen3Fragment fragment = new Screen3Fragment();
//        fragment.mServers.add(new ServerDataModel(
//                "Localhost",
//                "192.168.200.16",
//                "80"
//        ));
        fragment.mServers = G.dbHelper.getAllServers();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen3, container, false);

        Button findButton = (Button) rootView.findViewById(R.id.find_button);
        Button addButton = (Button) rootView.findViewById(R.id.add_button);
        Button deleteAllButton = (Button) rootView.findViewById(R.id.delete_all_button);
        mLst = (ListView) rootView.findViewById(R.id.lst);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a AlertDialog Builder.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                // Set title, icon, can not cancel properties.
                alertDialogBuilder.setTitle(R.string.title_add_server);
                alertDialogBuilder.setIcon(R.drawable.ic_fiber_new_accent_24dp);
                alertDialogBuilder.setCancelable(true);

                // Init popup dialog view and it's ui controls.
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());

                // Inflate the popup dialog from a layout xml file.
                View dialogView = layoutInflater.inflate(R.layout.dialog_add_server, null);

                // Get user input edittext and button ui controls in the popup dialog.
                final EditText nameView = (EditText) dialogView.findViewById(R.id.name_edit_text);
                final EditText ipView = (EditText) dialogView.findViewById(R.id.ip_edit_text);
                final EditText portView = (EditText) dialogView.findViewById(R.id.port_edit_text);
                Button cancelButton = dialogView.findViewById(R.id.cancel_button);
                Button saveButton = dialogView.findViewById(R.id.save_button);

                portView.setText(getString(R.string.default_port));

                alertDialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                // When user click the save user data button in the popup dialog.
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nameView.setError(null);
                        ipView.setError(null);
                        portView.setError(null);

                        String name = nameView.getText().toString().trim();
                        String ip = ipView.getText().toString().trim();
                        String port = portView.getText().toString().trim();
                        ServerDataModel model = new ServerDataModel(name, ip, port);

                        boolean cancel = false;
                        View focusView = null;

                        if (TextUtils.isEmpty(port)) {
                            portView.setError(getString(R.string.error_field_required));
                            focusView = portView;
                            cancel = true;
                        }

                        if (TextUtils.isEmpty(ip)) {
                            ipView.setError(getString(R.string.error_field_required));
                            focusView = ipView;
                            cancel = true;
                        } else if (!Validator.isValidIP4(ip)) {
                            ipView.setError(getString(R.string.error_invalid_ip));
                            focusView = ipView;
                            cancel = true;
                        }

                        if (TextUtils.isEmpty(name)) {
                            nameView.setError(getString(R.string.error_field_required));
                            focusView = nameView;
                            cancel = true;
                        }

                        if (cancel) {
                            focusView.requestFocus();
                        } else {
                            model.id = String.valueOf(G.dbHelper.insertServer(model));
                            mServers.add(model);
                            mServersAdapter.notifyDataSetChanged();
                            alertDialog.cancel();
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                AlertDialog dialog = builder.create();
                dialog.setIcon(R.drawable.ic_delete_accent_24dp);
                dialog.setTitle(R.string.title_delete_all);
                dialog.setMessage(getContext().getString(R.string.message_are_you_sure));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        G.dbHelper.deleteAll();
                        mServers.clear();
                        mServersAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        mServersAdapter = new Screen3ListAdapter(getContext(), mServers);
        mLst.setAdapter(mServersAdapter);

        return rootView;
    }
}