package com.example.nocomment.snapchat;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Luna on 17/09/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    //onTokenRefresh() get called whenever a new token is generated
    private String token;

    protected String getToken() {
        onTokenRefresh();
        return this.token;
    }
    @Override
    public void onTokenRefresh() {
        //get a token from Firebase cloud message
        String token = FirebaseInstanceId.getInstance().getToken();
        this.token=token;

    }
    // once you obtain a token from Firebase cloud message, you should remember to send this token to your app server database.
    // in this case, i send this token to my local server
    public void registerToken(String userID){


        URL url = null;
        try {

            url = new URL("http://130.56.252.250/snapchat/register.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            HashMap<String, String> postDataParams= new HashMap<>();
            postDataParams.put("ID", userID);
            postDataParams.put("Token", getToken());
            writer.write(Util.getPostDataString(postDataParams));
            writer.flush();
            writer.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.print(responseCode);
        } else {

        }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
