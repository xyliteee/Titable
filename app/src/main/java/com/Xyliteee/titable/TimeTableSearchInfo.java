package com.Xyliteee.titable;

public class TimeTableSearchInfo {                                                                  //创建唯一实例化，用于存储Cookie，实在不想用intent了
    private static TimeTableSearchInfo instance;
    public String loginCookie;
    public String UserIDCookie;

    private TimeTableSearchInfo(String loginCookie, String UserIDCookie) {
        this.loginCookie = loginCookie;
        this.UserIDCookie = UserIDCookie;
    }
    public static TimeTableSearchInfo getInstance(String loginCookie, String UserIDCookie) {
        if (instance == null) {
            instance = new TimeTableSearchInfo(loginCookie, UserIDCookie);
        }
        return instance;
    }
}
