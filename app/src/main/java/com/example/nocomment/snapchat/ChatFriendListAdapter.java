package com.example.nocomment.snapchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by guomingsun on 3/09/2016.
 */
public class ChatFriendListAdapter extends ArrayAdapter<String>{

    private LayoutInflater mInflater;
    private Context mContext;


    public ChatFriendListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//        if(convertView == null){
//            convertView = mInflater.inflate(R.layout.chat_friend_list, parent, false);
//            }
//        TextView header = (TextView) convertView.findViewById(R.id.frientListHeader);
//        String label = getItem(position);
//
//        if(position == 0 || getItem(position-1).charAt(0) != label.charAt(0)){
//            header.setVisibility(View.VISIBLE);
//            header.setText(label.substring(0,1));
//        } else{
//            header.setVisibility(View.GONE);
//        }
//        return super.getView(position, convertView, parent);
//    }
//
//    private void setTopHeader(int position){
//
//    }


}
