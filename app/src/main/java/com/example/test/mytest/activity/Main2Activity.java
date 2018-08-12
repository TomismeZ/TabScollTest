package com.example.test.mytest.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.test.mytest.R;
import com.example.test.mytest.adapter.AnswerAdapter;
import com.example.test.mytest.adapter.BuildingAlbumAdapter;
import com.example.test.mytest.adapter.BuildingLayoutAdapter;
import com.example.test.mytest.adapter.FeatureAdapter;
import com.example.test.mytest.adapter.SameAreaAdapter;
import com.example.test.mytest.helper.PermissionUtils;
import com.example.test.mytest.helper.UserHelper;
import com.example.test.mytest.utils.HttpUtil;
import com.example.test.mytest.view.ScrollChangedScrollView;
import com.example.test.mytest.view.ScrollImgView1;
import com.example.test.mytest.view.SpacesItemDecoration;
import com.example.test.mytest.widget.JustifyTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {
    private final int HANDLER_SHOW_TOAST = 1;
    private final int HANDLER_SETDATA_SUCCESS = 2;

    private Context context;
    private TabLayout tab_tagContainer;
    private ScrollChangedScrollView sv_bodyContainer;
    // 头部导航标签
    private String[] navigationTag = {"详情", "动态", "户型", "地图", "问答", "图片", "同区域"};
    /**
     * 是否是ScrollView主动动作
     * false:是ScrollView主动动作
     * true:是TabLayout 主动动作
     */
    private boolean tagFlag = false;

    /**
     * 用于切换内容模块，相应的改变导航标签，表示当一个所处的位置
     */
    private int lastTagIndex = 0;
    /**
     * 用于在同一个内容模块内滑动，锁定导航标签，防止重复刷新标签
     * true: 锁定
     * false ; 没有锁定
     */
    private LinearLayout ll_detail, ll_dynamic, ll_huxing, ll_map, ll_answer, ll_album, ll_samearea;
    private boolean content2NavigateFlagInnerLock = false;

    //折叠式菜单使用
    private AppBarLayout app_bar;

    //图片
    ScrollImgView1 scrollImgView;

    //楼房信息
    TextView tv_title, tv_price, tv_start_time, tv_join_time, tv_address, tv_lf_address;
    JustifyTextView jtv_price;

    //动态
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;

    //未来
    private JSONArray featureJa = null;
    private FeatureAdapter adapter;
    private RecyclerView recyclerView;

    //基本信息
    private TextView tv_lf_type, tv_building_types, tv_building_type,
            tv_building_state, tv_fitment, tv_property_fee, tv_parking_num,
            tv_developer, tv_property_name, tv_building_detail, tv_build;
    private JustifyTextView jtv_employment, jtv_parking, jtv_developers;

    //户型图
    private RecyclerView rvBuildLayout;
    private JSONArray buildlayoutJa = null;
    private BuildingLayoutAdapter buildingLayoutAdapter;

    //位置及周边
    private MapView mapViewLocation;

    private BDLocationListener myListener = new MyLocationListener();//继承BDAbstractLocationListener的class
     BaiduMap mBaiduMap; //定位地图实例
    public LocationClient mLocationClient = null; //定义LocationClient
    boolean ifFrist = true;//判断是否是第一次进去

    //在线问答
    private ListView lv_answer;
    private JSONArray answerJa = null;
    private AnswerAdapter answerAdapter;
    private JSONObject askBuildingJo = null;
    private boolean isShort = true; //默认只显示3条
    private Button btn_answer;
    private TextView tv_answerNum;

    //楼盘相册
    private RecyclerView rvBuildAlbum;
    private JSONArray buildAlbumJa = null;
    private BuildingAlbumAdapter buildingAlbumAdapter;
    private JSONObject buildAlbumJo = null;
    private TextView tv_album;

    //同区域楼盘
    private ListView lv_sameArea;
    private JSONArray sameAreaJa = null;
    private SameAreaAdapter sameAreaAdapter;
    private JSONObject sameAreaJo = null;

    String toastStr = "";
    private JSONObject buildingDetailData = null;
    private JSONArray imgArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        UserHelper.setWindowStatusBarColor(this, R.color.blcak);
        setContentView(R.layout.activity_main2);

        getViewId();
        refreshView();
        addListener();
        loadHttpData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapViewLocation.onResume();

        //调用LocationClient的start()方法，便可发起定位请求
        mLocationClient.start();
    }

    //添加监听器
    private void addListener() {
        sv_bodyContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //表明当前的动作是由 ScrollView 触发和主导
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tagFlag = true;
                }
                return false;
            }
        });
        sv_bodyContainer.setScrollViewListener(new ScrollChangedScrollView.ScrollViewListener() {

            @Override
            public void onScrollChanged(NestedScrollView scrollView, int x, int y, int oldx, int oldy) {
                scrollRefreshNavigationTag(scrollView);
            }

            @Override
            public void onScrollStop(boolean isStop) {
            }
        });
        tab_tagContainer.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //表明当前的动作是由 TabLayout 触发和主导
                tagFlag = false;
                // 根据点击的位置，使ScrollView 滑动到对应区域
                int position = tab.getPosition();
                // 计算点击的导航标签所对应内容区域的高度
                int targetY = 0;
                switch (position) {
                    case 0:
                        targetY = ll_detail.getTop();
                        break;
                    case 1:
                        targetY = ll_dynamic.getTop();
                        break;
                    case 2:
                        targetY = ll_huxing.getTop();
                        break;
                    case 3:
                        targetY = ll_map.getTop();
                        break;
                    case 4:
                        targetY = ll_answer.getTop();
                        break;
                    case 5:
                        targetY = ll_album.getTop();
                        break;
                    case 6:
                        targetY = ll_samearea.getTop();
                        break;
                    default:
                        break;
                }
                // 移动到对应的内容区域
                sv_bodyContainer.smoothScrollTo(0, targetY + 5);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
//                    UserHelper.showToast(context,"展开");
                    tab_tagContainer.setVisibility(View.GONE);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {

//                        UserHelper.showToast(context,"折叠");
                    tab_tagContainer.setVisibility(View.VISIBLE);
                }

            }
        });

        //点击查看更多
        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShort){
                    answerAdapter.setListData(answerJa);
                    btn_answer.setText("收起");
                    isShort=false;
                }else{
                    answerAdapter.setListData(frontItem(3,answerJa));
                    btn_answer.setText("查看全部回答");
                    isShort=true;
                }
            }
        });

    }

    //获取ViewId
    private void getViewId() {
        tab_tagContainer = (TabLayout) findViewById(R.id.anchor_tagContainer);
        sv_bodyContainer = (ScrollChangedScrollView) findViewById(R.id.anchor_bodyContainer);

        ll_detail = (LinearLayout) findViewById(R.id.ll_detail);
        ll_dynamic = (LinearLayout) findViewById(R.id.ll_dynamic);
        ll_huxing = (LinearLayout) findViewById(R.id.ll_huxing);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);
        ll_answer = (LinearLayout) findViewById(R.id.ll_answer);
        ll_album = (LinearLayout) findViewById(R.id.ll_album);
        ll_samearea = (LinearLayout) findViewById(R.id.ll_samearea);

        app_bar = (AppBarLayout) findViewById(R.id.app_bar);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_join_time = (TextView) findViewById(R.id.tv_join_time);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_lf_address = (TextView) findViewById(R.id.tv_lf_address);
        jtv_price = (JustifyTextView) findViewById(R.id.jtv_price);
        jtv_employment = (JustifyTextView) findViewById(R.id.jtv_employment);
        jtv_parking = (JustifyTextView) findViewById(R.id.jtv_parking);
        jtv_developers = (JustifyTextView) findViewById(R.id.jtv_developers);
        tv_lf_type = (TextView) findViewById(R.id.tv_lf_type);
        tv_building_types = (TextView) findViewById(R.id.tv_building_types); //楼房类型
        tv_building_type = (TextView) findViewById(R.id.tv_building_type); //建筑形式
        tv_building_state = (TextView) findViewById(R.id.tv_building_state); //现房期房
        tv_fitment = (TextView) findViewById(R.id.tv_fitment); //装修状况
        tv_property_fee = (TextView) findViewById(R.id.tv_property_fee); //就业费
        tv_parking_num = (TextView) findViewById(R.id.tv_parking_num); //停车数量
        tv_developer = (TextView) findViewById(R.id.tv_developer); //开发商
        tv_property_name = (TextView) findViewById(R.id.tv_property_name); //物业公司
        tv_building_detail = (TextView) findViewById(R.id.tv_building_detail); //楼房详情
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //动态
        gridView = (GridView) findViewById(R.id.gridview);
        //户型图
        rvBuildLayout = (RecyclerView) findViewById(R.id.rv_huxinng);
        tv_build = (TextView) findViewById(R.id.tv_build);

        //位置及周边
        mapViewLocation = (MapView) findViewById(R.id.mapview_location);

        //在线 问答
        lv_answer = (ListView) findViewById(R.id.lv_answer);
        btn_answer = (Button) findViewById(R.id.btn_answer);
        tv_answerNum = (TextView) findViewById(R.id.tv_answerNum);

        //楼盘相册
        rvBuildAlbum = (RecyclerView) findViewById(R.id.rv_buildingAlbum);
        tv_album = (TextView) findViewById(R.id.tv_album);

        //同区域楼盘
        lv_sameArea = (ListView) findViewById(R.id.lv_samearea);
    }

    private void refreshView() {
        context = this;
        // 添加页内导航标签
        for (String item : navigationTag) {
            tab_tagContainer.addTab(tab_tagContainer.newTab().setText(item));
        }

        imgArray = new JSONArray();
        scrollImgView = new ScrollImgView1(this, findViewById(R.id.scrollimg_rl));
        jtv_price.setTitleWidth(tv_lf_address);
        jtv_developers.setTitleWidth(tv_lf_type);
        jtv_employment.setTitleWidth(tv_lf_type);
        jtv_parking.setTitleWidth(tv_lf_type);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this); //线性，垂直或水平布局
        //设置为水平排列，用setOrientation方法设置
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        /*StaggeredGridLayoutManager()接收两个参数，第一个是指定布局列数，第二个指定布局的排列方向*/
        //实例化瀑布流布局管理器
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //实例化适配器
        adapter = new FeatureAdapter(context);
        //设置recyclerView的布局管理器
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置recyclerView的适配器
        recyclerView.setAdapter(adapter);
        int space = 36;
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));

        //动态
        initGridViewData();
        //户型图
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvBuildLayout.setLayoutManager(linearLayoutManager1);
        buildingLayoutAdapter = new BuildingLayoutAdapter(context);
        rvBuildLayout.setAdapter(buildingLayoutAdapter);
        rvBuildLayout.addItemDecoration(new SpacesItemDecoration(14));

        //位置及周边
        mBaiduMap = mapViewLocation.getMap();
        //开始定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //设置定位图标是否有箭头
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true,null));
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        if (PermissionUtils.getLocationPermissions(Main2Activity.this)) {
            initLocation();
        }

        //在线问答
        answerAdapter = new AnswerAdapter(context);
        lv_answer.setAdapter(answerAdapter);


        //楼盘相册
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvBuildAlbum.setLayoutManager(linearLayoutManager2);
        buildingAlbumAdapter = new BuildingAlbumAdapter(context);
        rvBuildAlbum.setAdapter(buildingAlbumAdapter);
        rvBuildAlbum.addItemDecoration(new SpacesItemDecoration(14));

        //同区域楼盘
        sameAreaAdapter = new SameAreaAdapter(context);
        lv_sameArea.setAdapter(sameAreaAdapter);
    }

    private void initGridViewData() {
        //图标
        int icno[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher
                , R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        //图标下的文字
        String name[] = {"项目概况", "户型图", "楼盘相册",
                "最新动态", "在线问答", "位置及周边"};
        dataList = new ArrayList<>();
        for (int i = 0; i < icno.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text", name[i]);
            dataList.add(map);
        }

        String[] from = {"img", "text"};

        int[] to = {R.id.img, R.id.text};

        simpleAdapter = new SimpleAdapter(this, dataList, R.layout.gridview_item, from, to);

        gridView.setAdapter(simpleAdapter);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

//        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    private void initMore(){
        if(answerJa.length()>3){
            answerAdapter.setListData(frontItem(3,answerJa));
            isShort=true;
            btn_answer.setVisibility(View.VISIBLE);
        }else{
//            isShort=false;
            answerAdapter.setListData(answerJa);
            btn_answer.setVisibility(View.GONE);
        }
    }

    //加载 网络数据
    private void loadHttpData() {
        HttpUtil.sendOkHttpRequest("http://192.168.5.228/api/bddetail?id=9997", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastStr = "加载失败";
                handler.sendEmptyMessage(HANDLER_SHOW_TOAST);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                try {
                    JSONObject jo = new JSONObject(jsonData);
                    JSONObject buildingDetailJo = jo.optJSONObject("buildingDetail");
                    JSONObject buildLayoutDataJo = jo.optJSONObject("buildingLayout");
                    askBuildingJo = jo.optJSONObject("askBuilding");
                    buildAlbumJo = jo.optJSONObject("buildingAlbum");
                    sameAreaJo = jo.optJSONObject("buildingSameArea");
                    buildingDetailData = buildingDetailJo.optJSONObject("data");
                    buildlayoutJa = buildLayoutDataJo.optJSONArray("data");
                    handler.sendEmptyMessage(HANDLER_SETDATA_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    toastStr = "服务器出错";
                    handler.sendEmptyMessage(HANDLER_SHOW_TOAST);
                }

            }
        });
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SHOW_TOAST:
                    UserHelper.showToast(context, toastStr);
                    break;
                case HANDLER_SETDATA_SUCCESS:
                    initUIData();
                    break;

            }
        }
    };

    //初始化UI数据
    private void initUIData() {
        imgArray = buildingDetailData.optJSONArray("imgs");
        scrollImgView.setData(imgArray);

        tv_title.setText(buildingDetailData.optString("title"));
        tv_price.setText(buildingDetailData.optString("price_avg") + "/m2");
        tv_start_time.setText(buildingDetailData.optString("start_time"));
        tv_join_time.setText(buildingDetailData.optString("join_time"));
        tv_address.setText(buildingDetailData.optString("address"));
        featureJa = buildingDetailData.optJSONArray("feature");
        adapter.setListData(featureJa);

        //基本信息
        JSONArray typesJa = buildingDetailData.optJSONArray("types");
        JSONArray building_typeJa = buildingDetailData.optJSONArray("building_type");
        JSONArray fitmentJa = buildingDetailData.optJSONArray("fitment");
        String types = "";
        for (int i = 0; i < typesJa.length(); i++) {
            types += typesJa.optString(i) + " ";
        }
        //截取多余字符
        if (!types.isEmpty())
            types = types.substring(0, types.length() - 1);

        String building_type = "";
        for (int i = 0; i < building_typeJa.length(); i++) {
            building_type += building_typeJa.optString(i) + "、";
        }
        if (!building_type.isEmpty())
            building_type = building_type.substring(0, building_type.length() - 1);

        String fitment = "";
        for (int i = 0; i < fitmentJa.length(); i++) {
            fitment += fitmentJa.optString(i) + ",";
        }
        if (!fitment.isEmpty())
            fitment = fitment.substring(0, fitment.length() - 1);


        tv_building_types.setText(types);
        tv_building_type.setText(building_type);
        tv_fitment.setText(fitment);
        tv_building_state.setText(buildingDetailData.optString("building_state"));
        tv_property_fee.setText(buildingDetailData.optString("property_fee") + "元/平方米*月");
        tv_parking_num.setText(buildingDetailData.optString("parking_num") + "个");
        tv_developer.setText(buildingDetailData.optString("developer"));
        tv_property_name.setText(buildingDetailData.optString("property_name"));
        tv_building_detail.setText(buildingDetailData.optString("details"));

        //户型图
        buildingLayoutAdapter.setListData(buildlayoutJa);
        String buildNum = "户型图  （<font color='#3366cc'>" + buildlayoutJa.length() + "</font>）";
        Spanned msg = Html.fromHtml(buildNum);
        tv_build.setText(msg);

        //在线问答
        answerJa = askBuildingJo.optJSONArray("data");
        String answerNumStr = "在线问答  （<font color='#3366cc'>" + answerJa.length() + "</font>）";
        Spanned answerNum = Html.fromHtml(answerNumStr);
        tv_answerNum.setText(answerNum);
        initMore();
//        answerAdapter.setListData(answerJa);

        //楼房相册
        buildAlbumJa = buildAlbumJo.optJSONArray("data");
        buildingAlbumAdapter.setListData(buildAlbumJa);
        String albumNum = "楼盘相册  （<font color='#3366cc'>" + buildAlbumJa.length() + "</font>）";
        Spanned msg1 = Html.fromHtml(albumNum);
        tv_album.setText(msg1);

        //同区域楼盘
        sameAreaJa = sameAreaJo.optJSONArray("data");
        sameAreaAdapter.setListData(sameAreaJa);
    }

    /**
     * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            Log.i("---------", location.getCityCode() + "---" + latitude + "--" + longitude + "----" + coorType + "--" + location.getCountry() + "--" + location.getCity() + "--" + location.getAddrStr());
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

           // 构造定位数据
//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(100).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();

            // 设置定位数据
//            mBaiduMap.setMyLocationData(locData);

            if (ifFrist) {
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                // 移动到某经纬度
                mBaiduMap.animateMapStatus(update);
                update = MapStatusUpdateFactory.zoomBy(5f);
                // 放大
                mBaiduMap.animateMapStatus(update);

                ifFrist = false;
            }

            // 显示个人位置图标,默认图标
//            MyLocationData.Builder builder = new MyLocationData.Builder();
//            builder.latitude(location.getLatitude());
//            builder.longitude(location.getLongitude());
//            MyLocationData data = builder.build();
//            mBaiduMap.setMyLocationData(data);



            // 充气布局
            View view = View.inflate(Main2Activity.this, R.layout.map_scenic_maker, null);
            // 填充数据
            ImageView iconView = (ImageView) view.findViewById(R.id.maker_icon);
            TextView nameView = (TextView) view.findViewById(R.id.maker_name);
//            if(source != null){
//                iconView.setImageBitmap(source);
//            }
            LatLng point = new LatLng(location.getLatitude(),
                    location.getLongitude());
            nameView.setText(location.getAddrStr());
            // 构建BitmapDescriptor
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .animateType(MarkerOptions.MarkerAnimateType.grow)
                    .zIndex(10)
                    .period(10)
                    .title("place")
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            Marker marker = (Marker)mBaiduMap.addOverlay(option);

        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }


    /**
     * 内容区域滑动刷新导航标签
     *
     * @param scrollView 内容模块容器
     */
    private void scrollRefreshNavigationTag(NestedScrollView scrollView) {
        if (scrollView == null) {
            return;
        }
        // 获得ScrollView滑动距离
        int scrollY = scrollView.getScrollY();
        if (scrollY > ll_samearea.getTop()) {
            refreshContent2NavigationFlag(6);
        } else if (scrollY > ll_album.getTop()) {
            refreshContent2NavigationFlag(5);
        } else if (scrollY > ll_answer.getTop()) {
            refreshContent2NavigationFlag(4);
        } else if (scrollY > ll_map.getTop()) {
            refreshContent2NavigationFlag(3);
        } else if (scrollY > ll_huxing.getTop()) {
            refreshContent2NavigationFlag(2);
        } else if (scrollY > ll_dynamic.getTop()) {
            refreshContent2NavigationFlag(1);
        } else {
            refreshContent2NavigationFlag(0);
        }

    }

    /**
     * 刷新标签
     *
     * @param currentTagIndex 当前模块位置
     */
    private void refreshContent2NavigationFlag(int currentTagIndex) {
        // 上一个位置与当前位置不一致是，解锁内部锁，是导航可以发生变化
        if (lastTagIndex != currentTagIndex) {
            content2NavigateFlagInnerLock = false;
        }
        if (!content2NavigateFlagInnerLock) {
            // 锁定内部锁
            content2NavigateFlagInnerLock = true;
            // 动作是由ScrollView触发主导的情况下，导航标签才可以滚动选中
            if (tagFlag) {
                tab_tagContainer.setScrollPosition(currentTagIndex, 0, true);
            }
        }
        lastTagIndex = currentTagIndex;
    }

    //取前面几个元素
    private JSONArray frontItem(int position,JSONArray ja){
        JSONArray target=new JSONArray();
        if(ja.length()>position){
            for(int i=0;i<position;i++){
                // 遍历jsonarray 数组，把每一个对象转成json对象
                JSONObject job = ja.optJSONObject(i);
                target.put(job);
            }
            return target;
        }else{
            return ja;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapViewLocation.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        mapViewLocation.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQUEST_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        initLocation();
                    } else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setMessage("定位当前位置需要定位权限\n是否去设置");
                        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent()
                                        .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.fromParts("package",
                                                context.getPackageName(), null));
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                }
                break;
            default:
                break;
        }
    }
}
