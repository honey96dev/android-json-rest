package com.example.jsonrest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Screen3ListAdapter extends ArrayAdapter<Screen3ListDataModel> {

    private ArrayList<Screen3ListDataModel> dataSet;
    Context mContext;
    static int mLastSelected = -1;

    public int getSelectedIndex() {
        return mLastSelected;
    }

    // View lookup cache
    private static class ViewHolder {
        RadioButton radio;
        TextView txtName;
        TextView txtIp;
        TextView txtPort;
        ImageView imgEdit;
        ImageView imgDelete;
    }

    public Screen3ListAdapter(Context context, ArrayList<Screen3ListDataModel> data) {
        super(context, R.layout.screen3_list_row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Screen3ListDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.screen3_list_row_item, parent, false);
            viewHolder.radio = (RadioButton) convertView.findViewById(R.id.radio);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name_edittext);
            viewHolder.txtIp = (TextView) convertView.findViewById(R.id.ip_edittext);
            viewHolder.txtPort = (TextView) convertView.findViewById(R.id.port_edittext);
            viewHolder.imgEdit = (ImageView) convertView.findViewById(R.id.item_edit_button);
            viewHolder.imgDelete = (ImageView) convertView.findViewById(R.id.item_delete_button);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
//        lastPosition = position;
        viewHolder.radio.setChecked(dataModel.checked);
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtIp.setText(dataModel.getIp());
        viewHolder.txtPort.setText(dataModel.getPort());

        viewHolder.radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int position = (Integer) v.getTag();
                Object object = getItem(position);
                Screen3ListDataModel dataModel = (Screen3ListDataModel) object;
                dataModel.checked = true;

                if (position == mLastSelected) {
                    return;
                }
                if (mLastSelected != -1) {
                    object = getItem(mLastSelected);
                    dataModel = (Screen3ListDataModel) object;
                    dataModel.checked = false;
                }
                G.SERVER_NAME = dataModel.getName();
                G.SERVER_IP = dataModel.getIp();
                G.SERVER_PORT = dataModel.getPort();
                mLastSelected = position;
                notifyDataSetChanged();
            }
        });

        viewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = getItem(position);
                final Screen3ListDataModel dataModel = (Screen3ListDataModel) object;

                // Create a AlertDialog Builder.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                // Set title, icon, can not cancel properties.
                alertDialogBuilder.setTitle(R.string.title_edit_server);
                alertDialogBuilder.setIcon(R.drawable.ic_edit_accent_24dp);
                alertDialogBuilder.setCancelable(true);

                // Init popup dialog view and it's ui controls.
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());

                // Inflate the popup dialog from a layout xml file.
                View dialogView = layoutInflater.inflate(R.layout.dialog_screen3_edit, null);

                // Get user input edittext and button ui controls in the popup dialog.
                final EditText txtName = (EditText) dialogView.findViewById(R.id.name_edittext);
                final EditText txtIp = (EditText) dialogView.findViewById(R.id.ip_edittext);
                final EditText txtPort = (EditText) dialogView.findViewById(R.id.port_edittext);
                Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                Button btnSave = dialogView.findViewById(R.id.btnSave);

                // Display values from the main activity list view in user input edittext.
                txtName.setText(dataModel.getName());
                txtIp.setText(dataModel.getIp());
                txtPort.setText(dataModel.getPort());


                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder.setView(dialogView);

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                // When user click the save user data button in the popup dialog.
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dataModel.name = txtName.getText().toString();
                        dataModel.ip = txtIp.getText().toString();
                        dataModel.port = txtPort.getText().toString();
                        alertDialog.cancel();
                        Log.e(String.valueOf(position), String.valueOf(mLastSelected));
                        if (position == mLastSelected) {
                            G.SERVER_NAME = dataModel.name;
                            G.SERVER_IP = dataModel.ip;
                            G.SERVER_PORT = dataModel.port;
                        }
                        notifyDataSetChanged();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
            }
        });

        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                AlertDialog dialog = builder.create();
                dialog.setIcon(R.drawable.ic_delete_accent_24dp);
                dialog.setTitle(R.string.title_delete_item);
                dialog.setMessage(getContext().getString(R.string.message_are_you_sure));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (position == mLastSelected) {
                            mLastSelected = -1;
                        } else if (position < mLastSelected) {
                            mLastSelected--;
                        }
                        dataSet.remove(position);
                        notifyDataSetChanged();
                    }
                });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        return convertView;
    }
}
