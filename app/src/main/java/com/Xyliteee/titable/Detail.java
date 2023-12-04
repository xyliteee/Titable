package com.Xyliteee.titable;

import androidx.appcompat.app.AppCompatActivity;


import android.widget.*;
import android.content.*;
import android.os.Bundle;

public class Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Course currentCourse = (Course) intent.getSerializableExtra("currentCourse");
        Animators.CircleMove(findViewById(R.id.Circle1),1000,2000);
        Animators.CircleMove(findViewById(R.id.Circle2),1400,700);
        Animators.CircleMove(findViewById(R.id.Circle3),2000,1000);
        Animators.BackGroundMove(findViewById(R.id.Background));
        if(currentCourse != null)
        {
            SetDetail(currentCourse);
        }
    }


    private void SetDetail(Course currentCourse)
    {
        TextView nameText = findViewById(R.id.Name);
        TextView classRoomText = findViewById(R.id.ClassRoom);
        TextView teacherText = findViewById(R.id.Teacher);
        TextView scoreText = findViewById(R.id.Score);
        TextView weeksText = findViewById(R.id.Weeks);
        nameText.setText(currentCourse.Name);
        String a = "上课教室："+currentCourse.classRoom;
        String b = "任课教师："+currentCourse.Teacher;
        String c = "课程学分："+currentCourse.courseScore+"(.5)(.25)";                                //爬虫获取的学分是抹掉小数点的整数，绷不住
        String d = "课程周数："+currentCourse.weekSlot;
        classRoomText.setText(a);
        teacherText.setText(b);
        scoreText.setText(c);
        weeksText.setText(d);
    }
}