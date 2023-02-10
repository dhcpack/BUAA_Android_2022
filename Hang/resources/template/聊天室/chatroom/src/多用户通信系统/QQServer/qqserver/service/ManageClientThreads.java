package 多用户通信系统.QQServer.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

public class ManageClientThreads
{
    //用于管理与客户端连接的端口
    private static HashMap<String,ServerConnectThread>hm=new HashMap<>();
    //添加线程对象

    public static HashMap<String, ServerConnectThread> getHm()
    {
        return hm;
    }

    public static void addClientThread(String userId, ServerConnectThread serverConnectThread)
    {
        hm.put(userId,serverConnectThread);

    }
    //根据userId 返回ServerConnectClientThread线程
    public static ServerConnectThread getServerConnectThread(String userId)
    {
        return hm.get(userId);
    }

    public static String getOnlineUser()
    {
        //集合遍历 遍历hashmap的key
        Iterator<String> iterator=hm.keySet().iterator();
        String onlineUserList="";
        while(iterator.hasNext())
        {
            onlineUserList+=iterator.next().toString()+" ";
        }
        return onlineUserList;
    }

    public static void removeServerConnectThread(String userId)
    {
        hm.remove(userId);
    }

}
