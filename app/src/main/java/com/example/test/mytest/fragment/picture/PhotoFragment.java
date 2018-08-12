package com.example.test.mytest.fragment.picture;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.test.mytest.R;


/**
 * Created by Administrator on 2018/4/8.
 */

public class PhotoFragment extends Fragment {
    private String url;
    public ImageView mPhotoView;
    private Context context;
    private View mView;
    /**
     * 获取这个fragment需要展示图片的url
     * @param url
     * @return
     */
    public static PhotoFragment newInstance(String url) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_photo, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        getViewId();
        init();
        addListener();


    }

    //添加监听器
    private void addListener() {
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UserHelper.showToast(getActivity(),"点击事件"+ ScrollImgView1.dataArray.toString());
//                Activity_ImgPreview.startPreview(getActivity(),ScrollImgView1.index,ScrollImgView1.dataArray.toString(),R.mipmap.ic_launcher,false);
            }
        });
    }

    //获取ViewId
    private void getViewId() {
        mPhotoView = (ImageView) mView.findViewById(R.id.photoview);
    }

    //初始化
    private void init(){
        //设置缩放类型，默认ScaleType.CENTER（可以不设置）
        mPhotoView.setScaleType(ImageView.ScaleType.CENTER);

        Glide.with(context)
                .load(url)
       //         .placeholder(R.mipmap.img_loading_gif)//加载过程中图片未显示时显示的本地图片
                .error(R.mipmap.ic_launcher)//加载异常时显示的图片
//  .centerCrop()//图片图填充ImageView设置的大小
                .fitCenter()//缩放图像测量出来等于或小于ImageView的边界范围,该图像将会完全显示
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(mPhotoView);
    }
}
