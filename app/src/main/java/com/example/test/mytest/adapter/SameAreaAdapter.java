package com.example.test.mytest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test.mytest.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/8/10.
 */
public class SameAreaAdapter extends BaseAdapter {
    private Context context;
    private JSONArray listData=null;

    public void setListData(JSONArray listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public SameAreaAdapter(Context context) {
        this.context = context;
        listData=new JSONArray();
    }

    @Override
    public int getCount() {
        try {
            int size = listData.length();
            return size;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return listData.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.same_area_item, null);
            viewHolder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 通过在具体的item上来适配数据
        final JSONObject jo = listData.optJSONObject(position);
        viewHolder.tv_title.setText(jo.optString("title"));

        return convertView;
    }

    public class ViewHolder{
        TextView tv_title;
    }
}
