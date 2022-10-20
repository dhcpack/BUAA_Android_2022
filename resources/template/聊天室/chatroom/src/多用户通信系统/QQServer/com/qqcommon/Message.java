package 多用户通信系统.QQServer.com.qqcommon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Message implements Serializable
{
    //表示客户端和服务器端通信时的消息对象
    private  static final long serialVersionUID=1L;
    private String sender;//发送者
    private String getter;//接收者
    private String content;//消息内容
    private String sendTime;//发送时间
    private String mesType;//消息类型->在接口中
    //消息的扩展 和文件相关的成员
    private byte[] fileBytes;
    private int fileLen=0;
    private String dest;//目标路径
    private String src;//源文件路径

    //离线消息包
    private ArrayList<String>offLineMes;

    //群聊列表
    private ArrayList<String>groupList;

    //群聊
    private String groupName;

    //已注册好友列表
    private ArrayList<String> accountList;

    //自己所在的全部列表
    private ArrayList<String>myGroupList;


//getter and setter

    public ArrayList<String> getAccountList() {
        return accountList;
    }

    public void setAccountList(ArrayList<String> accountList) {
        this.accountList = accountList;
    }

    public ArrayList<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<String> groupList) {
        this.groupList = groupList;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<String> getOffLineMes() {
        return offLineMes;
    }

    public void setOffLineMes(ArrayList<String> offLineMes) {
        this.offLineMes = offLineMes;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public ArrayList<String> getMyGroupList() {
        return myGroupList;
    }

    public void setMyGroupList(ArrayList<String> myGroupList) {
        this.myGroupList = myGroupList;
    }
}
