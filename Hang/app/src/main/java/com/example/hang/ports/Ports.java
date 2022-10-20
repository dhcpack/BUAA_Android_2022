package com.example.hang.ports;

public class Ports {
    public static String api = "http://43.143.166.142:8002/api/";
    public static String signInUrl = api + "signin/";  // GET 登录
    public static String signUpUrl = api + "signup/";  // POST 注册
    public static String modifyDetailUrl = api + "modifydetail/";  // PUT 修改用户信息

    public static String checkUrl = api + "check/";  // POST 打卡
    public static String checkDetail = api + "checkdetail/";  // GET 打卡记录

    public static String getBooksUrl = api + "getbooks/";  // GET 得到本用户所有记忆本
    public static String addBookUrl = api + "addbook/";  // POST 新建记忆本
    public static String modifyBookUrl = api + "modifybook/";  // PUT 修改记忆本
    public static String deleteBookUrl = api + "deletebook/";  // DELETE 删除记忆本
}
