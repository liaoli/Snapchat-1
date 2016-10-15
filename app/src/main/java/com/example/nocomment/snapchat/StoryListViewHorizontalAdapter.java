package com.example.nocomment.snapchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by guomingsun on 10/10/16.
 */

public class StoryListViewHorizontalAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<WebItem> webItems;
    private Context context;
    private int size;

    public StoryListViewHorizontalAdapter(Context context, ArrayList<WebItem> webItems, int size){
        mInflater= LayoutInflater.from(context);
        this.context = context;
        this.webItems = webItems;
        this.size = size;
    }
    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    private ViewHolder viewHolder    =new ViewHolder();
    private static class ViewHolder {

        private ImageView imageItem;
        private TextView textItem;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.item_in_story_listview, null);
            viewHolder.imageItem=(ImageView)convertView.findViewById(R.id.imgStory);
            viewHolder.textItem = (TextView) convertView.findViewById(R.id.textStory);


        }

        viewHolder.textItem.setText(webItems.get(position).getWebTitle());
        Picasso.with(context)
                .load(webItems.get(position).getWebUrl())
                .into(viewHolder.imageItem);
        return convertView;
    }

}