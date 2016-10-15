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

        }
        return convertView;
    }

//    public final class ViewHolder{
//        public TextView title;
//        public TextView info;
////        public Button viewBtn;
//    }

//    public void showInfo(int position){
//        ImageView img = new ImageView();
//    }

//    private void setTopHeader(int position){
//
//    }

}