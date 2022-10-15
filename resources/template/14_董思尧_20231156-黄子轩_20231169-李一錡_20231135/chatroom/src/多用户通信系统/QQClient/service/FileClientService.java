package 多用户通信系统.QQClient.service;


import 多用户通信系统.QQServer.com.qqcommon.Message;
import 多用户通信系统.QQServer.com.qqcommon.MessageType;

import java.io.*;

public class FileClientService
{
    public void sendFileToOne(String src,String dest,String senderId,String getterId) throws IOException {
        //读取src文件--->message
        Message message=new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);
        //读取文件
        FileInputStream fileInputStream=null;
        byte[]fileBytes=new byte[(int)new File(src).length()];
        if(fileBytes.length==0)
        {
            return;
        }
        try
        {
            fileInputStream=new FileInputStream(src);
            fileInputStream.read(fileBytes);//将src文件读入程序的字节数组
            //将文件对应的字节数组设置message
            message.setFileBytes(fileBytes);
            if(fileInputStream!=null)
            {
                fileInputStream.close();
            }

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("路径错误，发送失败");
        }
        //提示信息
        System.out.println("\n"+message.getSender()+" 给 "+message.getGetter()+"发送文件"+src+" 到对方电脑的 "+dest);
        //发送
        ObjectOutputStream oos=
                new ObjectOutputStream(ManageClientConnectServerThread.getClientServerThread(senderId).getSocket().getOutputStream());
        oos.writeObject(message);


    }
}
