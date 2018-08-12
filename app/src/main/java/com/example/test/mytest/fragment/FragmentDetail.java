package com.example.test.mytest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test.mytest.R;
import com.example.test.mytest.adapter.AnswerAdapter;
import com.example.test.mytest.adapter.BuildingAlbumAdapter;
import com.example.test.mytest.adapter.BuildingLayoutAdapter;
import com.example.test.mytest.adapter.FeatureAdapter;
import com.example.test.mytest.adapter.SameAreaAdapter;
import com.example.test.mytest.helper.UserHelper;
import com.example.test.mytest.utils.HttpUtil;
import com.example.test.mytest.view.ScrollImgView1;
import com.example.test.mytest.view.SpacesItemDecoration;
import com.example.test.mytest.widget.JustifyTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/8/9.
 */
public class FragmentDetail extends Fragment {
    private final int HANDLER_SHOW_TOAST = 1;
    private final int HANDLER_SETDATA_SUCCESS = 2;
    private View mView;

    public static FragmentDetail instance = null;//单例模式
    private Context context;

    //图片
    ScrollImgView1 scrollImgView;

    //楼房信息
    TextView tv_title,tv_price,tv_start_time,tv_join_time,tv_address,tv_lf_address;
    JustifyTextView jtv_price;

    //未来
    private JSONArray featureJa=null;
    private FeatureAdapter adapter;
    private RecyclerView recyclerView;

    //基本信息
    private TextView tv_lf_type,tv_building_types,tv_building_type,
            tv_building_state,tv_fitment,tv_property_fee,tv_parking_num,
            tv_developer,tv_property_name,tv_building_detail,tv_build;
    private JustifyTextView jtv_employment,jtv_parking,jtv_developers;

    //户型图
    private RecyclerView rvBuildLayout;
    private JSONArray buildlayoutJa=null;
    private BuildingLayoutAdapter buildingLayoutAdapter;

    //在线问答
    private ListView lv_answer;
    private JSONArray answerJa=null;
    private AnswerAdapter answerAdapter;
    private JSONObject askBuildingJo=null;

    //楼盘相册
    private  RecyclerView rvBuildAlbum;
    private JSONArray buildAlbumJa=null;
    private BuildingAlbumAdapter buildingAlbumAdapter;
    private  JSONObject buildAlbumJo=null;
    private TextView tv_album;

    //同区域楼盘
    private ListView lv_sameArea;
    private JSONArray sameAreaJa=null;
    private SameAreaAdapter sameAreaAdapter;
    private JSONObject  sameAreaJo=null;

    String toastStr = "";
    private JSONObject buildingDetailData=null;
    private JSONArray imgArray=null;
    public static FragmentDetail getInstance() {
        if (instance == null) {
            instance = new FragmentDetail();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_detail, container, false);
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getActivity();
        getViwId();
        init();
        loadHttpData();
    }

    //获取ViewId
    private void getViwId() {
        tv_title= (TextView) mView.findViewById(R.id.tv_title);
        tv_price= (TextView) mView.findViewById(R.id.tv_price);
        tv_start_time= (TextView) mView.findViewById(R.id.tv_start_time);
        tv_join_time= (TextView) mView.findViewById(R.id.tv_join_time);
        tv_address= (TextView) mView.findViewById(R.id.tv_address);
        tv_lf_address= (TextView) mView.findViewById(R.id.tv_lf_address);
        jtv_price= (JustifyTextView) mView.findViewById(R.id.jtv_price);
        jtv_employment= (JustifyTextView) mView.findViewById(R.id.jtv_employment);
        jtv_parking= (JustifyTextView) mView.findViewById(R.id.jtv_parking);
        jtv_developers= (JustifyTextView) mView.findViewById(R.id.jtv_developers);
        tv_lf_type= (TextView) mView.findViewById(R.id.tv_lf_type);
        tv_building_types= (TextView) mView.findViewById(R.id.tv_building_types); //楼房类型
        tv_building_type= (TextView) mView.findViewById(R.id.tv_building_type); //建筑形式
        tv_building_state= (TextView) mView.findViewById(R.id.tv_building_state); //现房期房
        tv_fitment= (TextView) mView.findViewById(R.id.tv_fitment); //装修状况
        tv_property_fee= (TextView) mView.findViewById(R.id.tv_property_fee); //就业费
        tv_parking_num= (TextView) mView.findViewById(R.id.tv_parking_num); //停车数量
        tv_developer= (TextView) mView.findViewById(R.id.tv_developer); //开发商
        tv_property_name= (TextView) mView.findViewById(R.id.tv_property_name); //物业公司
        tv_building_detail= (TextView) mView.findViewById(R.id.tv_building_detail); //楼房详情
        recyclerView= (RecyclerView) mView.findViewById(R.id.recycler_view);

        //户型图
        rvBuildLayout= (RecyclerView) mView.findViewById(R.id.rv_huxinng);
        tv_build= (TextView) mView.findViewById(R.id.tv_build);

        //在线 问答
        lv_answer= (ListView) mView.findViewById(R.id.lv_answer);

        //楼盘相册
        rvBuildAlbum= (RecyclerView) mView.findViewById(R.id.rv_buildingAlbum);
        tv_album= (TextView) mView.findViewById(R.id.tv_album);

        //同区域楼盘
        lv_sameArea= (ListView) mView.findViewById(R.id.lv_samearea);
    }

    //初始化
    private void init(){
        imgArray=new JSONArray();
        scrollImgView = new ScrollImgView1(getActivity(), mView.findViewById(R.id.scrollimg_rl));
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
        int space=36;
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));

        //户型图
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvBuildLayout.setLayoutManager(linearLayoutManager1);
        buildingLayoutAdapter=new BuildingLayoutAdapter(context);
        rvBuildLayout.setAdapter(buildingLayoutAdapter);
        rvBuildLayout.addItemDecoration(new SpacesItemDecoration(14));

        //在线问答
        answerAdapter=new AnswerAdapter(context);
        lv_answer.setAdapter(answerAdapter);

        //楼盘相册
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvBuildAlbum.setLayoutManager(linearLayoutManager2);
        buildingAlbumAdapter=new BuildingAlbumAdapter(context);
        rvBuildAlbum.setAdapter(buildingAlbumAdapter);
        rvBuildAlbum.addItemDecoration(new SpacesItemDecoration(14));

        //同区域楼盘
        sameAreaAdapter=new SameAreaAdapter(context);
        lv_sameArea.setAdapter(sameAreaAdapter);
    }


    //加载 网络数据
    private void loadHttpData() {
        HttpUtil.sendOkHttpRequest("http://192.168.5.228/api/bddetail?id=9997", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastStr="加载失败";
                handler.sendEmptyMessage(HANDLER_SHOW_TOAST);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData=response.body().string();
                try {
                    JSONObject jo=new JSONObject(jsonData);
                    JSONObject buildingDetailJo=jo.optJSONObject("buildingDetail");
                    JSONObject buildLayoutDataJo=jo.optJSONObject("buildingLayout");
                    askBuildingJo=jo.optJSONObject("askBuilding");
                    buildAlbumJo=jo.optJSONObject("buildingAlbum");
                    sameAreaJo=jo.optJSONObject("buildingSameArea");
                    buildingDetailData=buildingDetailJo.optJSONObject("data");
                    buildlayoutJa=buildLayoutDataJo.optJSONArray("data");
                    handler.sendEmptyMessage(HANDLER_SETDATA_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    toastStr="服务器出错";
                    handler.sendEmptyMessage(HANDLER_SHOW_TOAST);
                }

            }
        });
    }



    private Handler handler=new Handler(){
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
        imgArray=buildingDetailData.optJSONArray("imgs");
        scrollImgView.setData(imgArray);

        tv_title.setText(buildingDetailData.optString("title"));
        tv_price.setText(buildingDetailData.optString("price_avg")+"/m2");
        tv_start_time.setText(buildingDetailData.optString("start_time"));
        tv_join_time.setText(buildingDetailData.optString("join_time"));
        tv_address.setText(buildingDetailData.optString("address"));
        featureJa=buildingDetailData.optJSONArray("feature");
        adapter.setListData(featureJa);

        //基本信息
        JSONArray typesJa=buildingDetailData.optJSONArray("types");
        JSONArray building_typeJa=buildingDetailData.optJSONArray("building_type");
        JSONArray fitmentJa=buildingDetailData.optJSONArray("fitment");
        String  types="";
        for(int i=0;i<typesJa.length();i++){
            types += typesJa.optString(i)+" ";
        }
        //截取多余字符
        if(!types.isEmpty())
            types=types.substring(0,types.length()-1);

        String building_type="";
        for(int i=0;i<building_typeJa.length();i++){
            building_type +=building_typeJa.optString(i)+"、";
        }
        if(!building_type.isEmpty())
            building_type=building_type.substring(0,building_type.length()-1);

        String fitment="";
        for(int i=0;i<fitmentJa.length();i++){
            fitment +=fitmentJa.optString(i)+",";
        }
        if(!fitment.isEmpty())
            fitment=fitment.substring(0,fitment.length()-1);


        tv_building_types.setText(types);
        tv_building_type.setText(building_type);
        tv_fitment.setText(fitment);
        tv_building_state.setText(buildingDetailData.optString("building_state"));
        tv_property_fee.setText(buildingDetailData.optString("property_fee")+"元/平方米*月");
        tv_parking_num.setText(buildingDetailData.optString("parking_num")+"个");
        tv_developer.setText(buildingDetailData.optString("developer"));
        tv_property_name.setText(buildingDetailData.optString("property_name"));
        tv_building_detail.setText(buildingDetailData.optString("details"));

        //户型图
        buildingLayoutAdapter.setListData(buildlayoutJa);
        String buildNum="户型图  （<font color='#3366cc'>"+buildlayoutJa.length()+"</font>）";
        Spanned msg= Html.fromHtml(buildNum);
        tv_build.setText(msg);

        //在线问答
        answerJa=askBuildingJo.optJSONArray("data");
        answerAdapter.setListData(answerJa);

        //楼房相册
        buildAlbumJa=buildAlbumJo.optJSONArray("data");
        buildingAlbumAdapter.setListData(buildAlbumJa);
        String albumNum="楼盘相册  （<font color='#3366cc'>"+buildAlbumJa.length()+"</font>）";
        Spanned msg1= Html.fromHtml(albumNum);
        tv_album.setText(msg1);

        //同区域楼盘
        sameAreaJa=sameAreaJo.optJSONArray("data");
        sameAreaAdapter.setListData(sameAreaJa);
    }
}
