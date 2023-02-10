package 多用户通信系统.QQClient.view;


import 多用户通信系统.QQClient.service.FileClientService;
import 多用户通信系统.QQClient.service.GroupClientService;
import 多用户通信系统.QQClient.service.MessageClientService;
import 多用户通信系统.QQClient.service.UserClientService;
import 多用户通信系统.utils.Utility;
import 多用户通信系统.myJDBC.linkMySQL;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;

public class QQView implements ActionListener
{
    static JFrame qqClient = new JFrame();

    private UserClientService userClientService=new UserClientService();//用于登录服务、注册用户
    private boolean loop=true;//控制是否显示菜单
    private String  key="";//接收用户的键盘输入
    static private MessageClientService messageClientService=new MessageClientService();//对象用户私聊
    private FileClientService fileClientService=new FileClientService();//用于传输文件
    private GroupClientService groupClientService=new GroupClientService();
    static private ClientLoginGui loginGui = new ClientLoginGui();
    static private ClientChatGui chatGui = new ClientChatGui();


    //登录界面需要的属性
    private JButton login;
    private JButton regist;
    private JTextField userID;
    private JTextField passWord;

    //聊天界面需要的属性
    private ArrayList<JButton> friendsButton;
    private ArrayList<JLabel> friendsStatus;
    private ArrayList<JPanel> friendsSBack;
    //private String chattingId;
    private JButton sendButton;
    private JTextArea chatSend;
    private static JTextArea chatRecord;
    private JTextField groupOperate;
    private JButton enterGroupButton;
    private JButton quitGroupButton;
    private JButton createGroupButton;
    private static ArrayList<String> allGroupsList = new ArrayList<>();
    private ArrayList<JButton> groupsButton;
    //public String chattingGroup;

    public static MessageClientService getMessageClientService() {
        return messageClientService;
    }
//    public String getChattingId() {
//        return chattingId;
//    }
    public static JTextArea getChatRecord() {
        return chatRecord;
    }
    public ArrayList<JButton> getFriendsButton() {
        return friendsButton;
    }
    public ArrayList<JLabel> getFriendsStatus() {
        return friendsStatus;
    }
    public ArrayList<JPanel> getFriendsSBack() {
        return friendsSBack;
    }
    public static ClientLoginGui getLoginGui() {
        return loginGui;
    }
    public static ClientChatGui getChatGui() {
        return chatGui;
    }
    public static ArrayList<String> getAllGroupsList() {
        return allGroupsList;
    }

    private void mainMenu() throws IOException, SQLException
    {
        //登录界面初始化
        loginGui.init();
        //chatGui.init();

        //提取登录界面组件
        login = loginGui.getLogin();
        regist = loginGui.getRegist();
        userID = loginGui.getUserID();
        passWord = loginGui.getPwd();

        //添加登录界面事件监听器
        login.addActionListener(this);
        regist.addActionListener(this);

        //提取聊天界面组件
        friendsButton = chatGui.getFriendsButton();
        friendsSBack = chatGui.getFriendsSBack();
        friendsStatus = chatGui.getFriendsStatus();
        sendButton = chatGui.getSendButton();
        chatSend = chatGui.getChatSend();
        chatRecord = chatGui.getChatRecord();
        groupOperate = chatGui.getGroupOperate();
        enterGroupButton = chatGui.getEnterGroupButton();
        quitGroupButton = chatGui.getQuitGroupButton();
        createGroupButton = chatGui.getCreateGroupButton();
        groupsButton = chatGui.getGroupsButton();

        //添加聊天界面事件监听器
        sendButton.addActionListener(this);
        enterGroupButton.addActionListener(this);
        quitGroupButton.addActionListener(this);
        createGroupButton.addActionListener(this);

        while(loop)
        {
            //System.out.println(InetAddress.getLocalHost());
            System.out.println("=========欢迎登录网络通讯系统===========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 2 用户注册");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择 :");
            key= Utility.readString(1);
            //根据用户输入处理不同逻辑
            switch (key)
            {
                case "1":
                    System.out.print("请输入登录用户号: ");
                    String userId=Utility.readString(50);
                    System.out.print("请输入登录密码: ");
                    String pwd=Utility.readString(50);
                    //创建user对象 匹配服务器端是否有这样的对象
                    //略
                    //
                    //连接服务器 并且如果登录成功就开启新线程
                    //这时候窗口是由main主线程和用户登录开启后的子线程共同占有的
                    if(userClientService.checkUser(userId,pwd) == 1)
                    {
                        System.out.println("=========欢迎(用户 "+userId+" 登录成功)===========");
                        //接收离线消息->必须三步走 发信到服务器 再由服务器回信 只有服务器手上有数据
                        messageClientService.acceptOffLineMes(userId);
                        //进入二级菜单
                        while(loop)
                        {
                            System.out.println("\n========网络通信系统二级菜单(用户 "+userId+" )=======");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 5 创建群聊");
                            System.out.println("\t\t 6 显示聊天室列表");
                            System.out.println("\t\t 7 加入聊天室");
                            System.out.println("\t\t 8 在群聊中发言");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("\t\t 10 显示所有用户");
                            System.out.println("\t\t 11 显示当前已加入群聊");
                            System.out.println("\t\t 12 退出群聊");
                            System.out.print("请输入你的选择: ");
                            key=Utility.readString(2);
                            switch (key)
                            {
                                case "1" ->
                                { //写一个方法来获取在线用户列表
                                    System.out.println("显示在线用户列表");
                                    userClientService.onlineFriendList();//开了一个子线程 去获取用户列表去了
                                }
                                case "2" ->
                                {
                                    System.out.println("请输入消息内容: ");
                                    String s=Utility.readString(100);
                                    //调用一个方法将方法发送到服务器端
                                    messageClientService.sendMessageToAll(s,userId);
                                }

                                case "3" ->
                                {
                                    //发起者将消息打包发给服务器端 服务器将消息发给接收者
                                    System.out.println("请输入想聊天的用户号(在线）:");
                                    String getterId=Utility.readString(50);

                                    System.out.println("请输入想说的话: ");
                                    String content=Utility.readString(100);
//                                    ManageClientConnectServerThread.showInfo();
//                                    if(ManageClientConnectServerThread.getClientServerThread(getterId)==null)
//                                    {
//                                        System.out.println("该用户不存在或者不在线");
//                                        continue;
//                                    }
                                    //编写一个方法，将消息发送给服务器端
                                    messageClientService.sendMessageToOne(content,userId,getterId);
                                }

                                case "4" ->
                                {
                                    System.out.println("请输入你想发送文件的用户: ");
                                    String getterId=Utility.readString(50);
                                    System.out.println("请输入发送文件的路径(形式如D:\\xx.jpg)");
                                    String src=Utility.readString(100);
                                    System.out.println("请输入把文件发送到对方的路径(形式D:\\yy.jpg)");
                                    String dest=Utility.readString(100);
                                    fileClientService.sendFileToOne(src,dest,userId,getterId);

                                }
                                case "5"->//创建群聊
                                {
                                    //写一个方法 由现有用户创建群聊
                                    System.out.print("请输入群聊名称:");
                                    String groupName=Utility.readString(50);
                                    groupClientService.createGroupChat(userId,groupName);


                                }
                                case "6"->//显示聊天室列表
                                {
                                    groupClientService.showGroupList(userId);


                                }
                                case "7"->//加入聊天室
                                {
                                    groupClientService.showGroupList(userId);
                                    System.out.print("请输入你需要加入的聊天室名称: ");
                                    String groupName=Utility.readString(50);
                                    groupClientService.enterGroup(userId,groupName);

                                }
                                case "8"->//在群聊中发言
                                {
                                    System.out.print("你需要发言的聊天室名称: ");
                                    String groupName=Utility.readString(50);
                                    System.out.print("你要说的话是: ");
                                    String words=Utility.readString(1000);
                                    groupClientService.chatIngroup(userId,groupName,words);

                                }


                                case "9" ->
                                {
                                    //调用方法 给服务器发送一个退出系统的message
                                    userClientService.logout();
                                    loop = false;
                                }

                                case "10"->
                                        {
                                            messageClientService.getAccounts(userId);
                                        }

                                case "11"->
                                        {
                                            groupClientService.getMygroupList(userId);

                                        }
                                case "12"->
                                        {
                                            System.out.print("请输入你需要加入的聊天室名称: ");
                                            String groupName=Utility.readString(50);
                                            groupClientService.QuitGroup(userId,groupName);

                                        }

                            }
                        }
                    }
                    else//验证失败
                    {
                        System.out.println("=========登录失败===========");

                    }
                    break;

                case "2":
                    System.out.println("请输入用户号: ");
                    String userId1=Utility.readString(50);
                    System.out.println("请输入密 码: ");
                    String pwd1=Utility.readString(50);

                    int outcome = userClientService.userRegist(userId1,pwd1);
                    if(outcome == 1)
                    {
                        System.out.println("注册成功");
                    }
                    else
                    {
                        System.out.println("注册失败 用户名已经被占用");
                    }
                    break;


                case "9":
                    loop=false;
                    return;
            }
        }
    }

    public static void main(String[] args) throws IOException, SQLException
    {

        new QQView().mainMenu();
        //调用方法 给服务器端发送一个退出系统的message对象
        //服务器接受消息后 在线程集合中删除对应的端口
        // 把socket持有的socket关闭
        // 然后退出该线程的run
        //将服务器端口断开后，服务器中管理该用户端的线程退出
        //用户端线程退出

        System.out.println("==========客户端退出系统===========");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = (Object) e.getSource();
        //点击登录按钮，判断账号密码是否正确
        if (source.equals(login)){
            String userId = userID.getText();
            String pwd = passWord.getText();
            userID.setText("");
            passWord.setText("");
            int trylog = userClientService.checkUser(userId,pwd);
            if( trylog == 1)
            {
                loginGui.setVisible(false); //关闭登录界面
                chatGui.init(userId);             //开启聊天界面
                chatGui.setQqView(this);
                //关闭聊天界面时自动退出登录（无异常退出）
                chatGui.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        userClientService.logout();
                    }
                });
                //向其他用户发送提示自己上线的通知
                try {
                    //向其他用户发送提示自己上线的通知
                    messageClientService.showLogin(userId);
                    //获取在线好友列表
                    userClientService.onlineFriendList();
                    //获取全部注册好友列表
                    messageClientService.getAccounts(userId);
                    //获取离线消息
                    messageClientService.acceptOffLineMes(userId);
                    //获取全部群聊列表
                    groupClientService.showGroupList(userId);
                    //获取并更新全部所在群聊列表
                    groupClientService.getMygroupList(userId);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //new PopDialog("登陆成功！");
            }
            else if (trylog == 0)
            {
                new PopDialog("密码错误/账号不存在/账号已登录！");
            }
            else
            {
                new PopDialog("服务器未开启！");
            }
        }
        //点击注册按钮
        else if (source.equals(regist)){
            if (!userID.getText().equals("") && !passWord.getText().equals(""))
            {
                String userId = userID.getText();
                String pwd = passWord.getText();
                userID.setText("");
                passWord.setText("");
                int outcome = userClientService.userRegist(userId,pwd);
                if(outcome == 1)
                {
                    new PopDialog("注册成功! 请登录。");
                }
                else if (outcome == 0)
                {
                    new PopDialog("注册失败 用户名已经被占用");
                }
                else
                {
                    new PopDialog("服务器未开启！");
                }
                System.out.println("regist");
            }
            else
            {
                new PopDialog("用户名/密码不能为空");
            }

        }
        //点击好友列表中的按钮
        else if (friendsButton.contains(source))
        {
            //chattingId = chatGui.getChattingId();
            String chattingId = ((JButton)source).getText();
            chatGui.setChattingId(chattingId);
            chatGui.updateChatFrame(chattingId);
            //chattingGroup = "";
            chatGui.setChattingGroup("");
            //chatRecord.append("\n********************* 切换至好友：" + chattingId + " *********************");
            //System.out.println(chattingId);
        }
        //点击发送按钮
        else if (source.equals(sendButton))
        {
            if (!chatSend.getText().equals(""))
            {
                if (chatGui.getChattingId().equals("") && chatGui.getChattingGroup().equals(""))
                {
                    new PopDialog("请选择聊天对象");
                }
                //发送私聊信息
                else if (!chatGui.getChattingId().equals(""))
                {
                    try {
                        String userId = chatGui.getUserId();
                        //System.out.println(userId);
                        //System.out.println(chattingId);
                        messageClientService.sendMessageToOne(chatSend.getText(),userId,chatGui.getChattingId());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                //发送群聊消息
                else if (!chatGui.getChattingGroup().equals(""))
                {
                    try {
                        String userId = chatGui.getUserId();
                        groupClientService.chatIngroup(userId,chatGui.getChattingGroup(),chatSend.getText());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                //chatRecord.append("\n" + " 你对 "+chattingId+" 说 " + chatSend.getText());
                chatSend.setText("");
            }
        }
        //点击加入群聊按钮
        else if (source.equals(enterGroupButton))
        {
            if (!groupOperate.getText().equals(""))
            {
                if (allGroupsList.contains(groupOperate.getText()))
                {
                    try {
                        String userId = chatGui.getUserId();
                        groupClientService.enterGroup(userId,groupOperate.getText());
                        groupClientService.getMygroupList(userId);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else
                {
                    new PopDialog("不存在该群聊!您可以先创建该群聊。");
                }

                groupOperate.setText("");
            }
        }
        //点击创建群聊按钮
        else if (source.equals(createGroupButton))
        {
            if (!groupOperate.getText().equals(""))
            {
                if (groupOperate.getText().matches("(-)?[0-9]+(.[0-9]+)?"))
                {
                    new PopDialog("群聊名称不合法！（不能为数字）");
                }
                else if (allGroupsList.contains(groupOperate.getText()))
                {
                    new PopDialog("群聊已经存在！无法重复创建。");
                }
                else
                {
                    try {
                        String userId = chatGui.getUserId();
                        groupClientService.createGroupChat(userId,groupOperate.getText());
                        groupClientService.getMygroupList(userId);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                groupOperate.setText("");
            }
        }
        //点击退出群聊按钮
        else if (source.equals(quitGroupButton))
        {
            try {
                String userId = chatGui.getUserId();
                groupClientService.QuitGroup(userId,groupOperate.getText());
                groupClientService.getMygroupList(userId);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            groupOperate.setText("");
        }
        //点击群聊列表中的按钮
        else if (groupsButton.contains(source))
        {
            //System.out.println("点击了群聊按钮");
            String chattingGroup = ((JButton)source).getText();
            chatGui.setChattingGroup(chattingGroup);
            chatGui.updateGroupFrame(chattingGroup);
            //chattingId = "";
            chatGui.setChattingId("");
        }
    }

}
