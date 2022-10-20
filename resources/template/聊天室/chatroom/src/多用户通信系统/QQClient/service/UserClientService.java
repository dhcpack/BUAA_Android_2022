package 多用户通信系统.QQClient.service;


import 多用户通信系统.QQServer.com.qqcommon.Message;
import 多用户通信系统.QQServer.com.qqcommon.MessageType;
import 多用户通信系统.QQServer.com.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;

public class UserClientService
{
    //因为可能在其他地方使用user信息 因此要做成员属性 可以直接由u 访问到
    private  User u=new User();
    private Socket socket;

    //该类完成用户登录和注册等功能

    public int userRegist(String userId,String pwd)
    {
        int b=0;
        //创建User对象
        u.setUserId(userId);
        u.setPasswd(pwd);

        //连接到服务器 发送u对象
        try
        {
            u.regtag=1;
            socket=new Socket("10.135.36.184",9999); //InetAddress.getLocalHost(),9999);  //  "172.30.144.1",9999);      //"192.168.2.114",9999);  "10.135.36.184",9999);
            //对象通讯 使用对象输出流
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送User对象
            //接收服务器端回复的message
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
            //将读取到的对象信息强转为Message对象
            Message ms=(Message) ois.readObject();

            // Message类中的get方法 和 MessageType接口中的常量MLS
            if(ms.getMesType().equals(MessageType.MESSAGE_REG_SUCCEED))
            {   //注册成功
                b = 1;
            }
            else
            {
                //登录失败->不能启动线程 但是socket已经启动了
                b = 0;
            }
            socket.close();

        } catch (Exception e)
        {
            if (e instanceof ConnectException)
            {
                return -1;
            }
            e.printStackTrace();
        }

        return b;
    }

    /**
     *
     * @param userId 输入账号
     * @param pwd    输入密码
     * @return   1->成功登录; 0->登陆失败; -1->无法连接至服务器
     */
    public int checkUser(String userId,String pwd)
    {
        u.regtag=0;
        int b = 0;
        //创建User对象
        u.setUserId(userId);
        u.setPasswd(pwd);

        //连接到服务器 发送u对象
        try
        {
            socket=new Socket("10.135.36.184",9999);    //InetAddress.getLocalHost(),9999);
            //对象通讯 使用对象输出流
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送User对象
            //接收服务器端回复的message
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
            //将读取到的对象信息强转为Message对象
            Message ms=(Message) ois.readObject();

            // Message类中的get方法 和 MessageType接口中的常量MLS
            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED))
            {   //登录成功
                b=1;
                //开启一个线程 便于维护-> 创建一个类 ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread
                        =new ClientConnectServerThread(socket);
                clientConnectServerThread.start();//线程启动
                //多客户端 必须用线程集合管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId,clientConnectServerThread);
            }
            else
            {
                //登录失败->不能启动线程 但是socket已经启动了
                b=0;
                socket.close();
            }

        } catch (Exception e)
        {
            if (e instanceof ConnectException)
            {
                return -1;
            }
            e.printStackTrace();
        }
        return b;
    }

    //向服务器请求在线用户列表
    public void onlineFriendList()
    {
        //发送一个message MESSAGE_GET_ONLINEFRIEND
        Message message=new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINEFRIEND);
        message.setSender(u.getUserId());

        //发送给服务器
        //要获得当前用户线程的socket 对应的ObjectOutputStream对象
        try
        {
            ClientConnectServerThread clientConnectServerThread=
            ManageClientConnectServerThread.getClientServerThread(u.getUserId());

            ObjectOutputStream oos=new ObjectOutputStream
             (clientConnectServerThread.getSocket().getOutputStream());
            //发送消息
            oos.writeObject(message);//发送message对象
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //编写方法 退出客户端 并给服务器端发送退出系统的message对象
    public void logout()
    {
        //设置message信息
        Message message=new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());//指定是哪个客户端

        //发送message
        try
        {
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            //System.out.println(u.getUserId()+" 退出系统 ");
            System.exit(0);//结束聊天室客户端的进程
        }catch (IOException e)
        {
            //System.out.println("1111");
            e.printStackTrace();
        }

    }
}
