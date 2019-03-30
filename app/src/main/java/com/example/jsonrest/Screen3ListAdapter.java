package com.example.jsonrest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Screen3ListAdapter extends ArrayAdapter<Screen3ListDataModel> {

    private ArrayList<Screen3ListDataModel> dataSet;
    Context mContext;
    int mLastSelected = -1;

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
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtIp = (TextView) convertView.findViewById(R.id.ip);
            viewHolder.txtPort = (TextView) convertView.findViewById(R.id.port);
            viewHolder.imgEdit = (ImageView) convertView.findViewById(R.id.item_edit);
            viewHolder.imgDelete = (ImageView) convertView.findViewById(R.id.item_delete);

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
                mLastSelected = position;
                notifyDataSetChanged();
            }
        });

        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                AlertDialog dialog = builder.create();
                dialog.setIcon(android.R.drawable.ic_delete);
                dialog.setTitle("Delete Item");
                dialog.setMessage("Are you sure");
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
