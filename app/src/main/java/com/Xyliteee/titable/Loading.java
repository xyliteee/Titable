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
            TimeTableSearchInfo timeTableSearchInfo = TimeTableSearchInfo.getInstance(null,null);
            String loginCookie = timeTableSearchInfo.loginCookie;                                   //获取登录cookie，用于获取课表数据
            String userIDCookie = timeTableSearchInfo.UserIDCookie;                                 //获取userID的cookie，用于获取学号
            Handler handler = new Handler(Looper.getMainLooper());
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {                                                                //执行新线程
                //String UserID = threadFunction.GetUserID(userIDCookie);                             //通过传入的cookie获取学号
                String UserID = "1145141919810";                                                    //现在不再需要学号，因此不进行网络访问，随便给一个即可,但考虑到未来，依旧保留这个变量
                handler.post(() -> {                                                                //获取完成后
                    executor.execute(() -> {                                                        //执行新线程
                        GetTableGroups(loginCookie,UserID);
                        handler.post(() -> {                                                        //获取完成后
                            GetTimeTableData(tableGroup.get(GetCurrentTerm()));
                        });                                                                         //这段太抽象了，哪天我改改
                    });
                });
            });
        }
    }

    private void GetTableGroups(String loginCookie,String UserID){

        for(String term :StaticSource.termGroupString)
        {
            String singleTable = threadFunction.GetTimeTable(loginCookie,UserID,term);
            if(UserID.equals("UserIDNetWorkError"))
            {
                Intent intent1 = new Intent(Loading.this, SomethingWrong.class);          //出现错误，去往错误界面
                intent1.putExtra("ErrorString","学号网络错误"+"\n"+"可能是修改了接口");
                startActivity(intent1);
                break;
            }
            else if (UserID.equals("UserIDGetError"))
            {
                Intent intent1 = new Intent(Loading.this, SomethingWrong.class);          //出现错误，去往错误界面
                intent1.putExtra("ErrorString","学号解析错误"+"\n"+"可能是修改了规则或者接口");
                startActivity(intent1);
                break;
            }
            else if (UserID.equals("TimeTableNetWorkError"))
            {
                Intent intent1 = new Intent(Loading.this, SomethingWrong.class);          //出现错误，去往错误界面
                intent1.putExtra("ErrorString","课表网络错误"+"\n"+"可能是修改了接口");
                startActivity(intent1);
                break;
            }
            tableGroup.put(term,singleTable);
        }
    }

    private void GetTimeTableData(String res)                                                       //将单个课表字符串转为Course组成的数组
    {
        try
        {
            JSONObject jsonObject = new JSONObject(res).getJSONObject("datas").getJSONObject("cxxskb");//解析json中的数据
            int courseTime = jsonObject.getInt("totalSize");                                  //解析数据获取课程数目
            Course[] coursesGroup = new Course[courseTime];                                         //建立一个数组存储课程，数组长度为课程数目
            JSONArray rows = jsonObject.getJSONArray("rows");                                 //解析数据获取具体课程数据
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
            intent1.putExtra("ErrorString","课表解析错误"+"\n"+"可能是修改了接口");
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
        return "2023-2024-2";
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