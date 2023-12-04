package com.Xyliteee.titable;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.view.View;
import android.widget.Button;
import android.content.*;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private WebView loginWebview;
    private String loginCookie;
    private Button button;
    private Intent intent;
    private long mExitTime = 0;
    private String cookieForGettingUserID;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("courseData", MODE_PRIVATE);
        boolean alreadyHaveTableGroup = sharedPreferences.getBoolean("alreadyHaveTableGroup",false);

        button = findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);

        if(alreadyHaveTableGroup)                                                                   //如果的确有课程表组了
        {
            intent = new Intent(MainActivity.this, Loading.class);
            intent.putExtra("Auto",true);
            startActivity(intent);                                                                  //跳转到加载活动，传输课表数据以及自动标志
        }
        else
        {                                                                                           //否则就调用webView进行网络操作
            loginWebview = findViewById(R.id.LoginWeb);
            LoginInit();
        }

    }
    public void Analysis(View view)                                                                 //连接到确认按钮的函数
    {
        intent = new Intent(MainActivity.this, Loading.class);
        TimeTableSearchInfo.getInstance(loginCookie,cookieForGettingUserID);                        //将登录cookie和获取学号的cookie实例化在TimeTableSearchInfo中
        startActivity(intent);                                                                      //跳转到加载页面
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void LoginInit(){
        button.setVisibility(View.INVISIBLE);                                                       //先设置按钮不可见防止误触
        loginWebview.setVisibility(View.VISIBLE);
        loginWebview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CookieManager cookieManager = CookieManager.getInstance();                          //创建一个cookie管理器
                if(url.contains("/wdkb"))                                                           //如果进入到课表界面
                {
                    System.out.println(url);
                    loginCookie = cookieManager.getCookie(url);                                     //储存loginCookie
                    button.setVisibility(View.VISIBLE);                                             //确定按钮可见
                    Toast.makeText(MainActivity.this, "检索到课表信息，点击确认定位", Toast.LENGTH_SHORT).show();//为了防止webView因为快速响应导致的崩溃，所以采取手动确认

                }
                else if(url.contains("/OfficeHall"))                                                //如果是在主页面
                {
                    cookieForGettingUserID = cookieManager.getCookie(url);                          //储存主页面cookie用于获取学号
                    //loginWebview.loadUrl("https://ehall.ysu.edu.cn/jwapp/sys/syxkjg/*default/index.do?t_s=1701606927710&EMAP_LANG=zh&THEME=millennium&amp_sec_version_=1&gid_=aGZ0a3BYcTUyWm5VZEJjK0dxVXEyYk9QbHcrVWpCVVl5bVJKQUgwakxrK3c1K1hXcThoTVB1L3prVEhlUlFNVUtSUjhvMGhZTlFBVkx2WnVNTi91Qnc9PQ#/wdkb");//跳转到课表界面,老是跳错，先关闭
                }
            }
        }
        );
        loginWebview.loadUrl("https://cer.ysu.edu.cn/authserver/login?service=https://ehall.ysu.edu.cn/login?service=https://ehall.ysu.edu.cn/ywtb-mobile/index.html#/OfficeHall");//进入登陆界面
        WebSettings settings = loginWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        boolean firstUse = sharedPreferences.getBoolean("firstUse",true);                     //获取第一次使用标志，如果没获取到赋值true，证明是第一次使用
        if(firstUse)
        {
            Toast.makeText(MainActivity.this, "登录后手动访问课表界面后，点击确认", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstUse",false);
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再次操作以返回桌面", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        }
        else{
            this.finish();
            System.exit(0);
        }
    }
}