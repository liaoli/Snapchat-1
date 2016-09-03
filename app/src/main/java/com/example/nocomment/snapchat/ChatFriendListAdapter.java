package com.example.nocomment.snapchat;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by guomingsun on 3/09/2016.
 */
public class ChatFriendListAdapter extends ArrayAdapter{


    public ChatFriendListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }
}
