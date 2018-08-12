package com.example.test.mytest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.mytest.adapter.MainPagerAdapter;
import com.example.test.mytest.fragment.FragmentDetail;
import com.example.test.mytest.fragment.TestFragment;
import com.example.test.mytest.view.NoScrollViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    CoordinatorLayout activityRecyclerDetail;

    NoScrollViewPager viewpager;
    public ArrayList<Fragment> fragmentList = null;
    private Context context;

    private ImageView iv1, iv2, iv3,iv4,iv5,iv6;
    private LinearLayout ll_detail,ll_dynamic,ll_huxing,ll_map,ll_question,ll_picture;
    private ImageView mRadioButton_icon[];
    private TextView mRadioButton_text[];
    Drawable img_on[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewId();
        init();
        addListener();
    }

    //添加监听器
    private void addListener() {
        ll_detail.setOnClickListener(this);
        ll_dynamic.setOnClickListener(this);
        ll_huxing.setOnClickListener(this);
        ll_map.setOnClickListener(this);
        ll_question.setOnClickListener(this);
        ll_picture.setOnClickListener(this);

    }

    //获取ViewID
    private void getViewId() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        activityRecyclerDetail = (CoordinatorLayout) findViewById(R.id.activity_recycler_detail);
        viewpager = (NoScrollViewPager) findViewById(R.id.viewpager);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        iv5 = (ImageView) findViewById(R.id.iv5);
        iv6 = (ImageView) findViewById(R.id.iv6);
        ll_detail = (LinearLayout) findViewById(R.id.ll_detail);
        ll_dynamic = (LinearLayout) findViewById(R.id.ll_dynamic);
        ll_huxing = (LinearLayout) findViewById(R.id.ll_huxing);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);
        ll_question = (LinearLayout) findViewById(R.id.ll_question);
        ll_picture = (LinearLayout) findViewById(R.id.ll_picture);

        mRadioButton_icon = new ImageView[6];
        mRadioButton_icon[0] = (ImageView) findViewById(R.id.iv1);
        mRadioButton_icon[1] = (ImageView) findViewById(R.id.iv2);
        mRadioButton_icon[2] = (ImageView) findViewById(R.id.iv3);
        mRadioButton_icon[3] = (ImageView) findViewById(R.id.iv4);
        mRadioButton_icon[4] = (ImageView) findViewById(R.id.iv5);
        mRadioButton_icon[5] = (ImageView) findViewById(R.id.iv6);

        mRadioButton_text = new TextView[6];
        mRadioButton_text[0]= (TextView) findViewById(R.id.radio_button0_text);
        mRadioButton_text[1]= (TextView) findViewById(R.id.radio_button1_text);
        mRadioButton_text[2]= (TextView) findViewById(R.id.radio_button2_text);
        mRadioButton_text[3]= (TextView) findViewById(R.id.radio_button3_text);
        mRadioButton_text[4]= (TextView) findViewById(R.id.radio_button4_text);
        mRadioButton_text[5]= (TextView) findViewById(R.id.radio_button5_text);


    }

    //初始化
    private void init(){
        context = this;
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.mipmap.ic_backoff_white);
        }

        initViewPager();
    }

    //初始化View Pager
    private void initViewPager() {
        fragmentList = new ArrayList<>();

        //定义6个Fragment
        FragmentDetail detailFragment=FragmentDetail.getInstance();
        TestFragment  dynamicFragment=TestFragment.newInstance("动态");
        TestFragment  huxingFragment=TestFragment.newInstance("户型");
        TestFragment  mapFragment=TestFragment.newInstance("地图");
        TestFragment  questionFragment=TestFragment.newInstance("问答");
        TestFragment  pictureFragment=TestFragment.newInstance("图片");
        fragmentList.add(detailFragment);
        fragmentList.add(dynamicFragment);
        fragmentList.add(huxingFragment);
        fragmentList.add(mapFragment);
        fragmentList.add(questionFragment);
        fragmentList.add(pictureFragment);
        viewpager.setmDisableSroll(true);
        viewpager.setOffscreenPageLimit(fragmentList.size()); //预加载
        MainPagerAdapter viewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(viewPagerAdapter);
//        viewpager.setCurrentItem(0);
    }

    //    颜色和icon点击时候改变函数
    public void changeIconAndTextColor(int id) {
        for (int i = 0; i < 6; i++) {
            if (i == id) {
//                mRadioButton_icon[i].setImageDrawable(img_on[i]);
                mRadioButton_icon[i].setVisibility(View.VISIBLE);
                mRadioButton_text[i].setTextColor(context.getResources().getColor(R.color.orange));
            } else {
//                mRadioButton_icon[i].setImageDrawable(img_off[i]);
                mRadioButton_icon[i].setVisibility(View.INVISIBLE);
                mRadioButton_text[i].setTextColor(context.getResources().getColor(R.color.textblack));
            }
        }
    }

    //    初始化icon数据
    private void myInitialize() {
        img_on=new Drawable[1];
        Resources res = getResources();

        img_on[0]=res.getDrawable(R.mipmap.img_line_white);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_detail:
                viewpager.setCurrentItem(0);
                changeIconAndTextColor(0);
                break;
            case R.id.ll_dynamic:
                viewpager.setCurrentItem(1);
                changeIconAndTextColor(1);
                break;
            case R.id.ll_huxing:
                viewpager.setCurrentItem(2);
                changeIconAndTextColor(2);
                break;
            case R.id.ll_map:
                viewpager.setCurrentItem(3);
                changeIconAndTextColor(3);
                break;
            case R.id.ll_question:
                viewpager.setCurrentItem(4);
                changeIconAndTextColor(4);
                break;
            case R.id.ll_picture:
                viewpager.setCurrentItem(5);
                changeIconAndTextColor(5);
                break;
        }
    }
}
