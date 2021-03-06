package com.example.test.mytest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.test.mytest.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/8/10.
 */
public class BuildingAlbumAdapter extends RecyclerView.Adapter<BuildingAlbumAdapter.ViewHolder>  {

    private JSONArray listData =null;
    private Context context;

    //定义一个内部类ViewHolder，继承自RecyclerView.ViewHolder，用来缓存子项的各个实例，提高效率
    static class ViewHolder extends RecyclerView.ViewHolder{
        View buildLayoutView;
        ImageView  buildImg;
        TextView typeText;


        public ViewHolder(View view){
            super(view);
            buildLayoutView =view;
            buildImg= (ImageView) view.findViewById(R.id.iv_album);
            typeText = (TextView) view.findViewById(R.id.tv_type_text);

        }
    }

    //绑定传入的 数据源
    public BuildingAlbumAdapter(Context context){
        this.context=context;
        listData =new JSONArray();
    }

    public void setListData(JSONArray listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buildalbum_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        //    int position = holder.getAdapterPosition();
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.typeText.setText(listData.optString(position));
        JSONObject jo=listData.optJSONObject(position);
        Glide.with(context)
                .load(jo.optString("href"))
                .error(R.mipmap.ic_launcher)//加载异常时显示的图片
                .fitCenter()//缩放图像测量出来等于或小于ImageView的边界范围,该图像将会完全显示
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.buildImg);
        holder.typeText.setText(jo.optString("type_text"));
    }

    @Override
    public int getItemCount() {
        try {
            int size=listData.length();
            return size;
        }catch (Exception e){
            return 0;
        }

    }
}
