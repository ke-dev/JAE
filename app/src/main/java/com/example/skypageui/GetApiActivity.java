package com.example.skypageui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetApiActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvshow;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private int GETDATA_SUCCESS = 101;//获取数据成功的标志

    //不同进程之间的通信
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == GETDATA_SUCCESS) {
                String data = message.getData().getString("data");
                Log.i("MainActivity", data);
                mTvshow.setText(data);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_main);

        //初始化控件
        initUI();

        //初始化数据
        initData();
    }

    private void initUI() {
        //获取文本框
        mTvshow = findViewById(R.id.tv_show);
        //获取按钮并且设置监听者
        findViewById(R.id.btn_refresh).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        initData();
    }

    //初始化数据,请求网络
    private void initData() {
        //请求网络只能在子线程创建
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = getDataFromServer();

                //创建信息对象
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("data", data);
                message.setData(bundle);
                message.what = GETDATA_SUCCESS;
                //向主线程发送信息
                mHandler.sendMessage(message);
            }
        }).start();
    }

    //从服务器获取数据
    private String getDataFromServer() {

        try {
            //1.创建RUL
            URL url = new URL("https://v1.hitokoto.cn/?c=f&encode=text");

            //2.打开链接
            connection = (HttpURLConnection) url.openConnection();

            //3.判断并处理,响应状态码
            if (connection.getResponseCode() == 200) {

                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                for (String line = ""; (line = bufferedReader.readLine()) != null; ) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //先开后关
                if (bufferedReader != null) bufferedReader.close();
                if (inputStream != null) inputStream.close();
                if (connection != null) connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "";

    }
}
