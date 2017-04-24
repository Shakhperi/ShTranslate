package com.example.macbookairmd760.yandexTranslate.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macbookairmd760.yandexTranslate.View.ListWordPagerAdapter;
import com.example.macbookairmd760.yandexTranslate.R;

/**
 * Created by macbookairmd760 on 20.04.17.
 */
public class ListWordTabLayoutFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListWordPagerAdapter adapter;

    public void ListWordTabLayoutFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_layout, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ListWordPagerAdapter(getChildFragmentManager());

        ListWordFragment fragmentFavorite = new ListWordFragment();
        Bundle args = new Bundle();
        args.putBoolean("isFavorite", true);
        fragmentFavorite.setArguments(args);

        String historyTitle = getResources().getString(R.string.title_history);
        String favoritesTitle = getResources().getString(R.string.title_favorites);

        adapter.addFragment(new ListWordFragment(), historyTitle);
        adapter.addFragment(fragmentFavorite,favoritesTitle);

        viewPager.setAdapter(adapter);
    }
}
