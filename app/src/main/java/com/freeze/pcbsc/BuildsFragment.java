package com.freeze.pcbsc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class BuildsFragment  extends Fragment{

    FragmentAdapter adapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.builds, container, false);
        tabLayout=v.findViewById(R.id.tabLayout);
        viewPager=v.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        adapter = new FragmentAdapter(getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);
        return v;
    }

    public void switchTab(int i){
        viewPager.setCurrentItem(i);
        MainActivity ma = (MainActivity) getActivity();
        ma.getData();
    }

}