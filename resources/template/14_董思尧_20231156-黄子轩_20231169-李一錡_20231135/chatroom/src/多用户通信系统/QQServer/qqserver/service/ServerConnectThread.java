package 多用户通信系统.QQServer.qqserver.service;


import 多用户通信系统.QQServer.com.qqcommon.Message;
import 多用户通信系统.QQServer.com.qqcommon.MessageType;
import 多用户通信系统.myJDBC.linkMySQL;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServerConnectThread extends Thread
{
    //类对象与某个客户端保持通信
    private Socket socket;
    private String userId;//连接到服务器的用户id

    public ServerConnectThread(Socket socket,String userId)
    {
        this.socket=socket;
        this.userId=userId;
    }
    public Socket getSocket()
    {
        return  socket;
    }

    @Override
    public void run()//可以发送 接收消息
    {
       while(true)
       {
           try
           {
               System.out.println("服务端和客户端"+userId+"保持通讯，读取数据..");
               ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
               Message message=(Message) ois.readObject();

               //根据接收的message类型 做相应的业务处理

               //拉取在线用户列表请求
               if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINEFRIEND))
               {
                   System.out.println(message.getSender()+"需要在线列表");
                   //在服务器端可以知道 连接上服务器的线程就是在线用户
                   String onlineUser=ManageClientThreads.getOnlineUser();
                   //返回message
                   //构建一个message对象 返回给客户端
                   Message message2=new Message();
                   //全部是信息类的类属性和对应set
                   message2.setMesType(MessageType.MESSAGE_RET_ONLINEFRIEND);
                   message2.setContent(onlineUser);
                   message2.setGetter(message.getSender());

                   //从端口返回消息给客户端
                   ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                   oos.writeObject(message2);

               }
               //普通发信请求
               else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES))
               {
                   //判断一下接收者是否在线或是否存在
                   String getterId=message.getGetter();
                   String onlineUser=ManageClientThreads.getOnlineUser();
                   String []a=onlineUser.split(" ");
                   int det=0;
                   for(int i=0;i<a.length;i++)
                   {
                       if(a[i].equals(getterId))
                       {
                           det=1;
                           break;
                       }
                   }
                   //不存在 则不转发消息 并向发送端返回消息
                   if(det==0)
                   {
                       Message message2=new Message();
                       if(linkMySQL.checkPwd("account",message.getGetter())==null)
                       {
                           message2.setMesType(MessageType.MESSAGE_CLIENT_NOTFOUND);
                       }
                       else   //添加离线消息
                       {
                           message2.setMesType(MessageType.MESSAGE_OFFLINE_MES);
                           linkMySQL.addOffLineMsg(message.getGetter(),message.getSender(),message.getContent());
                       }

                       //获取setterId 得到对应线程
                       //message2
                       message2.setContent(message.getContent());
                       message2.setGetter(message.getSender());
                       message2.setSender(message.getGetter());
                       message2.setSendTime(message.getSendTime());
                       ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                       oos.writeObject(message2);//如果用数据库 可以做一个离线的发送消息
                   }
                   else//存在该在线用户
                   {

                       //服务器对消息进行转发 发给接收者
                       //根据message获得getterId 然后得到对应线程
                       ServerConnectThread serverConnectThread=
                               ManageClientThreads.getServerConnectThread(message.getGetter());
                       //得到socket的对象输出流 将message对象转发给指定的客户端
                       ObjectOutputStream oos=new ObjectOutputStream(serverConnectThread.getSocket().getOutputStream());
                       oos.writeObject(message);//如果用数据库 可以做一个离线的发送消息

                       //向发送者返回送信成功
                       Message message2=new Message();
                       message2.setMesType(MessageType.MESSAGE_CLIENT_SENDSUCCEED);
                       message2.setGetter(message.getSender());
                       message2.setSender(message.getGetter());
                       message2.setContent(message.getContent());
                       message2.setSendTime(message.getSendTime());
                       ObjectOutputStream oos1=new ObjectOutputStream(socket.getOutputStream());
                       oos1.writeObject(message2);//如果用数据库 可以做一个离线的发送消息
                   }


               }
               //处理离线消息 服务器从数据库中获得离线信息 并放进message中发送回去
               else if(message.getMesType().equals(MessageType.MESSAGE_OFFLINE_MES))
               {
                   message.setMesType(MessageType.MESSAGE_OFFLINE_GET);
                   message.setOffLineMes(linkMySQL.getOffLineMsg(message.getSender()));
                   linkMySQL.deleteMsg("offlinemsg","id",message.getSender());
                   ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                   oos.writeObject(message);

               }
               //处理群发请求
               else if(message.getMesType().equals(MessageType.MESSAGE_TOALL_MES))
               {
                   //遍历所有线程 向所有线程发送信息
                   HashMap<String,ServerConnectThread> hm=ManageClientThreads.getHm();
                   Iterator<String>iterator=hm.keySet().iterator();
                   while(iterator.hasNext())
                   {
                       //取出在线用户的id
                       String onLineUserId=iterator.next().toString();
                       //群发不能发给自己
                       if(!onLineUserId.equals(message.getSender()))
                       {
                           //进行转发
                           ObjectOutputStream oos=
                                   new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                           oos.writeObject(message);

                       }
                   }

               }
               //发送文件请求
               else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES))
               {
                   //判断一下接收者是否在线或是否存在
                   String getterId=message.getGetter();
                   String onlineUser=ManageClientThreads.getOnlineUser();
                   String []a=onlineUser.split(" ");
                   int det=0;
                   for(int i=0;i<a.length;i++)
                   {
                       if(a[i].equals(getterId))
                       {
                           det=1;
                           break;
                       }
                   }
                   //不存在或者不在线 则不转发消息 并向发送端返回消息
                   if(det==0)
                   {
                       Message message2=new Message();
                       message2.setGetter(message.getSender());
                       //判断用户是否存在
                       if(linkMySQL.checkPwd("account", message.getGetter()) == null)
                       {
                           message2.setMesType(MessageType.MESSAGE_CLIENT_NOTFOUND);
                       }
                       //添加离线消息
                       else
                       {
                           message2.setMesType(MessageType.MESSAGE_OFFLINE_MES);
                           linkMySQL.addOffLineMsg(message.getGetter(),message.getSender(),message.getContent());
                       }

                       QQServer.addOffLineMes(message);
                       //获取setterId 得到对应线程

                       ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                       oos.writeObject(message2);//如果用数据库 可以做一个离线的发送消息
                   }
                   else
                   {
                       ServerConnectThread serverConnectThread=ManageClientThreads.getServerConnectThread(message.getGetter());
                       ObjectOutputStream oos=new ObjectOutputStream(serverConnectThread.getSocket().getOutputStream());
                       //转发
                       oos.writeObject(message);
                   }
               }
               //客户端登录提示
               else if(message.getMesType().equals(MessageType.MESSAGE_OTHER_LOGIN))
               {

                   HashMap<String,ServerConnectThread> hm=ManageClientThreads.getHm();
                   Iterator<String>iterator=hm.keySet().iterator();
                   String onlineUser=ManageClientThreads.getOnlineUser();
                   //message.setContent(onlineUser);
                   Message message2 = new Message();
                   message2.setMesType(MessageType.MESSAGE_OTHER_LOGIN);
                   message2.setContent(onlineUser);
                   message2.setSender(message.getSender());
                   //System.out.println("*****在线列表：" + onlineUser);
                   while(iterator.hasNext())
                   {
                       //取出在线用户的id
                       String onLineUserId=iterator.next().toString();
                       //群发不能发给自己
                       if(!onLineUserId.equals(message.getSender()))
                       {
                           //进行转发
                           ObjectOutputStream oos=
                                   new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                           oos.writeObject(message2);
                       }
                   }
               }
               //客户端退出请求
               else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT))
               {
                   sleep(500);
                   System.out.println(message.getSender()+"退出");
                   //将这个客户端对应的线程从集合中删除
                   //sleep(10);
                   ManageClientThreads.removeServerConnectThread(message.getSender());
                   socket.close();//把当前端口关闭
                   //退出当前线程 即直接让run方法结束 否则会不断地getSocket并报错
                   //向所有在线用户发送该用户退出信息
                   HashMap<String,ServerConnectThread> hm=ManageClientThreads.getHm();
                   Iterator<String>iterator=hm.keySet().iterator();
                   String onlineUser=ManageClientThreads.getOnlineUser();
                   Message message2 = new Message();
                   message2.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
                   message2.setContent(onlineUser);
                   message2.setSender(message.getSender());
                   //System.out.println("*****在线列表：" + onlineUser);
                   while(iterator.hasNext())
                   {
                       //取出在线用户的id
                       String onLineUserId=iterator.next().toString();
                       //群发不能发给自己
                       if(!onLineUserId.equals(message.getSender()))
                       {
                           //进行转发
                           ObjectOutputStream oos=
                                   new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                           oos.writeObject(message2);

                       }
                   }
                   break;
               }
               //创建聊天室请求
               else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_CREATE))
               {
                   linkMySQL.createGroup(message.getGroupName());
                   linkMySQL.addMember(message.getGroupName(),message.getSender());
                   ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                   oos.writeObject(message);
               }
               //显示群聊列表请求
               else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_LIST))
               {
                   ArrayList<String>groups=linkMySQL.showTables();
                   message.setGroupList(groups);
                   ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                   oos.writeObject(message);
               }
               //加入群聊申请
               else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_ENTER))
               {
                   if(linkMySQL.checkMember(message.getGroupName(),message.getSender()))
                   {
                       message.setMesType(MessageType.MESSAGE_GROUP_ENTER_FAILED);
                   }
                   else
                   {
                       message.setMesType(MessageType.MESSAGE_GROUP_ENTER_SUCCEED);
                       linkMySQL.addMember(message.getGroupName(),message.getSender());
                   }
                   ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                   oos.writeObject(message);
               }
               //处理 在聊天室中发消息
               else if(message.getMesType().equals(MessageType.MESSAGE_GROUP_MES))
               {
                   boolean b=linkMySQL.checkMember(message.getGroupName(),message.getSender());
                   if(!b)//不在群聊中
                   {
                       message.setMesType(MessageType.MESSAGE_GROUP_MEMBERNOTFOUND);
                   }
                   else
                   {
                       //在群聊中 可以发送
                       HashMap<String,ServerConnectThread> hm=ManageClientThreads.getHm();
                       Iterator<String>iterator=hm.keySet().iterator();
                       while(iterator.hasNext())
                       {
                           //取出在线用户的id
                           String onLineUserId=iterator.next().toString();
                           //发给这些人 包括自己
                           if(linkMySQL.checkMember(message.getGroupName(),onLineUserId))
                           {
                               //进行转发
                               ObjectOutputStream oos=
                                       new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                               oos.writeObject(message);
                           }
                       }
                   }
               }

               else if(message.getMesType().equals(MessageType.MESSAGE_GET_ACCOUNTS))
               {
                   ArrayList<String>accounts=linkMySQL.showAccounts();
                   message.setAccountList(accounts);
                   ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                   oos.writeObject(message);
               }

               else if(message.getMesType().equals(MessageType.MESSAGE_GET_MYGROUPS))
               {
                   ArrayList<String>groups=linkMySQL.showMyGroup(message.getSender());
                   message.setMyGroupList(groups);
                   ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                   oos.writeObject(message);
               }
               //退出群聊申请
               else if(message.getMesType().equals(MessageType.MESSAGE_QUIT_GROUP))
               {
                   //不在群聊中
                   if(!linkMySQL.checkMember(message.getGroupName(),message.getSender()))
                   {
                       message.setMesType(MessageType.MESSAGE_QUITGROUP_FAIL);
                   }
                   //在群聊中
                   else
                   {
                       linkMySQL.delMember(message.getGroupName(),message.getSender());
                   }
                   ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                   oos.writeObject(message);
               }


               else
               {

               }

           }
           catch (Exception e)
           {
               e.printStackTrace();
           }

       }
    }
}
