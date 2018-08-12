package com.example.test.mytest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test.mytest.R;

import org.json.JSONArray;

/**
 * Created by Administrator on 2018/8/9.
 */
public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {

    private JSONArray listData =null;
    private Context context;

    //定义一个内部类ViewHolder，继承自RecyclerView.ViewHolder，用来缓存子项的各个实例，提高效率
    static class ViewHolder extends RecyclerView.ViewHolder{
        View featureView;
        TextView featureName;

        public ViewHolder(View view){
            super(view);
            featureView=view;
            featureName= (TextView) view.findViewById(R.id.tv_feature_item);
        }
    }

    //绑定传入的 数据源
    public FeatureAdapter(Context context){
        this.context=context;
        listData =new JSONArray();
    }

    public void setListData(JSONArray listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
    //    int position = holder.getAdapterPosition();
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.featureName.setText(listData.optString(position));
    }

    @Override
    public int getItemCount() {
        return listData.length();
    }


}
