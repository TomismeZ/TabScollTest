package com.example.test.mytest.view;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.test.mytest.R;
import com.example.test.mytest.adapter.MainPagerAdapter;
import com.example.test.mytest.fragment.picture.PhotoFragment;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/18.
 */

public class ScrollImgView1 {
    FragmentActivity activity;
    View mainView;
    public static JSONArray dataArray;
    public static int index=0;
    ViewPager viewPager;
    public ArrayList<Fragment> fragmentList = null;
    private TextView tv_num;

    public ScrollImgView1(Activity activity, View mainView) {
        this.activity = (FragmentActivity) activity;
        this.mainView = mainView;
        getViewId();
        initView();
        addListener();
    }

    private void addListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int page = position % fragmentList.size();
                tv_num.setText((page + 1) + "/" + fragmentList.size());
                index=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        fragmentList = new ArrayList<Fragment>();
    }

    private void getViewId() {
        viewPager = (ViewPager) mainView.findViewById(R.id.scrollimg_vp);
        tv_num = (TextView) mainView.findViewById(R.id.tv_num);
    }


    public void setData(JSONArray dataArray) {
        this.dataArray = dataArray;
        setScrollImgViewData();
        viewPager.setAdapter(new MainPagerAdapter(activity.getSupportFragmentManager(), fragmentList));
//        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % fragmentList.size());
        tv_num.setText((1) + "/" + dataArray.length());
    }


    private void setScrollImgViewData() {
        for (int i = 0; i < dataArray.length(); i++) {
            try {
                PhotoFragment photoFragment = PhotoFragment.newInstance(dataArray.getJSONObject(i).getString("href"));
                fragmentList.add(photoFragment);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
