package com.example.jsonrest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    BottomNavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        G.dbHelper = new DatabaseHelper(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        mNavigationView.setSelectedItemId(R.id.navigation_screen1);
                        break;
                    case 1:
                        mNavigationView.setSelectedItemId(R.id.navigation_screen2);
                        break;
                    case 2:
                        mNavigationView.setSelectedItemId(R.id.navigation_screen3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_screen1:
//                    mTextMessage.setText(R.string.title_home);
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_screen2:
//                    mTextMessage.setText(R.string.title_dashboard);
                        mViewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_screen3:
//                    mTextMessage.setText(R.string.title_notifications);
                        mViewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
    }

}
