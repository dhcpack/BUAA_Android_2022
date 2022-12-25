package com.example.hang.ports;

public class Ports {
    public static String api = "http://43.143.166.142:8003/api/";
    // POST和PUT详细格式见postman(表单是Json格式)
    // GET和DELETE的url在下面已经列出，可以在postman测试
    /*
     * 登录：GET
     * url + username + password
     * isArray: false
     * */
    public static String signInUrl = api + "signin/";
    /*
     * 注册：POST
     * */
    public static String signUpUrl = api + "signup/";
    /*
     * 通过用户名得到用户的详细信息
     * url + nickname
     * */
    public static String userDetailUrl = api + "userdetail/";
    /*
     * 修改用户信息：PUT
     * */
    public static String modifyDetailUrl = api + "modifydetail/";  // PUT 修改用户信息
    /*
     * 打卡：POST
     * */
    public static String checkUrl = api + "check/";
    /*
     * 打卡记录：GET
     * url + nickname
     * isArray: false
     * */
    public static String checkDetail = api + "checkdetail/";
    /*
     * 创建记忆本：POST
     * */
    public static String addBookUrl = api + "addbook/";
    /*
     * 得到本用户的所有记忆本：GET
     * url + nickname
     * isArray: true
     * */
    public static String getBooksUrl = api + "getbooks/";
    /*
     * 修改记忆本：PUT
     * */
    public static String modifyBookUrl = api + "modifybook/";
    /*
     * 删除记忆本：DELETE
     * url + nickname + bookname
     * */
    public static String deleteBookUrl = api + "deletebook/";
    /*
     * 创建题目：POST
     * */
    public static String addQuestionUrl = api + "addques/";
    /*
     * 修改题目：PUT
     * */
    public static String modifyQuestionUrl = api + "modifyques/";
    /*
     * 得到全部题目：GET
     * url + bookid
     * isArray: true
     * */
    public static String getQuestionUrl = api + "getques/";
    /*
     * 删除题目：DELETE
     * url + nickname + bookid + quesid
     * */
    public static String deleteQuestionUrl = api + "deleteques/";
    /*
     * 发送好友申请：POST
     * */
    public static String postRequestUrl = api + "postrequest/";
    /*
     * 查找申请添加自己为好友的列表：GET
     * url + nickname + 'receive'
     * isArray: true
     * */
    public static String requestMeUrl = api + "getrequests/";
    /*
     * 查找自己申请添加别人好友的列表：GET
     * url + nickname + 'send'
     * isArray: true
     * */
    public static String requestOtherUrl = api + "getrequests/";
    /*
     * 接受好友申请：POST
     * */
    public static String acceptRequestUrl = api + "acceptrequest/";
    /*
     * 查询好友列表：GET
     * url + nickname
     * isArray: true
     * */
    public static String getFriendsUrl = api + "getfriends/";
    /*
     * 删除好友：DELETE
     * url + nickname1 + nickname2  1删除了2
     * */
    public static String deleteFriendUrl = api + "deletefriend/";
    /*
     * 发消息：POST
     * */
    public static String chatUrl = api + "chat/";
    /*
     * 得到聊天记录：GET
     * url + receiver + sender
     * isArray: true
     * */
    public static String getChatRecordUrl = api + "getchatrecord/";
    /*
     * 发帖：POST
     * */
    public static String postUrl = api + "post/";
    /*
     * 得到所有帖子：GET
     * url
     * isArray: true
     * */
    public static String getPostUrl = api + "post/";
    /*
     * 修改帖子：PUT
     * url + postid
     * */
    public static String modifyPostUrl = api + "post/";
    /*
     * 删除帖子：DELETE
     * url + postid
     * */
    public static String deletePostUrl = api + "post/";
    /*
     * 发评论：POST
     * */
    public static String addCommentUrl = api + "comment/";
    /*
     * 删除评论：DELETE
     * url + commentid
     * */
    public static String deleteCommentUrl = api + "comment/";
    /*
     * 查找指定用户：GET
     * url + (nickname | institute | major | grade | sex | all) + cond(筛选条件)
     * isArray: true
     * */
    public static String searchUserUrl = api + "searchuser/";
    /*
     * 查找指定的、公开的记忆本：GET
     * url + (tag | all) + cond(筛选条件)(all的时候筛选条件瞎写一个就行，不能不写)
     * isArray: true
     * */
    public static String searchBookUrl = api + "searchbook/";
    /*
     * 查找指定帖子：GET
     * url + (tag | all) + cond(筛选条件)(all的时候筛选条件瞎写一个就行，不能不写)
     * isArray: true
     * */
    public static String searchPostUrl = api + "searchpost/";
    /*
     * 查找文章的评论：GET
     * url + postid
     * isArray: true
     * */
    public static String searchCommentUrl = api + "searchcomment/";
    /*
     * 导入公开记忆本：GET
     * url + nickname + bookid
     * isArray: false
     * */
    public static String importBookUrl = api + "importbook/";
    /*
     * 导入公开记忆本：GET
     * url + imagePath
     * isArray: false
     * */
    public static String getPicUrl = api + "getimage/";
    /*
     * 上传图片：POST
     * */
    public static String postPicUrl = api + "postimage/";
}
