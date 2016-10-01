package com.example.nocomment.snapchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;


import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by guomingsun on 30/08/2016.
 */
public class ChatAdapter extends ArrayAdapter<ChatMsg>{

    private ArrayList<ChatMsg> msgList;
    private Activity context;

    public ChatAdapter (Context context, ArrayList<ChatMsg> msg) {
        super(context, R.layout.msg_right ,msg);
    }


    @Override
    public  int getItemViewType(int position){
        ChatMsg item = getItem(position);
        if(item.isMe())
            return 1;
        else
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        
        
        int viewType = getItemViewType(position);
        // Allign the position of the messages which current user sends
        if(viewType == 1){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_right, parent, false);
            
            ChatMsg chatMsg = getItem(position);
            
            TextView textView = (TextView) convertView.findViewById(R.id.txtMsg);
            TextView txtInfo = (TextView) convertView.findViewById(R.id.txtInfo);
            textView.setText(getItem(position).getMsg());
            txtInfo.setText(getItem(position).getTime());
            textView.setBackgroundResource(R.drawable.me_msg_pic);
            
            
            
        }else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_left, parent, false);
            
            TextView textView = (TextView) convertView.findViewById(R.id.txtMsg);
            TextView txtInfo = (TextView) convertView.findViewById(R.id.txtInfoRcv);
            textView.setText(getItem(position).getMsg());
            txtInfo.setText(getItem(position).getTime());
            textView.setBackgroundResource(R.drawable.frd_msg_pic);
        }
        
        TextView textViewInfo = (TextView) convertView.findViewById(R.id.txtInfo);
        textViewInfo.setText(getItem(position).getTime());
        
        //from name
        //            TextView textViewMsgOwner = (TextView) convertView.findViewById(R.id.msgOwner);
        //            textViewMsgOwner.setText(getItem(position).get);
        
        return convertView;
    }


//    private void GoToAlbum(){
//        Intent intent;
//        if(Build.VERSION.SDK_INT < 19){
//            intent = new Intent();
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//
//        }else{
//            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//
//
//
//        }
//    }


}
