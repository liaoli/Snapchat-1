package com.example.nocomment.snapchat;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luna on 17/09/2016.
 */
public class Util {

    private Context context;

    public Util(Context context) {
        this.context = context;
    }

    public static String getPostDataString(HashMap<String, String> params){
        try {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                if(entry.getValue()==null){
                    result.append(URLEncoder.encode("", "UTF-8"));
                }
                else {result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));}

            }
            return result.toString();
        } catch (UnsupportedEncodingException e) {
            System.out.print(e.getMessage());
        }return  "";
    }

    public static String login(String user,String password){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            URL url = new URL("http://130.56.252.250/snapchat/login.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ID", user);
            postDataParams.put("Password", password);

            writer.write(Util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String signUP(String user,String email,String password){
        String response = "";
        try {

            URL url = new URL("http://130.56.252.250/snapchat/signup.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ID", user);
            postDataParams.put("Email",email);
            postDataParams.put("Password", password);

            writer.write(Util.getPostDataString(postDataParams));

            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response =br.readLine();
                br.close();

            } else {
                response = "fail";
            }
            connection.disconnect();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String acceptFriend(String id,String friendID){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            URL url = new URL("http://130.56.252.250/snapchat/acceptRequest.php");


            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ID", id);
            postDataParams.put("friendID", friendID);

            writer.write(Util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }



    public static String sendFriendRequest(String id,String friendID){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            URL url = new URL("http://130.56.252.250/snapchat/notification.php");


            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("Body",friendID+" is sending a friend request");
            postDataParams.put("Title", "Friends request");
            postDataParams.put("ID", id);
            postDataParams.put("Type", "friendshipRequest");
            postDataParams.put("User", friendID);
            postDataParams.put("Message", "Hi! "+id);
            writer.write(Util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static String getUsers(String id){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ID",id);
            String paramater= Util.getPostDataString(postDataParams);
            String str="http://130.56.252.250/snapchat/getUsers.php?"+paramater;
            URL url = new URL(str);

            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    public static String getFriends(String id){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ID",id);
            String paramater= Util.getPostDataString(postDataParams);
            String str="http://130.56.252.250/snapchat/getFriends.php?"+paramater;
            URL url = new URL(str);

            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String postImage(String id, String bitmap, boolean isStory){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        String isPostStory=isStory?"true":"false";

        try {
            URL url = new URL("http://130.56.252.250/snapchat/postImage.php");

            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("imageCode", bitmap);
            postDataParams.put("user", id);
            postDataParams.put("isPostStory", isPostStory);

            writer.write(Util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }



    public static String sendNotification(String id, ArrayList friendID, String message, int category){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            URL url = new URL("http://130.56.252.250/snapchat/notification.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            HashMap<String, String> postDataParams = new HashMap<>();
            JSONArray mJSONArray = new JSONArray(friendID);
            switch (category){
                case 0:
                    postDataParams.put("Body","Friends request");
                    postDataParams.put("Title", "Friends request");
                    postDataParams.put("MyID", id);
                    postDataParams.put("Type", "friendshipRequest");
                    postDataParams.put("Friends", mJSONArray.toString());
                    postDataParams.put("Message", message);
                    break;
                case 1:
                    postDataParams.put("Body","Send Message");
                    postDataParams.put("Title", "Send Message");
                    postDataParams.put("MyID", id);
                    postDataParams.put("Type", "sendMessage");
                    postDataParams.put("Friends", mJSONArray.toString());
                    postDataParams.put("Message", message);
                    break;
                case 2:
                    postDataParams.put("Body","Send Image");
                    postDataParams.put("Title", "Send Image");
                    postDataParams.put("MyID", id);
                    postDataParams.put("Type", "sendImage");
                    postDataParams.put("Friends", mJSONArray.toString());
                    postDataParams.put("Message", message);
                    break;
                case 3:
                    postDataParams.put("Body","Send Story");
                    postDataParams.put("Title", "Send Story");
                    postDataParams.put("MyID", id);
                    postDataParams.put("Type", "sendStory");
                    postDataParams.put("Friends", mJSONArray.toString());
                    break;
            }
            writer.write(Util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static String postSubscribe(String id,String topicName){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            URL url = new URL("http://130.56.252.250/snapchat/subscribe.php");


            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ID",id);
            postDataParams.put("topicName", topicName);

            writer.write(Util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*{"discovery":[{"tagName":"CNN","item":[{"iconUrl":"https:\/\/pbs.twimg.com\/media\/CuAK2a8WAAQNlj5.jpg","url":"http:\/\/people.com\/celebrity\/sean-penn-leila-george-event-girlfriend\/"},{"iconUrl":"https:\/\/pbs.twimg.com\/media\/CuAK2a8WAAQNlj5.jpg","url":"http:\/\/people.com\/tv\/ashton-kutcher-mad-laura-prepon-kept-ben-foster-engagement-secret\/"}]},{"tagName":"People","item":[{"iconUrl":"https:\/\/assets01.magshop.com\/au\/assets\/product\/0006015_australian-womens-weekly-food-single-issues_220.jpeg","url":"http:\/\/www.heraldsun.com.au\/lifestyle\/food\/tastecomau-seasons\/dill-salmon-tikka\/news-story\/112db7ffd0a08c066fdf3a4672edccec"},{"iconUrl":"https:\/\/assets01.magshop.com\/au\/assets\/product\/0006015_australian-womens-weekly-food-single-issues_220.jpeg","url":"http:\/\/www.heraldsun.com.au\/lifestyle\/food\/tastecomau-seasons\/dill-salmon-tikka\/news-story\/112db7ffd0a08c066fdf3a4672edccec"}]},{"tagName":"MTV","item":[]}]}
   */
    public static String getDiscovery(){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {

            String str="http://130.56.252.250/snapchat/getDiscovery.php";
            URL url = new URL(str);

            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    public static String getSubscribeDiscovery(String id){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ID",id);
            String paramater= Util.getPostDataString(postDataParams);
            String str="http://130.56.252.250/snapchat/subscribe.php?"+paramater;
            URL url = new URL(str);

            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*{"stories":[{"user":"b","imageUrl":"http:\/\/130.56.252.250\/snapchat\/image\/57f8710a8cb54.png","time":"2016-10-08 04:07:38"},{"user":"b","imageUrl":"http:\/\/130.56.252.250\/snapchat\/image\/57f8713bc0f72.png","time":"2016-10-08 04:08:27"},{"user":"b","imageUrl":"http:\/\/130.56.252.250\/snapchat\/image\/57f8714838f3e.png","time":"2016-10-08 04:08:40"},{"user":"b","imageUrl":"http:\/\/130.56.252.250\/snapchat\/image\/57f8716f5ab87.png","time":"2016-10-08 04:09:19"},{"user":"b","imageUrl":"http:\/\/130.56.252.250\/snapchat\/image\/57f87324140a0.png","time":"2016-10-08 04:16:36"},{"user":"c","imageUrl":"http:\/\/130.56.252.250\/snapchat\/image\/57f8735f16c18.png","time":"2016-10-08 04:17:35"},{"user":"d","imageUrl":"http:\/\/130.56.252.250\/snapchat\/image\/57f8748e391ba.png","time":"2016-10-08 04:22:38"}]}  */
    public static String getStories(String id){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ID",id);
            String paramater= Util.getPostDataString(postDataParams);
            String str="http://130.56.252.250/snapchat/getFriendsStories.php?"+paramater;
            URL url = new URL(str);

            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static String findUsers(String id){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("userName",id);
            String paramater= Util.getPostDataString(postDataParams);
            String str="http://130.56.252.250/snapchat/getUsers.php?"+paramater;
            URL url = new URL(str);

            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }





    public static String findUserByPhone(String phone){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("phone",phone);

            String paramater= Util.getPostDataString(postDataParams);
            String str="http://130.56.252.250/snapchat/getUserByPhone.php?"+paramater;
            URL url = new URL(str);

            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //like 'a'
        return response;
    }





    public static String addClickCount(String urlClick){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String response="";
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("URL",urlClick);
            String paramater= Util.getPostDataString(postDataParams);
            String str="http://130.56.252.250/snapchat/getFriendsStories.php?"+paramater;
            URL url = new URL(str);

            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
                br.close();

            } else {
                response= "fail";
            }
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}