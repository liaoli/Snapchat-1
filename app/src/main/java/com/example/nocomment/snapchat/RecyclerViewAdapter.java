package com.example.nocomment.snapchat;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by guomingsun on 11/10/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<WebItem> webItems;
    private Context context;
    private int MESSAGE_RETRIEVED = 0;
    private String tempWebTitle;
    private String tempUrl;

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
            case 3:
                Picasso.with(context)
                        .load(webItems.get(position).getWebUrl())
                        .transform(new ColorLayerImmutable(0x0066FF))
                        .into(holder.imgItem);
                break;
            case 4:
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

                tempWebTitle = webItems.get(position).getWebTitle();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String myId = getLoggedInUserId();
                        String discovery = Util.requestSubscription(myId,
                                webItems.get(position).getWebTitle());

                        Message isSubscribedMsg = new Message();
                        isSubscribedMsg.what = MESSAGE_RETRIEVED;
                        isSubscribedMsg.obj = discovery;
                        handler.sendMessage(isSubscribedMsg);

                    }
                }).start();

                return false;
            }
        });

        /*
         *  open up the webview when user click the image
         */
        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempUrl = webItems.get(position).getWebViewUrl();

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        Util.addClickCount(tempUrl);
                    }
                }).start();

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

    private String getLoggedInUserId () {
        String loggedInUser = "";

        if (Login.getLoggedinUserId() == "") {
            FileInputStream fis = null;

            try {
                fis = context.openFileInput("user");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                loggedInUser = bufferedReader.readLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loggedInUser = Login.getLoggedinUserId();
        }


        return loggedInUser;

    }


    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                if(message.obj.toString().trim().equals("true")){
                    FragmentManager fm = ((Activity) context).getFragmentManager();
                    SubscribeDialog subscribeDialog = new SubscribeDialog(context, tempWebTitle);
                    subscribeDialog.show(fm, "Subscription");
                } else {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "This has been subscribed", duration);
                    toast.show();
                }
            }
            return false;
        }
    });
}