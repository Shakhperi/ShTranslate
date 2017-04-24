package com.example.macbookairmd760.yandexTranslate.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.macbookairmd760.yandexTranslate.R;

import java.util.ArrayList;

/**
 * Created by macbookairmd760 on 20.04.17.
 */
public class MainActivity extends AppCompatActivity  {
    private FragmentManager fragmentManager;
    Toolbar toolbar;
    ArrayList<Fragment> fragments = new ArrayList<>();

    private AHBottomNavigation bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.home_tool_bar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        bottomNavigationView = (AHBottomNavigation) findViewById(R.id.bottom_navigation_view);
        setupNavigationBottomView();

        bottomNavigationView.setCurrentItem(0);

    }

    private void setupNavigationBottomView() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_title_translate, R.drawable.ic_translate, R.color.background);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_title_history, R.drawable.ic_history, R.color.background);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_title_settings, R.drawable.ic_settings, R.color.background);

        bottomNavigationView.addItem(item1);
        bottomNavigationView.addItem(item2);
        bottomNavigationView.addItem(item3);

        bottomNavigationView.setDefaultBackgroundColor(Color.parseColor("#FAFAFA"));
        bottomNavigationView.setBehaviorTranslationEnabled(false);
        bottomNavigationView.setAccentColor(Color.parseColor("#673AB7"));

        bottomNavigationView.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigationView.setColored(false);

        fragments.add(new TranslateFragment());
        fragments.add(new ListWordTabLayoutFragment());
        fragments.add(new SettingsFragment());

        bottomNavigationView.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                //добавила этот код перед сном
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment fragment = fragments.get(position);
                transaction.replace(R.id.main_container, fragment).commit();

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        int pos = bottomNavigationView.getCurrentItem();
        if (pos != 0) {
            bottomNavigationView.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }
}
