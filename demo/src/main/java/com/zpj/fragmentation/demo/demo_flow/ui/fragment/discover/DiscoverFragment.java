package com.zpj.fragmentation.demo.demo_flow.ui.fragment.discover;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.demo.demo_flow.adapter.DiscoverFragmentAdapter;
import com.zpj.fragmentation.demo.demo_flow.base.BaseMainFragment;

/**
 * Created by YoKeyword on 16/2/3.
 */
public class DiscoverFragment extends BaseMainFragment {

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mToolbar.setTitle(R.string.discover);
        initToolbarNav(mToolbar);

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.recommend));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.hot));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.favorite));
        mViewPager.setAdapter(new DiscoverFragmentAdapter(getChildFragmentManager(), getString(R.string.recommend), getString(R.string.hot), getString(R.string.favorite)));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
