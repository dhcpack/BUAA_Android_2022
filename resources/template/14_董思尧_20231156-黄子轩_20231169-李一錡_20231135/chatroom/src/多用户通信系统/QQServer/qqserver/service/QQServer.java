package 多用户通信系统.QQServer.qqserver.service;


import 多用户通信系统.QQServer.com.qqcommon.Message;
import 多用户通信系统.QQServer.com.qqcommon.MessageType;
import 多用户通信系统.QQServer.com.qqcommon.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import 多用户通信系统.myJDBC.linkMySQL;
public class QQServer
{
    //服务器 监听9999 等待客户端连接 并保持通信
    private ServerSocket ss=null;
    //创建一个集合 记录合法用户 使用索引+对象的HashmMap形式 可以完整的记录所有信息 并且便于操作
    //所有的查找 只需要调用hashmap的get（key）方法即可 非常方便
    private static ConcurrentHashMap<String, User>validUsers=new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<Message>>offLineMes=new ConcurrentHashMap<>();
    //发送以后，记得从offLineMes里面删掉！
    //在静态代码块初始化可登录用户
    static
    {

        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("200","123456"));
        validUsers.put("400",new User("200","123456"));
        validUsers.put("500",new User("200","123456"));

    }
    //offLineMes的处理方法
    public static ConcurrentHashMap<String, ArrayList<Message>> getOffLineMes() {
        return offLineMes;
    }

    public static void setOffLineMes(ConcurrentHashMap<String, ArrayList<Message>> offLineMes) {
        QQServer.offLineMes = offLineMes;
    }
    public static void removeOffLineMes(String id)
    {
        offLineMes.remove(id);
    }

    public static void addOffLineMes(Message message)
    {
        ArrayList<Message>temp=offLineMes.get(message.getGetter());
        if(temp!=null)
        {
            temp.add(message);
        }
        else
        {
            ArrayList<Message>temp1=new ArrayList<Message>();
            temp1.add(message);
            offLineMes.put(message.getGetter(),temp1);
        }

    }

    private boolean checkUser(String userId, String passwd) throws SQLException
    {

        String s=linkMySQL.checkPwd("account",userId);
        //id 不存在
        if(s==null)
        {
            return false;
        }
        //已经登陆过了
        if(ManageClientThreads.getServerConnectThread(userId)!=null)
        {
            return false;
        }
        //id对 密码错
        if(!passwd.equals(s))
        {
            return false;
        }
        return true;
    }

    public QQServer()
    {
        try
        {
            System.out.println("服务器端在9999端口监听...");
            //启动推送新闻的线程
            new Thread(new SendNewstoAllService()).start();
            ss=new ServerSocket(9999);

            while(true)//当和某个客户端建立连接后 继续监听
            {//对于每一个来自客户端的请求登录线程
                // 创建通讯端口 如果登录成功 开启新线程 加入端口，并将放进管理集合中
                // 如果登录失败 关闭端口 开始接收新的客户端请求


                Socket socket=ss.accept();
                //得到socket关联的对象输入流
                ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                User u=(User)ois.readObject();//读取客户端发送的user对象
                //创建一个用于回复的Message对象
                Message message=new Message();
                //回复用的输出流
                ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());

                //回复用户注册
                if(u.regtag==1)
                {
                    u.regtag=0;
                    boolean check=linkMySQL.addAccount(u.getUserId(),u.getPasswd());
                    if(check)
                       message.setMesType(MessageType.MESSAGE_REG_SUCCEED);
                    else
                        message.setMesType(MessageType.MESSAGE_REG_FAILED);
                    oos.writeObject(message);
                    continue;
                }

                //验证
                //登录成功
                if(checkUser(u.getUserId(),u.getPasswd()))
                {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    //创建一个线程 和客户端保持通信 该线程需要持有socket对象

                    ServerConnectThread serverConnectThread=new ServerConnectThread(socket,u.getUserId());
                    serverConnectThread.start();

                    ManageClientThreads.addClientThread(u.getUserId(),serverConnectThread);


                }
                else
                {
                    //登录失败
                    System.out.println("用户 id="+u.getUserId()+" pwd="+u.getPasswd()+"验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    //关闭socket
                    socket.close();

                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {try
            {
                //服务端如果离开while循环 说明服务器不再监听
                ss.close();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }
}
