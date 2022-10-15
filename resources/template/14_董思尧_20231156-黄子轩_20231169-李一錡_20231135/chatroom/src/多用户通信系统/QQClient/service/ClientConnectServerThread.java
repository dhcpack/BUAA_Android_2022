package 多用户通信系统.QQClient.service;


import 多用户通信系统.QQClient.view.*;
import 多用户通信系统.QQServer.com.qqcommon.Message;
import 多用户通信系统.QQServer.com.qqcommon.MessageType;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnectServerThread extends Thread
{
    //线程必须持有socket以进行通讯
    private Socket socket;
    public ClientConnectServerThread(Socket socket)
    {
        this.socket=socket;
    }

    private ClientChatGui chatGui = QQView.getChatGui(); //组合聊天UI界面
    private JTextArea chatRecord = QQView.getChatRecord();
    private ArrayList<String> allGroupsList = QQView.getAllGroupsList();

    //聊天界面需要的属性
//    private ArrayList<JButton> friendsButton = QQView.getFriendsButton();
//    private ArrayList<JLabel> friendsStatus = QQView.getFriendsStatus();
//    private ArrayList<JPanel> friendsSBack = QQView.getFriendsSBack();
    @Override
    public void run()//两个run之间都是 端口通信 接收另一方的message并进行对应操作 再向对方返回message
    {
        //因为Thread 需要在后台和服务器通信，因此需要做成while循环  线程开启后 每次输出"客户端线程，等待读取从服务器端发送的消息"然后等待服务器端的消息 这句话标志着while单次循环的开始
        //在后台和服务器不断通信

        //这个while循环导致了交流模式为 每次用户端发送1条消息 所有操作都要打包在一条消息里
        //并且这个消息的标识必须大家都认识
        // 在客户端的console界面是获取不了服务器端的信息的 所有信息都在服务器端


        //登录后，先写一个方法获得离线消息
        while(true)
        {
            System.out.println("客户端线程，等待读取从服务器端发送的消息");
            try
            {
                // 传入的socket如果突然退出了 那么这个getInputStream显然会出现IO异常 还没有进行处理
                ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                //阻塞 直到从服务器端口传来message对象
                Message message=(Message)ois.readObject();
                //目前还没有使用message
                //根据服务器端回送的message类型 进行不同的处理


                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINEFRIEND))
                {
                    //取出在线列表信息并显示
                    String[] onlineUsers =message.getContent().split(" ");
                    System.out.println("\n======当前在线用户列表=======");
                    for(int i=0;i< onlineUsers.length;i++)
                    {
                        System.out.println("用户: "+onlineUsers[i]);
                    }
                    chatGui.updateFriendsList(onlineUsers);
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES))
                {
                    //
                    //把服务器端收到的消息展示一下 这个消息是其他用户发来的
                    System.out.println("\n"+message.getSender()+" 对你("
                    +message.getGetter()+")说: "+message.getContent());
                    JTextArea targetRecord = chatGui.getTargetChatTextArea(message.getSender());
                    targetRecord.append("\n//" + message.getSendTime() + "//");
                    //targetRecord.append("\n"+message.getSender()+" 对你(" + message.getGetter()+")说: "+message.getContent());
                    targetRecord.append("\n"+ message.getSender()+": "+message.getContent());


                }
                else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_NOTFOUND))
                {
                    chatRecord.append("\n未选择聊天对象！");
                    System.out.println("指定用户不存在");
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_OFFLINE_MES))
                {
                    System.out.println("消息已离线发送");
                    JTextArea targetRecord = chatGui.getTargetChatTextArea(message.getSender());
                    targetRecord.append("\n//" + message.getSendTime() + "//");
                    targetRecord.append("\n向" + message.getSender() + "发送离线消息: " + message.getContent());  //这个要改
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_OFFLINE_GET))
                {
                    ArrayList<String>offLineMes=message.getOffLineMes();
                    if(offLineMes.size()==0)
                    {
                        System.out.println("没有离线消息");
                        continue;
                    }
                    else
                    {
                        System.out.println("一共收到"+offLineMes.size()+"条离线消息");
                    }
                    for(String s:offLineMes)
                    {
                        chatRecord.append("\n" + s);
                        System.out.println(s);
                    }
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_SENDSUCCEED))
                {
                    System.out.println("发送成功");
                    JTextArea targetRecord = chatGui.getTargetChatTextArea(message.getSender());
                    targetRecord.append("\n//" + message.getSendTime() + "//");
                    //targetRecord.append("\n" + " 你对 "+message.getSender() + " 说:" + message.getContent());
                    targetRecord.append("\n" + "你: " + message.getContent());
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_TOALL_MES))
                {
                    //显示在客户端
                    System.out.println("\n"+message.getSender()+" 对大家说:"+message.getContent());
                    new PopService("***** 在线聊天室服务器消息: *****\n" + message.getContent());

                }
                else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_CREATE))
                {
                    System.out.println("创建群聊成功");
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_LIST))
                {
                   ArrayList<String>groupList=message.getGroupList();
                   if(groupList.size()==0)
                   {
                       System.out.println("当前没有群聊");
                       allGroupsList.clear();
                       continue;
                   }
                   else
                   {
                       System.out.println("共有"+groupList.size()+"个聊天室可以进入:");
                       allGroupsList.addAll(groupList);
                   }
                   for(String group:groupList)
                   {
                       System.out.println(group);
                   }
                   //更新用户UI全部群聊列表
                   //chatGui.updateGroupsList(groupList);
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_ENTER_SUCCEED))
                {
                    System.out.println("你已成功加入群聊"+message.getGroupName());
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_ENTER_FAILED))
                {
                    System.out.println("你已经在群聊"+message.getGroupName()+"中");
                    new PopDialog("你已经在群聊"+message.getGroupName()+"中");
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_MEMBERNOTFOUND))
                {
                    System.out.println("你不在群聊"+message.getGroupName()+"中");
                    //new PopDialog("你不在群聊"+message.getGroupName()+"中");
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_MES))
                {
                    System.out.println("群聊"+message.getGroupName()+"中,"
                            +message.getSender()+"说:"+message.getContent());
                    JTextArea targetRecord = chatGui.getTargetGroupTextArea(message.getGroupName());
                    targetRecord.append("\n//" + message.getSendTime() + "//");
                    //targetRecord.append("\n" + "群聊"+message.getGroupName()+"中,"+message.getSender()+"说:"+message.getContent());
                    targetRecord.append("\n" + message.getSender()+": "+message.getContent());
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES))//文件消息
                {
                    //解包message 并输出文件
                    System.out.println("\n"+message.getSender()+" 给 "+message.getGetter()
                    +" 发送文件 "+message.getSrc()+"到电脑目录"+message.getDest());
                    try
                    {
                        FileOutputStream fileOutputStream=new FileOutputStream(message.getDest());
                        fileOutputStream.write(message.getFileBytes());
                        fileOutputStream.flush();
                        fileOutputStream.close();

                    }catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                        System.out.println("lala2");
                    }
                    System.out.println("\n"+"保存文件成功");
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT))
                {
                    String[] onlineUsers =message.getContent().split(" ");
                    System.out.println(message.getSender()+"已退出");
                    chatGui.updateFriendsList(onlineUsers);
                    QQView.getMessageClientService().getAccounts(chatGui.getUserId());
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_OTHER_LOGIN))
                {
                    String[] onlineUsers =message.getContent().split(" ");
                    System.out.println(message.getSender()+"已登录");
                    chatGui.updateFriendsList(onlineUsers);
                    QQView.getMessageClientService().getAccounts(chatGui.getUserId());
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_GET_ACCOUNTS))
                {
                    System.out.println("已注册好友: " + message.getAccountList());
                    ArrayList<String> allRegistedUsers = message.getAccountList();
                    chatGui.updateRegistedFriendsList(allRegistedUsers);
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_GET_MYGROUPS))
                {
                    System.out.println("当前所在全部群聊：" + message.getMyGroupList());
                    chatGui.updateGroupsList(message.getMyGroupList());
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_QUIT_GROUP))
                {
                    System.out.println("退出群聊"+message.getGroupName()+"成功");
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_QUITGROUP_FAIL))
                {
                    System.out.println("你本来就不在群聊"+message.getGroupName()+"中");
                    new PopDialog("你本来就不在群聊"+message.getGroupName()+"中");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("lala");
            }

        }
    }

    public Socket getSocket()
    {
        return socket;
    }

}
