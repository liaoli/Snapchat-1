package com.example.nocomment.snapchat;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by guoming on 10/8/2016.
 * A class that handles implements the chat list functionality.
 */

public class ChatListAdapter extends ArrayAdapter<String> {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<String> mFrdList;

    public ChatListAdapter(Context context, ArrayList<String> frdList) {
        super(context, R.layout.item_in_friend_list, frdList);
        mContext = context;
        mFrdList = frdList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        if(convertView == null){
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_in_chat_list, null);
            final TextView friend = (TextView) convertView.findViewById(R.id.userNameFriend);

            friend.setText(mFrdList.get(position));

        }
        return convertView;
    }


}