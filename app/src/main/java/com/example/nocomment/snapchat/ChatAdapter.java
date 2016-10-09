package com.example.nocomment.snapchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
        if(item.getMsgType() == ChatMsg.RIGHT_MSG)
            return 0;
        else if(item.getMsgType() == ChatMsg.LEFT_MSG)
            return 1;
        else if(item.getMsgType() == ChatMsg.RIGHT_IMG)
            return 2;
        else
            return 3;
    }

    // prevent msgContainer clickable
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        
        
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

            ImageView imgMsg = (ImageView) convertView.findViewById(R.id.imgMsg);
            TextView imgInfo = (TextView) convertView.findViewById(R.id.imgInfo);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            imgMsg.setImageBitmap(BitmapFactory.decodeFile(getItem(position).getMsg(),options));
            imgInfo.setText(getItem(position).getTime());
//            textView.setBackgroundResource(R.drawable.frd_msg_pic);
        } else if(viewType == 3){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.msg_left_img, parent, false);

            ImageView imgMsg = (ImageView) convertView.findViewById(R.id.imgMsg);
            TextView imgInfo = (TextView) convertView.findViewById(R.id.imgInfo);
//            new DownloadImageTask(imgMsg)
//                    .execute(getItem(position).getMsg());
            URL newurl = null;
            try {
                newurl = new URL(getItem(position).getMsg());

                imgMsg.setImageBitmap(BitmapFactory.decodeStream(newurl.openConnection().getInputStream()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 10;
//            imgMsg.setImageBitmap(BitmapFactory.decodeFile(getItem(position).getMsg(),options));
            imgInfo.setText(getItem(position).getTime());

        }


        
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
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }



}


