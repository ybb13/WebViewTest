package com.s06.ybb.com.webviewtest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int SHOW_HTML= 1 ;
    private static final String LOG = "WebView";

    private WebView wb;
    private Button send;
    private TextView content;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
             switch (msg.what){
                 case SHOW_HTML:
                     String response = (String)msg.obj;
                     content.setText(response);
             }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:
                Log.d(LOG,"onclick");
                sendHttpRespone();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

      //  wb = (WebView)findViewById(R.id.wb);
        send = (Button)findViewById(R.id.send);
        content = (TextView)findViewById(R.id.content);
        send.setOnClickListener(this);

//        wb.getSettings().setJavaScriptEnabled(true);
//        wb.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });


    }

    public void sendHttpRespone(){
        Log.d(LOG,"sendHttpRespone");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG,"start");
                HttpURLConnection connection = null;
                try {
                    Log.d(LOG,"1");
                    URL url = new URL("http://www.baidu.com");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    Log.d(LOG,"2");
                    InputStream in = connection.getInputStream();
                    BufferedReader bin = new BufferedReader(new InputStreamReader(in));

                    String str;
                    StringBuffer buffer = new StringBuffer();
                    Log.d(LOG,"3");
                    while((str=bin.readLine())!=null){
                        buffer.append(str);
                    }
                    Log.d(LOG,"4");
                    bin.close();
                    Message message = new Message();
                    message.what=SHOW_HTML;
                    message.obj = buffer.toString();
                    Log.d(LOG,"5");
                    handler.sendMessage(message);
                    Log.d(LOG,"6");
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }

                }

            }
        }).start();

    }
}
