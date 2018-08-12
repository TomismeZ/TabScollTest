package com.example.test.mytest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test.mytest.R;
import com.example.test.mytest.widget.SpacingTextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/8/10.
 */
public class AnswerAdapter extends BaseAdapter {
    private Context context;
    private JSONArray listData=null;

    public void setListData(JSONArray listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public AnswerAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.answer_item, null);
            viewHolder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_uname= (TextView) convertView.findViewById(R.id.tv_uname);
            viewHolder.tv_clicks= (TextView) convertView.findViewById(R.id.tv_clicks);
            viewHolder.tv_created_at= (TextView) convertView.findViewById(R.id.tv_created_at);
            viewHolder.tv_replys= (TextView) convertView.findViewById(R.id.tv_replys);
            viewHolder.tv_replys_content= (SpacingTextView) convertView.findViewById(R.id.tv_replys_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 通过在具体的item上来适配数据
        final JSONObject jo = listData.optJSONObject(position);
        String title=jo.optString("title");
        String replys_content=jo.optString("replys_content");
        String uname=jo.optString("uname");
        String clicks=jo.optString("clicks");
        String created_at=jo.optString("created_at");
        String replys=jo.optString("replys");
        if(!title.isEmpty())
            viewHolder.tv_title.setText(title);
        if(!replys_content.isEmpty())
            viewHolder.tv_replys_content.setText(replys_content);

        viewHolder.tv_replys_content.setLetterSpacing(1);

        viewHolder.tv_uname.setText(uname);
        viewHolder.tv_clicks.setText("浏览 "+clicks+" 次");
        viewHolder.tv_created_at.setText(created_at);
        viewHolder.tv_replys.setText(replys);

        return convertView;
    }

    public class ViewHolder{
        TextView tv_title;
        SpacingTextView tv_replys_content;
        TextView tv_uname;
        TextView tv_clicks;
        TextView tv_created_at;
        TextView tv_replys;
    }
}
