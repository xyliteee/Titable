package com.Xyliteee.titable;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.client5.http.cookie.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.*;

import java.util.HashMap;
import java.util.List;



public class ThreadFunction
{
    CookieStore cookieStore = new BasicCookieStore();
    private final CloseableHttpClient httpClient = HttpClients.custom ().setDefaultCookieStore(cookieStore).build();
    public String GetTimeTable(String cookie,String UserID,String Date){
        String url = "https://ehall.ysu.edu.cn/jwapp/sys/syxkjg/modules/wdkb/cxxskb.do";
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        httpPost.addHeader("cookie",cookie);
        List<NameValuePair> params = new ArrayList<>();
        String timeTableData = "null";
        params.add(new BasicNameValuePair("XNXQDM", Date));
        params.add(new BasicNameValuePair("XH", UserID));
        params.add(new BasicNameValuePair("KBLB", "0"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        try
        {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            timeTableData = EntityUtils.toString(entity);
        }catch (Exception e)
        {
            timeTableData = "TimeTableNetWorkError";
        }
        return timeTableData;
    }

    public String GetUserID(String cookie){                                                         //学校接口更新，不再需要学号，但为了各方面考虑，目前依旧保留方法
        String UserID = "";
        String result = "";
        String url = "https://ehall.ysu.edu.cn/getLoginUser";
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        httpGet.addHeader("cookie",cookie);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            UserID = "UserIDNetWorkError";
            return UserID;
        }
        try{
            UserID = new JSONObject(result).getJSONObject("data").getString("userAccount");
            System.out.println(UserID);
        }catch (Exception e){
            UserID = "UserIDGetError";
        }
        return UserID;
    }

    public static HashMap<String,String> LoadTheTableGroup(SharedPreferences sharedPreferences)
    {
        String jsonString = sharedPreferences.getString("TableGroup","None");
        if (!jsonString.equals("None"))
        {
            Gson gson = new Gson();
            HashMap<String, String> tableGroup = gson.fromJson(jsonString, HashMap.class);
            return tableGroup;
        }
        return null;
    }
}
