package com.example.nocomment.snapchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luna on 17/09/2016.
 */
public class util {

    public  static String getPostDataString(HashMap<String, String> params){
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
        String reponse="";
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

            writer.write(util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reponse = br.readLine();
                br.close();

            } else {
                reponse= "fail";
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
        return reponse;
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

                writer.write(util.getPostDataString(postDataParams));

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
        String reponse="";
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

            writer.write(util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reponse = br.readLine();
                br.close();

            } else {
                reponse= "fail";
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
        return reponse;
    }



    public static String sendFriendRequest(String id,String friendID){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String reponse="";
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
            postDataParams.put("Title", "Fridends request");
            postDataParams.put("ID", id);
            postDataParams.put("Type", "fridendshipRequest");
            postDataParams.put("User", friendID);
            postDataParams.put("Message", "Hi! "+id);
            writer.write(util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reponse = br.readLine();
                br.close();

            } else {
                reponse= "fail";
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
        return reponse;
    }
    public static String getUsers(String id){
        BufferedWriter writer=null;
        HttpURLConnection connection=null;
        String reponse="";
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ID",id);
            String paramater= util.getPostDataString(postDataParams);
            String str="http://130.56.252.250/snapchat/getUsers.php?"+paramater;
            URL url = new URL(str);

            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reponse = br.readLine();
                br.close();

            } else {
                reponse= "fail";
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
        return reponse;
    }
}
