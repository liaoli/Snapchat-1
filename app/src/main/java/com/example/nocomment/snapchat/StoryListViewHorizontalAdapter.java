package com.example.nocomment.snapchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by guomingsun on 10/10/16.
 */

public class StoryListViewHorizontalAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    public StoryListViewHorizontalAdapter(Context con){
        mInflater= LayoutInflater.from(con);
    }
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    private ViewHolder vh    =new ViewHolder();
    private static class ViewHolder {

        private ImageView im;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.item_in_story_listview, null);
            vh.im=(ImageView)convertView.findViewById(R.id.imgStory);

            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }
        return convertView;
    }
}
