package com.Xyliteee.titable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.content.*;


import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.Gson;

public class Loading extends AppCompatActivity {

    private HashMap<String, String> tableGroup;
    private final ThreadFunction threadFunction = new ThreadFunction();
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        sharedPreferences = this.getSharedPreferences("courseData",MODE_PRIVATE);
        Intent intent = getIntent();
        boolean Auto = intent.getBooleanExtra("Auto",false);
        if(Auto)
        {
            tableGroup = ThreadFunction.LoadTheTableGroup(sharedPreferences);
            GetTimeTableData(tableGroup.get(GetCurrentTerm()));
        }
        else
        {
            tableGroup = new HashMap<>();
            TimeTableSearchInfo timeTableSearchInfo = TimeTableSearchInfo.getInstance(null);
            String loginCookie = timeTableSearchInfo.loginCookie;                                   //获取登录cookie，用于获取课表数据
            Handler handler = new Handler(Looper.getMainLooper());
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {                                                                //执行新线程
                GetTableGroups(loginCookie);
                handler.post(() -> {
                    GetTimeTableData(tableGroup.get(GetCurrentTerm()));//获取完成后
                });
            });
        }
    }

    private void GetTableGroups(String loginCookie){
        for(String term :StaticSource.termGroupString)
        {
            String singleTable = threadFunction.GetTimeTable(loginCookie,term);
            System.out.println(singleTable);
            if (singleTable.equals("TimeTableNetWorkError")) {
                Intent intent1 = new Intent(Loading.this, SomethingWrong.class);      //出现错误，去往错误界面
                intent1.putExtra("ErrorString", "课表网络错误" + "\n" + "可能是修改了接口");
                startActivity(intent1);
                break;
            }
            try{
                int messageCode = new JSONObject(singleTable).getJSONObject("datas").getJSONObject("cxxskb").getJSONObject("extParams").getInt("code");
                if (messageCode == 1){tableGroup.put(term,singleTable);}                            //确认获取到的课表数据编码为1，也就是成功获取，才进行存储
            }catch (Exception e){
                Intent intent1 = new Intent(Loading.this, SomethingWrong.class);      //出现错误，去往错误界面
                intent1.putExtra("ErrorString","课表解析错误"+"\n"+"无法获得解析结果编码");
                startActivity(intent1);
                break;
            }

        }
    }

    private void GetTimeTableData(String res)                                                       //将单个课表字符串转为Course组成的数组
    {
        try
        {
            JSONObject jsonObject = new JSONObject(res).getJSONObject("datas").getJSONObject("cxxskb");//解析json中的数据
            JSONArray rows = jsonObject.getJSONArray("rows");                                 //解析数据获取具体课程数据
            int courseTime = rows.length();                                                         //解析数据获取课程数目
            Course[] coursesGroup = new Course[courseTime];                                         //建立一个数组存储课程，数组长度为课程数目
            for (int i = 0;i < rows.length();i++)                                                   //遍历每个课程数据
            {
                coursesGroup[i] = new Course(rows.getJSONObject(i));                                //实例化课程并放置在数组中，调用构造函数进行初始化
            }
            SaveTheTableGroup(tableGroup);                                                          //移动到这里，解析成功后才储存，不然错误率太高
            Intent intent1 = new Intent(Loading.this, TimeTable.class);
            intent1.putExtra("CoursesGroup",coursesGroup);                                    //传输这个数组
            startActivity(intent1);
        }
        catch (Exception e){
            Intent intent1 = new Intent(Loading.this, SomethingWrong.class);          //出现错误，去往错误界面
            intent1.putExtra("ErrorString","课表解析错误"+"\n"+"可能是修改了数据类型");
            startActivity(intent1);
        }
    }

    private String GetCurrentTerm()
    {
        LocalDate now = LocalDate.now();

        for (int i = 0; i < StaticSource.termStarts.length; i++) {
            if ((now.isAfter(StaticSource.termStarts[i]) || now.isEqual(StaticSource.termStarts[i])) && (now.isBefore(StaticSource.termEnds[i]) || now.isEqual(StaticSource.termEnds[i]))) {
                //return termGroup[i];                                                              //暂时不考虑学期的切换
            }
        }
        return StaticSource.currentTerm;
    }


    private void SaveTheTableGroup(HashMap<String,String> hashMap)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(hashMap);
        editor.putString("TableGroup", jsonString);
        editor.putBoolean("alreadyHaveTableGroup",true);
        editor.apply();
    }

    public void onBackPressed() {
    }

}