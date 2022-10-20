package 多用户通信系统.QQServer.qqserver.service;


import 多用户通信系统.QQClient.clientUtils.Utility;
import 多用户通信系统.QQServer.com.qqcommon.Message;
import 多用户通信系统.QQServer.com.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
/*
* 服务器端的独立线程，用来发送推送*/
public class SendNewstoAllService implements Runnable
{
    private Scanner scanner=new Scanner(System.in);

    @Override
    public void run()
    {
        //为了多次推送 用while循环
        while(true)
        {
            System.out.println("请输入服务器要推送的新闻/消息[输入exit退出推送服务]");
            String news= Utility.readString(1000);
            if(news.equals("exit"))
            {
                break;
            }
            //构建一个消息
            Message message=new Message();
            message.setSender("Server");
            message.setContent(news);
            message.setMesType(MessageType.MESSAGE_TOALL_MES);//就用群发的那个消息类型标记，在用户端当作群发消息处理就行
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人说: "+news);
            //遍历当前所有通信线程，得到socket并发送message
            HashMap<String,ServerConnectThread>hm=ManageClientThreads.getHm();
            Iterator<String>iterator=hm.keySet().iterator();
            while(iterator.hasNext())
            {
                String onLineUserId=iterator.next().toString();
                ServerConnectThread serverConnectThread=hm.get(onLineUserId);
                try {
                    ObjectOutputStream oos=new ObjectOutputStream(serverConnectThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
