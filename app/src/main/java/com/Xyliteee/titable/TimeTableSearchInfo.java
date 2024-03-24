package com.Xyliteee.titable;

public class TimeTableSearchInfo {                                                                  //创建唯一实例化，用于存储Cookie，实在不想用intent了
    private static TimeTableSearchInfo instance;
    public String loginCookie;

    private TimeTableSearchInfo(String loginCookie) {
        this.loginCookie = loginCookie;
    }
    public static TimeTableSearchInfo getInstance(String loginCookie) {
        if (instance == null) {
            instance = new TimeTableSearchInfo(loginCookie);
        }
        return instance;
    }
}
