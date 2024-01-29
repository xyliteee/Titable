package com.Xyliteee.titable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.*;
import android.content.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import java.util.Locale;
import java.util.Random;





public class TimeTable extends AppCompatActivity {
    private Course[] coursesGroup;
    private int currentWeek;
    private TextView currentWeekText;

    private final Button[][] allButtons  = new Button[7][6];
    private final Button[] WeekButtons = new Button[7];
    @Override
    protected void onResume() {
        super.onResume();
        Animators.ControlBoxFloat(findViewById(R.id.ControlBox));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Intent intent = getIntent();
        coursesGroup =  (Course[]) intent.getSerializableExtra("CoursesGroup");
        currentWeekText = findViewById(R.id.CurrentWeekText);
        GetCurrentWeek();                                                                           //获取当前周数
        ButtonInit();                                                                               //所有按钮的初始化
        CheckWeekChangeButton();                                                                    //检查切换周按钮是否可用
        SetTableButton(currentWeek);                                                                //更新按钮以设置课表
        SetTableDay();                                                                              //设置今天
        currentWeekText.setText(String.valueOf(currentWeek));
    }

    private void GetCurrentWeek()
    {
        LocalDate today = LocalDate.now();
        long weeks = ChronoUnit.WEEKS.between(StaticSource.firstMonday, today);
        currentWeek = (int)(weeks+1);
        if(currentWeek >= 20)                                                                       //防止时间过久导致的周数超出或者开课前发布课表导致的周数为负数
        {
            currentWeek = 20;
        }
        else if(currentWeek <= 0)
        {
            currentWeek = 1;
        }
    }


    public void ChangeWeek(View view){

        Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        VibrationEffect effect = VibrationEffect.createWaveform(new long[]{0, 50}, -1);
        vibrator.vibrate(effect);

        Animators.ButtonPress((Button)view);
        if (view.getId() == R.id.lastWeek){
            currentWeek --;
        }
        else{
            currentWeek ++;
        }
        currentWeekText.setText(String.valueOf(currentWeek));
        CheckWeekChangeButton();
        ButtonInit();
        SetTableButton(currentWeek);
        SetTableDay();
    }

    private void CheckWeekChangeButton(){
        Button lastWeekButton = findViewById(R.id.lastWeek);
        Button nextWeekButton = findViewById(R.id.nextWeek);
        lastWeekButton.setEnabled(true);
        nextWeekButton.setEnabled(true);
        if (currentWeek <= 1){
            lastWeekButton.setEnabled(false);
        }
        else if(currentWeek >= 20){
            nextWeekButton.setEnabled(false);
        }
    }
    private void SetTableButton(int currentWeek)
    {
        if(coursesGroup == null) return;                                                            //如果没有课程，没必要往下了
        for (Course eachCourse:coursesGroup)                                                        //遍历每个课程
        {
            String t = eachCourse.Name.replace("（","\n（")+"\n"+eachCourse.classRoom;                                   //设定按钮显示的基本信息
            Button b = allButtons[eachCourse.targetWeek - 1][eachCourse.targetButton];              //确定头两节课对应的按钮
            switch (eachCourse.spendTime) {
                case 2:                                                                             //对于两节课的课程
                    if (b.getAlpha()==1f) break;                                                    //如果这个按钮完全不透明，说明是当前周存在的课程，不需要更改
                    b.setText(t);                                                                   //设置文字，设置不透明度，启用，携带课程数据，连接到函数
                    b.setAlpha(1f);
                    b.setEnabled(true);
                    b.setTag(eachCourse);
                    b.setOnClickListener(this::GetDetail);
                    if(isNotInCurrentWeek(eachCourse.weekSlot, currentWeek))                        //如果这个课不在当前周，不透明度设置为20%
                    {
                        b.setAlpha(0.2f);
                    }
                    break;
                case 4:
                    Button bb =  allButtons[eachCourse.targetWeek - 1][eachCourse.targetButton+1];  //获取后两节课程按钮
                    if(b.getAlpha()==1f || bb.getAlpha()==1f) break;
                    b.setText(t);
                    b.setAlpha(1f);
                    b.setEnabled(true);
                    b.setTag(eachCourse);
                    b.setOnClickListener(this::GetDetail);
                    bb.setText(t);
                    bb.setAlpha(1f);
                    bb.setEnabled(true);
                    bb.setTag(eachCourse);
                    bb.setOnClickListener(this::GetDetail);
                    if(isNotInCurrentWeek(eachCourse.weekSlot, currentWeek))
                    {
                        bb.setAlpha(0.2f);
                        b.setAlpha(0.2f);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static boolean isNotInCurrentWeek(String str, int num) {
        String[] periods = str.split(",");                                                    //以，分割周数的字符串
        for (String period : periods) {
            if (period.contains("-")) {                                                             //检查是否存在-号，存在表示是11-15周这种表现形式
                String[] bounds = period.split("-");                                          //以-分割
                int lowerBound = Integer.parseInt(bounds[0].replaceAll("[^0-9]", ""));//获取第一个数字
                int upperBound = Integer.parseInt(bounds[1].replaceAll("[^0-9]", ""));//获取第二个数字
                if (num >= lowerBound && num <= upperBound) {                                       //检查当前周数是否位于这个区间，如果在
                    return false;
                }
            } else {                                                                                //没有-号，也就是16周这种表现形式
                int week = Integer.parseInt(period.replaceAll("[^0-9]", ""));      //获取数字
                if (num == week) {                                                                  //直接检查是否相等，相等表示就是当前周
                    return false;
                }                                                                                   //位于区间返回false
            }
        }
        return true;
    }
    private void GetDetail(View view)                                                               //获取详细信息并转到详情页面
    {
        Course currentCourse = (Course) view.getTag();                                              //获取当前按钮表示的课程
        Intent i = new Intent(TimeTable.this, Detail.class);
        i.putExtra("currentCourse",currentCourse);
        startActivity(i);
    }
    private void SetTableDay()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.SIMPLIFIED_CHINESE);
        String currentData = simpleDateFormat.format(new Date());
        if (currentData.contains("星期一")) {WeekButtons[0].setAlpha(1f);}                           //将对应天的标签不透明度设置为100%
        else if (currentData.contains("星期二")){WeekButtons[1].setAlpha(1f);}
        else if (currentData.contains("星期三")){WeekButtons[2].setAlpha(1f);}
        else if (currentData.contains("星期四")){WeekButtons[3].setAlpha(1f);}
        else if (currentData.contains("星期五")){WeekButtons[4].setAlpha(1f);}
        else if (currentData.contains("星期六")){WeekButtons[5].setAlpha(1f);}
        else {WeekButtons[6].setAlpha(1f);}
    }

    private void ButtonInit()
    {
        int[] buttonIds = new int[]
                {
                R.id.b112, R.id.b134, R.id.b156, R.id.b178, R.id.b1910, R.id.b11112,
                R.id.b212, R.id.b234, R.id.b256, R.id.b278, R.id.b2910, R.id.b21112,
                R.id.b312, R.id.b334, R.id.b356, R.id.b378, R.id.b3910, R.id.b31112,
                R.id.b412, R.id.b434, R.id.b456, R.id.b478, R.id.b4910, R.id.b41112,
                R.id.b512, R.id.b534, R.id.b556, R.id.b578, R.id.b5910, R.id.b51112,
                R.id.b612, R.id.b634, R.id.b656, R.id.b678, R.id.b6910, R.id.b61112,
                R.id.b712, R.id.b734, R.id.b756, R.id.b778, R.id.b7910, R.id.b71112                 //好多按钮啊啊啊啊啊啊啊啊
                };
        int[] WeekLabel = new int[]
                {
                R.id.MondayLabel,R.id.TuesdayLabel,
                R.id.WednesdayLabel,R.id.ThursdayLabel,
                R.id.FridayLabel,R.id.SaturdayLabel,R.id.SundayLabel
                };

        String[] emotionGroup = new String[10];
        emotionGroup[0] = "(￣▽￣)";
        emotionGroup[1] = "(*￣︶￣)";
        emotionGroup[2] = "φ(゜▽゜*)";
        emotionGroup[3] = "(*>﹏<*)";
        emotionGroup[4] = "( ＾皿＾)っ";
        emotionGroup[5] = "( $ _ $ )";
        emotionGroup[6] = "(/≧▽≦)/";
        emotionGroup[7] = "(☆▽☆)";
        emotionGroup[8] = "(o゜▽゜)o";
        emotionGroup[9] = "⊙﹏⊙|||";

        Random r=new Random();
        for (int i = 0; i < 7; i++) {
            WeekButtons[i] = findViewById(WeekLabel[i]);
            WeekButtons[i].setAlpha(0.3f);                                                          //将所有天设置为30%不透明度
            WeekButtons[i].setOnClickListener(this::WeekMessage);                                   //连接到WeekMessage函数
            for (int j = 0; j < 6; j++) {
                allButtons[i][j] = findViewById(buttonIds[i * 6 + j]);
                Button b = allButtons[i][j];
                b.setAlpha(0.1f);
                b.setTextSize(8f);
                b.setTextColor(0xFFFFFFFF);
                int num=r.nextInt(10);
                b.setText(emotionGroup[num]);
                b.setTag(null);                                                                     //所表示的课程置空，其实也可以不管，因为就算不为空按钮也无法点击不会有影响，但为了完善以及省出一点内存
                b.setEnabled(false);                                                                //关闭按钮，不可点击
            }
        }

    }
    public void WeekMessage(View view)
    {
        Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        VibrationEffect effect = VibrationEffect.createWaveform(new long[]{0, 50}, -1);
        vibrator.vibrate(effect);
        Animators.ButtonPress((Button) view);
        if(view.getId() == R.id.MondayLabel){
            Toast.makeText(TimeTable.this, "Monday left me broken", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.TuesdayLabel){
            Toast.makeText(TimeTable.this, "Tuesday I was through with hoping", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.WednesdayLabel){
            Toast.makeText(TimeTable.this, "Wednesday my empty arms were open", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.ThursdayLabel){
            Toast.makeText(TimeTable.this, "Thursday waiting for love, waiting for vMe50", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.FridayLabel){
            Toast.makeText(TimeTable.this, "Thank the stars it's Friday", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.SaturdayLabel){
            Toast.makeText(TimeTable.this, "I'm burning like a fire gone wild on Saturday", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(TimeTable.this, "Guess I won't be coming to church on Sunday", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        Animators.ControlBoxDown(findViewById(R.id.ControlBox));
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Animators.ControlBoxDown(findViewById(R.id.ControlBox));
    }
}