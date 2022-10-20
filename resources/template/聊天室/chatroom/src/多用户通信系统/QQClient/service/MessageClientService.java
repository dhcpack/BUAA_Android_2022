package 多用户通信系统.QQClient.service;


import 多用户通信系统.QQServer.com.qqcommon.Message;
import 多用户通信系统.QQServer.com.qqcommon.MessageType;
import 多用户通信系统.myJDBC.linkMySQL;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/*
* 提供和消息相关的服务方法 包括私聊 传文件等
* */
public class MessageClientService
{
    // 内容 发送者id 接收者id
    public void sendMessageToOne(String content,String senderId,String getterId) throws IOException {
        //构建Message
        Message message=new Message();
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setMesType(MessageType.MESSAGE_COMM_MES);//普通聊天消息
        LocalDateTime dateTime = LocalDateTime.now(); // get the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //System.out.println(dateTime.format(formatter));
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message.setSendTime(dateTime.format(formatter));//发送时间
        //message.setSendTime(new Date().toString());//发送时间
//        System.out.println(senderId+" 对 "+getterId+" 说 "+content);
        System.out.println("送信申请发送到服务器");
        //发送给服务端
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(senderId).getSocket().getOutputStream());
        oos.writeObject(message);

    }

    public void sendMessageToAll(String content,String senderId) throws IOException
    {
        //构建Message
        Message message=new Message();
        message.setSender(senderId);
        message.setContent(content);
        message.setMesType(MessageType.MESSAGE_TOALL_MES);//普通聊天消息
        message.setSendTime(new Date().toString());//发送时间
//        System.out.println(senderId+" 对 "+getterId+" 说 "+content);
        System.out.println("送信请求已发送");
        //发送给服务端
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(senderId).getSocket().getOutputStream());
        oos.writeObject(message);
    }

    public void acceptOffLineMes(String senderId) throws IOException
    {
        Message message=new Message();
        message.setSender(senderId);
        message.setMesType(MessageType.MESSAGE_OFFLINE_MES);
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(senderId).getSocket().getOutputStream());
        oos.writeObject(message);
    }

    public void showLogin(String senderId) throws IOException {
        Message message=new Message();
        message.setSender(senderId);
        message.setMesType(MessageType.MESSAGE_OTHER_LOGIN);
        message.setSendTime(new Date().toString());//发送时间
        //发送给服务端
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(senderId).getSocket().getOutputStream());
        oos.writeObject(message);

    }

    public void getAccounts(String senderId) throws IOException
    {
        Message message=new Message();
        message.setMesType(MessageType.MESSAGE_GET_ACCOUNTS);
        message.setSender(senderId);
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(senderId).getSocket().getOutputStream());
        oos.writeObject(message);

    }


}
