package com.example.nocomment.snapchat;

import android.app.Activity;
import android.app.FragmentManager;
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
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by guomingsun on 11/10/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<WebItem> webItems;
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<WebItem> webItems) {
        this.context = context;
        this.webItems = webItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View webViewLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.web_item_view, null);
        ViewHolder viewHolder = new ViewHolder(webViewLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        int remainder = position % 5;

        /*
         *  Use different color filter on the given images
         */
        switch (remainder){
            case 0:
                Picasso.with(context)
                        .load(webItems.get(position).getWebUrl())
                        .transform(new ColorLayerImmutable(0x99FF99))
                        .into(holder.imgItem);
                break;
            case 1:
                Picasso.with(context)
                        .load(webItems.get(position).getWebUrl())
                        .transform(new ColorLayerImmutable(0x33FFFF))
                        .into(holder.imgItem);
                break;
            case 2:
                Picasso.with(context)
                        .load(webItems.get(position).getWebUrl())
                        .transform(new ColorLayerImmutable(0x9933FF))
                        .into(holder.imgItem);
                break;
            case 4:
                Picasso.with(context)
                        .load(webItems.get(position).getWebUrl())
                        .transform(new ColorLayerImmutable(0x0066FF))
                        .into(holder.imgItem);
                break;
            case 5:
                Picasso.with(context)
                        .load(webItems.get(position).getWebUrl())
                        .transform(new ColorLayerImmutable(0x3399FF))
                        .into(holder.imgItem);
                break;
        }

        /*
         * set title for each discover view
         */
        holder.txtItem.setText(webItems.get(position).getWebTitle());

        /*
         *  Open the dialog for subscription when user put their thumb on the image
         */
        holder.imgItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FragmentManager fm = ((Activity) context).getFragmentManager();
                SubscribeDialog subscribeDialog = new SubscribeDialog(context, webItems.get(position).getWebTitle());
                subscribeDialog.show(fm, "Subscription");
                return false;
            }
        });

        /*
         *  open up the webview when user click the image
         */
        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, WebViewWindows.class);
                intent.putExtra("url", webItems.get(position).getWebViewUrl());
                context.startActivity(intent);


            }
        });

    }

    /*
     *  find how many view are needed
     */
    @Override
    public int getItemCount() {
        return webItems.size();
    }

    /*
     * find view from the layout
     */
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