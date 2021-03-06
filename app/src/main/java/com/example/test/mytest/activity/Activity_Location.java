package com.example.test.mytest.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.test.mytest.R;
import com.example.test.mytest.adapter.PlaceListAdapter;
import com.example.test.mytest.helper.UserHelper;

import java.util.ArrayList;
import java.util.List;

public class Activity_Location extends AppCompatActivity {


    MapView mMapView;
    BaiduMap mBaiduMap;
    ImageView mSelectImg;
    // 定位
    LocationClient mLocationClient = null;
    MyBDLocationListner mListner = null;
    BitmapDescriptor mCurrentMarker = null;
    // 当前经纬度
    double mLantitude;
    double mLongtitude;
    LatLng mLoactionLatLng;
    // 设置第一次定位标志
    boolean isFirstLoc = true;
    // MapView中央对于的屏幕坐标
    Point mCenterPoint = null;
    // 地理编码
    GeoCoder mGeoCoder = null;
    // 位置列表
    ListView mListView;
    PlaceListAdapter mAdapter;
    List mInfoList;
    PoiInfo mCurentInfo;
    private String pictruepath;

    String base64Pic = "";
    String loacalpath;
    private ImageButton chat_activity_backBtn;
    private TextView placeTv;
    private ImageView showImg;
    private String addree;
    private PopupWindow mPopupWindow;

    private EditText et_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getViewId();
        initView();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initView();
            }
        }, 2000);
        addListener();
    }

    //添加监听器
    private void addListener() {
        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mGeoCoder.geocode(new GeoCodeOption().city("遵义").address(et_address.getText().toString().trim()));
            }
        });
    }


    //获取ViewID
    private void getViewId() {
        et_address = (EditText) findViewById(R.id.et_address);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        // TODO Auto-generated method stub



        initPop();

        showImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(Activity_Location.this,
//                        PointMapActivity.class);
//                Location1 location = new Location1();
//                location.setLatitude(mCurentInfo.location.latitude);
//                location.setLongitude(mCurentInfo.location.longitude);
//                location.setAddress(mCurentInfo.address);
//                intent.putExtra("pointInfo", location);
//                startActivity(intent);
            }
        });

/*        findViewById(R.id.place_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mCurentInfo != null) {
                    mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {

                        @Override
                        public void onSnapshotReady(Bitmap snapshot) {
                            // TODO Auto-generated method stub
                            if (mCurentInfo != null) {
                                mPopupWindow.showAtLocation(getWindow()
                                        .getDecorView(), Gravity.BOTTOM, 0, 0);
                                showImg.setImageBitmap(snapshot);
                                placeTv.setText(mCurentInfo.name
                                        + mCurentInfo.address);
                                backgroundAlpha(0.1f);
                                mMapView.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }

        });*/
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.chooseplace_bmapView);

        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_marka);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMapTouchListener(touchListener);

        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
            public void onMapClick(LatLng point) {
                mLoactionLatLng = point;
                turnBack();
                resetMarker(point);
            }

            public boolean onMapPoiClick(MapPoi poi) {
                return false;
            }
        });
        // 初始化POI信息列表
        mInfoList = new ArrayList();
        // 初始化当前MapView中心屏幕坐标，初始化当前地理坐标
        mCenterPoint = mBaiduMap.getMapStatus().targetScreen;
        mLoactionLatLng = mBaiduMap.getMapStatus().target;
        // 定位
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this);
        mListner = new MyBDLocationListner();
        mLocationClient.registerLocationListener(mListner);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        // 地理编码
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);
        // 周边位置列表
        mListView = (ListView) findViewById(R.id.place_list);
        mListView.setOnItemClickListener(itemClickListener);
        mAdapter = new PlaceListAdapter(getLayoutInflater(), mInfoList);
        mListView.setAdapter(mAdapter);

        mSelectImg = new ImageView(this);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void initPop() {
        // TODO Auto-generated method stub
        View view = getLayoutInflater().inflate(R.layout.poup_layout, null);
        showImg = (ImageView) view.findViewById(R.id.show_img);
        placeTv = (TextView) view.findViewById(R.id.place_tv);
        mPopupWindow = showPlaceWindow(view);
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                backgroundAlpha(1.0f);
                mMapView.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void resetMarker(LatLng point) {
        // TODO Auto-generated method stub
        OverlayOptions ooC = new MarkerOptions().position(point)
                .icon(mCurrentMarker).zIndex(0);
        Marker marker = (Marker) mBaiduMap.addOverlay(ooC);
        marker.setPosition(point);
        // 实现动画跳转
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLoactionLatLng);
        mBaiduMap.animateMapStatus(u);

    }

    public void turnBack() {
        // 实现动画跳转
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLoactionLatLng);
        mBaiduMap.animateMapStatus(u);

        mBaiduMap.clear();
        // 发起反地理编码检索
        mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                .location(mLoactionLatLng));

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mLocationClient.stop();
        mMapView.onDestroy();
        mGeoCoder.destroy();
        mSuggestionSearch.destroy();
    }

    // 定位监听器
    private class MyBDLocationListner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;

            mLantitude = location.getLatitude();
            mLongtitude = location.getLongitude();
            mLoactionLatLng = new LatLng(mLantitude, mLongtitude);


            // 是否第一次定位
            if (isFirstLoc) {
                resetMarker(mLoactionLatLng);
                isFirstLoc = false;
                // 实现动画跳转
                MapStatusUpdate u = MapStatusUpdateFactory
                        .newLatLng(mLoactionLatLng);
                mBaiduMap.animateMapStatus(u);

                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                        .location(mLoactionLatLng));
                return;
            }

        }

    }

    // 地理编码监听器
    OnGetGeoCoderResultListener GeoListener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检索到结果
                UserHelper.showToast(getApplicationContext(), "抱歉，未能找到结果");
            }
            // 获取地理编码结果
            else {
                // 当前位置信息
                Log.e("Activity_Location", "地址信息：" + result.getAddress());
                UserHelper.showToast(getApplicationContext(), "地址信息：" + result.getAddress());
            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有找到检索结果
            }
            // 获取反向地理编码结果
            else {
                // 当前位置信息
                mCurentInfo = new PoiInfo();
                mCurentInfo.address = result.getAddress();
                mCurentInfo.location = result.getLocation();
                mCurentInfo.name = "[位置]";
                mInfoList.clear();
                mInfoList.add(mCurentInfo);

                // 将周边信息加入表
                if (result.getPoiList() != null) {
                    mInfoList.addAll(result.getPoiList());
                }
                // 通知适配数据已改变
                mAdapter.setmList(mInfoList, 0);
                // placeTv.setText(mCurentInfo.name+mCurentInfo.address);
            }
        }
    };

    private SuggestionSearch mSuggestionSearch;
    private OnGetSuggestionResultListener mOnGetSuggestionResultListener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                //未找到相关结果
                return;
            }

        }
    };

    private void initSuggestionSearch() {
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(mOnGetSuggestionResultListener);
        mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().city("cityName").keyword("address"));
    }

    // 地图触摸事件监听器
    OnMapTouchListener touchListener = new OnMapTouchListener() {
        @Override
        public void onTouch(MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_UP) {

                if (mCenterPoint == null) {
                    return;
                }

                // 获取当前MapView中心屏幕坐标对应的地理坐标
                LatLng currentLatLng;
                currentLatLng = mBaiduMap.getProjection().fromScreenLocation(
                        mCenterPoint);
                System.out.println("----" + mCenterPoint.x);
                System.out.println("----" + currentLatLng.latitude);
                // 发起反地理编码检索
                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                        .location(currentLatLng));

            }
        }
    };

    // listView选项点击事件监听器
    OnItemClickListener itemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub

            // 通知是适配器第position个item被选择了
//            mAdapter.setNotifyTip(position);
//            mBaiduMap.clear();
//            mCurentInfo = (PoiInfo) mAdapter.getItem(position);
//            mLoactionLatLng = mCurentInfo.location;
//            resetMarker(mLoactionLatLng);
//
//            // 选中项打勾
//            mSelectImg.setBackgroundResource(R.mipmap.weigouxuan);
//            mSelectImg = (ImageView) view.findViewById(R.id.place_select);
//            mSelectImg.setImageResource(R.mipmap.gouxuan);
            // placeTv.setText(mCurentInfo.name+mCurentInfo.address);

//            UserHelper.showToast(getApplicationContext(),mCurentInfo.address);

            mCurentInfo = (PoiInfo) mAdapter.getItem(position);
            //数据是使用Intent返回
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.putExtra("address", mCurentInfo.address);
            setResult(RESULT_OK, intent);
            finish();
        }

    };

    public PopupWindow showPlaceWindow(View view) {

        PopupWindow popupWindow = new PopupWindow(this);

        popupWindow.setWidth(LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.anim.push_up_in);
        popupWindow.setContentView(view);

        return popupWindow;
    }

    private Handler handler = new Handler();


}

