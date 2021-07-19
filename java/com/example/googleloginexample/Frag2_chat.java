package com.example.googleloginexample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.googleloginexample.Adapter.SectionPagerAdapter;
import com.example.googleloginexample.chatting.ChatNowFragment;
import com.example.googleloginexample.match.MatchingFragment;
import com.example.googleloginexample.mento_list.MentoListFragment;
import com.google.android.material.tabs.TabLayout;

public class Frag2_chat extends Fragment {
    View view;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag2_chatting,container,false); //frag2.xml과 연결

        viewPager=view.findViewById(R.id.viewPager);
        tabLayout=view.findViewById(R.id.tabLayout);

        return view;
    }
    //call onActivity Create method


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setUpViewPager(ViewPager viewPager) {

        SectionPagerAdapter adapter=new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MentoListFragment(),"멘토 리스트");
        adapter.addFragment(new MatchingFragment(),"내가 찜한");
        adapter.addFragment(new ChatNowFragment(),"채팅하기");

        viewPager.setAdapter(adapter);
    }
}
