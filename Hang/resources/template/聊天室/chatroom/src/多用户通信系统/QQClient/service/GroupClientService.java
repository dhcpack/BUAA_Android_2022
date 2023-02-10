package 多用户通信系统.QQClient.service;

import 多用户通信系统.QQServer.com.qqcommon.Message;
import 多用户通信系统.QQServer.com.qqcommon.MessageType;
import 多用户通信系统.myJDBC.linkMySQL;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroupClientService
{
    public void createGroupChat(String createrId,String groupName) throws IOException {
        Message message=new Message();
        message.setSender(createrId);
        message.setMesType(MessageType.MESSAGE_GROUP_CREATE);
        message.setGroupName(groupName);
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(createrId).getSocket().getOutputStream());
        oos.writeObject(message);
    }
    public void showGroupList(String id) throws IOException {
        Message message=new Message();
        message.setSender(id);
        message.setMesType(MessageType.MESSAGE_GROUP_LIST);
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(id).getSocket().getOutputStream());
        oos.writeObject(message);
    }

    public void enterGroup(String id,String groupName) throws IOException {
        Message message=new Message();
        message.setSender(id);
        message.setMesType(MessageType.MESSAGE_GROUP_ENTER);
        message.setGroupName(groupName);
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(id).getSocket().getOutputStream());
        oos.writeObject(message);
    }

    public void chatIngroup(String id,String groupName,String words) throws IOException {
        Message message=new Message();
        message.setSender(id);
        message.setMesType(MessageType.MESSAGE_GROUP_MES);
        message.setContent(words);
        message.setGroupName(groupName);
        LocalDateTime dateTime = LocalDateTime.now(); // get the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        message.setSendTime(dateTime.format(formatter));
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(id).getSocket().getOutputStream());
        oos.writeObject(message);
    }

    public void getMygroupList(String id) throws IOException
    {
        Message message=new Message();
        message.setSender(id);
        message.setMesType(MessageType.MESSAGE_GET_MYGROUPS);
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(id).getSocket().getOutputStream());
        oos.writeObject(message);
    }

    public void QuitGroup(String id,String groupName) throws IOException
    {
        Message message=new Message();
        message.setSender(id);
        message.setMesType(MessageType.MESSAGE_QUIT_GROUP);
        message.setGroupName(groupName);
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(id).getSocket().getOutputStream());
        oos.writeObject(message);
    }


}
