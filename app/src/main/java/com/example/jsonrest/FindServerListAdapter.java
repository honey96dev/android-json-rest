package com.example.jsonrest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class FindServerListAdapter extends ArrayAdapter<ServerDataModel> {

    private ArrayList<ServerDataModel> mDataSet;
    Context mContext;
    static int mLastSelected = -1;
    boolean allChecked;

    public int getSelectedIndex() {
        return mLastSelected;
    }

    // View lookup cache
    private static class ViewHolder {
        CheckBox checkbox;
        TextView txtName;
        TextView txtIp;
        TextView txtPort;
    }

    public FindServerListAdapter(Context context, ArrayList<ServerDataModel> data) {
        super(context, R.layout.list_row_server, data);
        this.mDataSet = data;
        this.mContext = context;
        allChecked = false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ServerDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_row_server_with_checkbox, parent, false);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name_edittext);
            viewHolder.txtIp = (TextView) convertView.findViewById(R.id.ip_edittext);
            viewHolder.txtPort = (TextView) convertView.findViewById(R.id.port_edittext);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkbox.setChecked(dataModel.checked);
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtIp.setText(dataModel.getIp());
        viewHolder.txtPort.setText(dataModel.getPort());

        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = getItem(position);
                ServerDataModel dataModel = (ServerDataModel) object;
                dataModel.checked = !dataModel.checked;

                int checkedCnt = 0;
                for (ServerDataModel model:
                        mDataSet) {
                    checkedCnt += model.checked ? 1 : 0;
                }
                if (checkedCnt == mDataSet.size()) {
                    allChecked = true;
                } else {
                    allChecked = false;
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    void toggle() {
        allChecked = !allChecked;
        for (ServerDataModel model:
             mDataSet) {
            model.checked = allChecked;
        }
        notifyDataSetChanged();
    }
}
