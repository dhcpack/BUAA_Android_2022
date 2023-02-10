package 多用户通信系统.QQClient.service;

import java.util.HashMap;

public class ManageClientConnectServerThread
{
    //key是用户id 值是线程 每个id唯一对应一个线程
    private static HashMap<String,ClientConnectServerThread>hm=new HashMap<>();
    //添加
    public static void addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread)
    {
        hm.put(userId,clientConnectServerThread);
    }
    //获取
    public static ClientConnectServerThread getClientServerThread(String userId)
    {
        //根据键 查找值并返回
        return hm.get(userId);
    }
    public static void showInfo()
    {
        System.out.println(hm);
    }


}
