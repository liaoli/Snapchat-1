package com.example.nocomment.snapchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by guomingsun on 11/10/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private WebItem[] webItem;
    private Context context;

    public RecyclerViewAdapter(Context context, WebItem[] webItem) {
        this.context = context;
        this.webItem = webItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View webViewLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.web_item_view, null);
        ViewHolder viewHolder = new ViewHolder(webViewLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Picasso.with(context)
                .load(webItem[position].getWebUrl())

                .into(holder.imgItem);

        holder.txtItem.setText(webItem[position].getWebTitle());

        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webItem[position].getWebViewUrl()));
                context.startActivity(intent);
            }
        });

//        InputStream input = null;
//        try {
//            input = new java.net.URL(webItem[position].getWebUrl()).openStream();
//            holder.imgItem.setImageBitmap(BitmapFactory.decodeStream(input));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // Decode Bitmap


    }


    @Override
    public int getItemCount() {
        return webItem.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtItem;
        public ImageView imgItem;

        public ViewHolder(View itemView) {
            super(itemView);
            txtItem = (TextView) itemView.findViewById(R.id.textItem);
            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
        }
    }
}