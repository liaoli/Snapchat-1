package com.example.nocomment.snapchat;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by guomingsun on 30/08/2016.
 */
public class ChatAdapter extends ArrayAdapter<ChatMsg>{

    private ArrayList<ChatMsg> msgList;
    private Context context;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;



    public ChatAdapter (Context context, ArrayList<ChatMsg> msg) {
        super(context, R.layout.msg_right ,msg);
        this.context = context;
    }


    @Override
    public  int getItemViewType(int position){
        ChatMsg item = getItem(position);

        int returnType = 5;
        switch (item.getMsgType()){
            case(ChatMsg.RIGHT_MSG):
                returnType = 0;
                break;
            case(ChatMsg.LEFT_MSG):
                returnType = 1;
                break;
            case(ChatMsg.RIGHT_IMG):
                returnType = 2;
                break;
            case(ChatMsg.LEFT_IMG):
                returnType = 3;
                break;

        }

        return returnType;
    }

    // prevent msgContainer clickable
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

//
        int viewType = getItemViewType(position);
        // Allign the position of the messages which current user sends
        if(viewType == 0){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_right, parent, false);

            ChatMsg chatMsg = getItem(position);

            TextView textView = (TextView) convertView.findViewById(R.id.txtMsg);
            TextView txtInfo = (TextView) convertView.findViewById(R.id.txtInfo);
            textView.setText(getItem(position).getMsg());
            textView.setTextIsSelectable(true);
            txtInfo.setText(getItem(position).getTime());
            textView.setBackgroundResource(R.drawable.me_msg_pic);

        }else if(viewType == 1){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_left, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.txtMsgRcv);
            TextView txtInfo = (TextView) convertView.findViewById(R.id.txtInfoRcv);
            textView.setText(getItem(position).getMsg());
            txtInfo.setText(getItem(position).getTime());
            textView.setBackgroundResource(R.drawable.frd_msg_pic);
        }else if(viewType == 2){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_right_img, parent, false);

//            PhotoViewAttacher mAttacher;
            final ImageView imgMsg = (ImageView) convertView.findViewById(R.id.imgMsg);
            TextView imgInfo = (TextView) convertView.findViewById(R.id.imgInfo);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            imgMsg.setImageBitmap(BitmapFactory.decodeFile(getItem(position).getMsg(),options));
            imgInfo.setText(getItem(position).getTime());

//            mAttacher = new PhotoViewAttacher(imgMsg);
//            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//            Display display = wm.getDefaultDisplay();



            imgMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageViewBitmapWindows.class);
                    intent.putExtra("url", getItem(position).getMsg());
                    context.startActivity(intent);
                }
            });



//            textView.setBackgroundResource(R.drawable.frd_msg_pic);
        } else if(viewType == 3){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_left_img, parent, false);

            ImageView imgMsg = (ImageView) convertView.findViewById(R.id.imgMsgRcv);
            TextView imgInfo = (TextView) convertView.findViewById(R.id.imgInfoRcv);
            Picasso.with(context)
                    .load(getItem(position).getMsg())
                    .resize(50, 50)
                    .centerCrop()
                    .into(imgMsg);


            imgInfo.setText(getItem(position).getTime());

            imgMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageViewWindows.class);
                    intent.putExtra("url", getItem(position).getMsg());
                    context.startActivity(intent);
                }
            });



        }

        return convertView;
    }







}