package com.Xyliteee.titable;
import org.json.JSONObject;
import java.io.Serializable;

public class Course implements Serializable {                                                       //课程类，且可被用于不同活动传输
    public String Name = null;                                                                      //课程名字
    public String weekSlot = null;                                                                  //在第几周内授课
    public int targetWeek = 0;                                                                      //在具体星期几上课
    public String classRoom = null;                                                                 //在什么教室上课
    public int startTime = 0;                                                                       //上课开始时间
    public int endTime = 0;                                                                         //结束时间
    public String Teacher = null;                                                                   //老师是谁
    public int courseScore = 0;                                                                     //学分多少
    public int spendTime = 0;                                                                       //这个课程今天花多久
    public int targetButton = 0;                                                                    //对应在什么按钮上
    public Course(JSONObject jsonData)                                                              //构造函数，传入一个json解析数据获取各类信息
    {
        try
        {
            this.Name = jsonData.getString("KCM");
            this.weekSlot = jsonData.getString("ZCMC");
            this.targetWeek = jsonData.getInt("SKXQ");
            this.classRoom = jsonData.getString("JASMC");
            this.startTime = jsonData.getInt("KSJC");
            this.endTime = jsonData.getInt("JSJC");
            this.Teacher = jsonData.getString("SKJS");
            if(this.Teacher.equals("null"))
            {
                this.Teacher = "未提供";
            }
            this.courseScore = jsonData.getInt("XF");
            this.spendTime = this.endTime - this.startTime+1;                                       //持续时间就是结束-开始+1
            this.targetButton = (int) Math.floor(startTime/2f);                                     //设置本课程第一节课对应的按钮

        }catch (Exception e){System.out.println("解析出错，也需要重新匹配规则");}

    }
}
