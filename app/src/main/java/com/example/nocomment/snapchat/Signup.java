package com.example.nocomment.snapchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.net.*;
import java.io.*;
import java.util.*;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
public class Signup extends AppCompatActivity {

    EditText userName;
    EditText email;
    EditText pwd;
    EditText compwd;
    Button singup;

    private final int MESSAGE_RETRIEVED = 0;
    android.os.Handler handler = new android.os.Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            if (message.what==MESSAGE_RETRIEVED){
                // update UI
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message.obj.toString(), duration);
                if(message.obj.toString().contains("Create user success!")){

                    Intent intent =new Intent(Signup.this,CameraScreen.class);
                    startActivity(intent);
                }
                toast.show();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userName = (EditText) findViewById(R.id.userName);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        compwd = (EditText) findViewById(R.id.compwd);
        singup = (Button) findViewById(R.id.sign_up);

        singup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = "";
                        try {

                            if (!pwd.getText().toString().equals(compwd.getText().toString())) {

                                response = "Password is not consistent";
                            } else {

                                URL url = new URL("http://130.56.252.250/snapchat/index.php");
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
                                postDataParams.put("ID", userName.getText().toString());
                                postDataParams.put("Email", email.getText().toString());
                                postDataParams.put("Password", pwd.getText().toString());

                                writer.write(getPostDataString(postDataParams));

                                writer.flush();
                                writer.close();

                                int responseCode = connection.getResponseCode();
                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    String line;
                                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                    while ((line = br.readLine()) != null) {
                                        response += line;
                                    }
                                } else {
                                    response = "";
                                }
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
