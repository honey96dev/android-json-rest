package com.example.jsonrest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Screen3Fragment extends Fragment {

    ListView mLst;
    ArrayList<Screen3ListDataModel> mListItems = new ArrayList<Screen3ListDataModel>();
    Screen3ListAdapter mListAdapter;

    public Screen3Fragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Screen3Fragment newInstance() {
        Screen3Fragment fragment = new Screen3Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen3, container, false);

        mLst = (ListView) rootView.findViewById(R.id.lst);
        mListAdapter = new Screen3ListAdapter(getContext(), mListItems);
        mLst.setAdapter(mListAdapter);
        mListItems.add(new Screen3ListDataModel(
                "Localhost",
                "192.168.200.16",
                "80"
        ));
        for (int i = 0; i < 100; i++) {
            mListItems.add(new Screen3ListDataModel(
                    String.valueOf(i),
                    "ip",
                    "port"
            ));
        }

        return rootView;
    }
}