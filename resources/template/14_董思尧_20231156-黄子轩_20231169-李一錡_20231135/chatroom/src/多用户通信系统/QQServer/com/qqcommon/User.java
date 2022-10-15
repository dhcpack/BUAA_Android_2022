package 多用户通信系统.QQServer.com.qqcommon;

import java.io.Serializable;

//序列化 将java对象转化为字节数组 便于储存或者传输
//应该是这个可序列化接口中有方法能够返回字节数组化的对象
public class User implements Serializable//可序列化
{
    //设置序列id
    private  static final long serialVersionUID=1L;
    private  String userId;
    private String passwd;
    public User() {}
    public User(String userId,String passwd)
    {
        this.passwd=passwd;
        this.userId=userId;
    }

    public int regtag=0;
//getter setter生成 右键generate
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
