package com.example.jsonrest;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> sections;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        sections = new ArrayList<>();
        sections.add(Screen1Fragment.newInstance());
        sections.add(Screen2Fragment.newInstance());
        sections.add(Screen3Fragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("position", String.valueOf(position));
        return sections.get(position);
    }

    @Override
    public int getCount() {
        return sections.size();
    }
}
