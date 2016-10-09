package com.example.nocomment.snapchat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
    private ArrayList<String> mFrdList;


    public ChatFriendListAdapter(Context context, ArrayList<String> frdList) {
        super(context, R.layout.item_in_friend_list, frdList);
        mContext = context;
        mFrdList = frdList;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        if(convertView == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = mInflater.inflate(R.layout.item_in_friend_list, parent, false);
            convertView = mInflater.inflate(R.layout.item_in_friend_list, null);

        }

        final ImageView addFriend = (ImageView) convertView.findViewById(R.id.addFriend);
        final TextView friend = (TextView) convertView.findViewById(R.id.userNameFriend);
        final TextView header = (TextView) convertView.findViewById(R.id.frientListHeader);

        String label = getItem(position);
        if (position == 0
                || getItem(position - 1).charAt(0) != label.charAt(0)) {
            header.setVisibility(View.VISIBLE);
            header.setText(label.substring(0, 1));
        } else {
            header.setVisibility(View.GONE);
        }

        friend.setText(mFrdList.get(position));
        addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String response =Util.sendFriendRequest("a", friend.getText().toString());
//                            addFriend.setVisibility(View.GONE);
                        }
                    }).start();

                }
            });


        return convertView;
//        return super.getView(position, convertView, parent);

    }



}
