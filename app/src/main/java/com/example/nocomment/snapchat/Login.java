package com.example.nocomment.snapchat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import android.content.Intent;
public class Login extends AppCompatActivity {

    EditText userName;
    EditText pwd;
    Button login;

    private final int MESSAGE_RETRIEVED = 0;
    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message.obj.toString(), duration);


                toast.show();
                if(message.obj.toString().contains("login successfully")){

                    Intent intent =new Intent(Login.this,CameraScreen.class);
                    startActivity(intent);
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.button);
        userName = (EditText) findViewById(R.id.login_userName);
        pwd = (EditText) findViewById(R.id.login_password);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = "";
                        try {

                            HashMap<String, String> postDataParams = new HashMap<>();
                            postDataParams.put("ID", userName.getText().toString());
                            postDataParams.put("Password", pwd.getText().toString());
                            String urlstr=getPostDataString(postDataParams);



                            URL url = new URL("http://130.56.252.250/snapchat/index.php"+"?"+urlstr);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");


                            connection.connect();

                            int responseCode = connection.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                String line;
                                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                while ((line = br.readLine()) != null) {
                                    response += line;
                                }
                                br.close();
                            } else {
                                response = "fail";
                            }



                            Message message = new Message();
                            message.what = MESSAGE_RETRIEVED;
                            message.obj = response;
                            handler.sendMessage(message);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}