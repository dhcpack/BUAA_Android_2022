package 多用户通信系统.QQClient.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClientChatGui extends JFrame
{
    private QQView qqView;

    private JPanel panelBack = new JPanel();
    //private JPanel panelList = new JPanel();
    private JScrollPane scrollList;
    private JScrollPane scrollGroupsList;
    private JScrollPane scrollChat;
    private JScrollPane scrollSend;
    private JTextArea chatSend = new JTextArea(3,29);
    private JTextArea chatRecord = new JTextArea(16,39);
    private ArrayList<ChatFrame> recordsOfChat = new ArrayList<>();
    private ArrayList<GroupFrame> recordsOfGroup = new ArrayList<>();
    private JTextArea displayingRecord = new JTextArea("");
    private JButton sendButton = new JButton("发送");
    private JTextField groupOperate = new JTextField("");
    private JButton enterGroupButton = new JButton("加入群聊");
    private JButton quitGroupButton = new JButton("退出群聊");
    private JButton createGroupButton = new JButton("创建群聊");
    private JLabel listLabel = new JLabel("好友列表");
    private JLabel groupsListLabel = new JLabel("群聊列表");
    private JLabel chatLabel = new JLabel("聊天记录");
    private JLabel welcomeLabel = new JLabel("欢迎用户 100 ！");
    private ArrayList<String> onlineFriendsIdList = new ArrayList<>();
    private ArrayList<String> myGroupsIdList = new ArrayList<>();
    private ArrayList<JButton> friendsButton = new ArrayList<>();
    private ArrayList<JLabel> friendsStatus = new ArrayList<>();
    private ArrayList<JPanel> friendsSBack = new ArrayList<>();
    private ArrayList<JButton> groupsButton = new ArrayList<>();
    //private ArrayList<JLabel> groupsStatus = new ArrayList<>();
    //private ArrayList<JPanel> groupsSBack = new ArrayList<>();
    private JPanel panelFriends = new JPanel();
    private JPanel panelGroups = new JPanel();
    private String userId = "";
    private String chattingId = "";
    private String chattingGroup = "";

    public String getChattingGroup() {
        return chattingGroup;
    }

    public void setChattingGroup(String chattingGroup) {
        this.chattingGroup = chattingGroup;
    }

    public ArrayList<JButton> getGroupsButton() {
        return groupsButton;
    }

    public JTextField getGroupOperate() {
        return groupOperate;
    }

    public JButton getEnterGroupButton() {
        return enterGroupButton;
    }

    public JButton getQuitGroupButton() {
        return quitGroupButton;
    }

    public JButton getCreateGroupButton() {
        return createGroupButton;
    }

    public ArrayList<ChatFrame> getRecordsOfChat() {
        return recordsOfChat;
    }

    public JTextArea getDisplayingRecord() {
        return displayingRecord;
    }

    public String getUserId() {
        return userId;
    }

    public String getChattingId() {
        return chattingId;
    }

    public void setChattingId(String chattingId) {
        this.chattingId = chattingId;
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

    public JTextArea getChatSend() {
        return chatSend;
    }

    public JTextArea getChatRecord() {
        return chatRecord;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setQqView(QQView qqView) {
        this.qqView = qqView;
    }

    public ClientChatGui(){};

    public void init(String UserId)
    {
        this.setTitle("在线聊天室");
        Container container = getContentPane();
        container.setLayout(null);   //取消布局
        this.userId = UserId;

        //初始化背景画板，大小700x500
        panelBack.setLayout(null);
        panelBack.setBounds(0,0,700,520);
        panelBack.setBackground(new Color(148, 210, 231, 255));
        container.add(panelBack);

        //初始化欢迎语
        welcomeLabel = new JLabel("欢迎用户 " + userId + " !");
        welcomeLabel.setBounds(450,10, 250, 30);
        welcomeLabel.setFont(new Font("宋体",Font.BOLD,14));
        panelBack.add(welcomeLabel);

        //初始化好友列表
        panelFriends.setPreferredSize(new Dimension(130,1200));
        scrollList = new JScrollPane(panelFriends);
        scrollList.setBounds(50,50,170,190);
        scrollList.setViewportView(panelFriends);
        scrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panelBack.add(scrollList);
        listLabel.setBounds(35,20, 180, 30);
        listLabel.setFont(new Font("楷体",Font.BOLD,16));
        panelBack.add(listLabel);

        //初始化群聊列表
        panelGroups.setPreferredSize(new Dimension(130,1200));
        scrollGroupsList = new JScrollPane(panelGroups);
        scrollGroupsList.setBounds(50,275,170,185);
        scrollGroupsList.setViewportView(panelGroups);
        scrollGroupsList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panelBack.add(scrollGroupsList);
        groupsListLabel.setBounds(35,245,180,30);
        groupsListLabel.setFont(new Font("楷体",Font.BOLD,16));
        panelBack.add(groupsListLabel);

        //初始化聊天记录
        chatRecord.setText("欢迎登录在线聊天系统，祝您聊天愉快！~");
        chatRecord.setLineWrap(true);  //自动换行，进制水平滚动
        chatRecord.setEditable(false);
        chatRecord.setFont(new Font("楷体",Font.PLAIN,16));
        scrollChat = new JScrollPane(chatRecord);
        scrollChat.setBounds(250,50, 400,310);
        panelBack.add(scrollChat);
        chatLabel.setBounds(235,20,180,30);
        chatLabel.setFont(new Font("楷体",Font.BOLD,16));
        panelBack.add(chatLabel);

        //初始化文字输入栏
        chatSend.setText("");
        chatSend.setLineWrap(true);  //自动换行，进制水平滚动
        scrollSend = new JScrollPane(chatSend);
        scrollSend.setBounds(250,385,300,70);
        panelBack.add(scrollSend);

        //初始化发送按钮
        sendButton.setBounds(570,400,70,40);
        sendButton.setBorder(BorderFactory.createRaisedBevelBorder());
        panelBack.add(sendButton);

        //初始化群聊操作输入框&加入群聊、创建群聊，退出群聊按钮
        groupOperate.setBounds(50,470,170,30);
        enterGroupButton.setBounds(250,470,90,30);
        enterGroupButton.setBorder(BorderFactory.createRaisedBevelBorder());
        quitGroupButton.setBounds(350,470,90,30);
        quitGroupButton.setBorder(BorderFactory.createRaisedBevelBorder());
        createGroupButton.setBounds(450,470,90,30);
        createGroupButton.setBorder(BorderFactory.createRaisedBevelBorder());
        panelBack.add(groupOperate);
        panelBack.add(enterGroupButton);
        panelBack.add(quitGroupButton);
        panelBack.add(createGroupButton);

        //初始化在线聊天框
        this.setBounds(350,180,712,555);
        this.setResizable(false);  //窗口大小不可变
        this.setVisible(true);
        //this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    //更新在线好友
    public void updateFriendsList (String [] friendsOnline)
    {
        //清空好友面板
        panelFriends.removeAll();

        friendsButton.clear();
        friendsStatus.clear();
        friendsSBack.clear();
        onlineFriendsIdList.clear();

        //ArrayList<String> friendsTemp = new ArrayList<>();
        for (String i : friendsOnline)
        {
            if (!i.equals(userId))
            {
                onlineFriendsIdList.add(i);
            }
        }

        for (int i = 0; i < onlineFriendsIdList.size(); i++)  //添加好友按钮
        {
            JButton tempFriend = new JButton(onlineFriendsIdList.get(i));
            tempFriend.setBounds(0,i * 30,110,30);
            friendsButton.add(tempFriend);
            panelFriends.add(tempFriend);

            JLabel tempStatus = new JLabel("在线");
            JPanel tempSBack = new JPanel();
            tempSBack.setBounds(110,i * 30 + 1, 40,28);
            tempSBack.setBackground(new Color(104, 184, 61));
            tempSBack.add(tempStatus);
            //tempSBack.repaint();
            friendsStatus.add(tempStatus);
            friendsSBack.add(tempSBack);
            panelFriends.add(tempSBack);
        }

        panelFriends.setLayout(null);
        for (JButton tempButton : friendsButton)
        {
            tempButton.addActionListener(qqView);
        }
        panelFriends.updateUI();
    }

    //更新离线好友（与更新在线好友配合使用，素质二连）
    public void updateRegistedFriendsList(ArrayList<String> allRegistedUsers)
    {
        //去除用户自身，存入friendsTemp
        ArrayList<String> friendsTemp = new ArrayList<>();
        for (String i : allRegistedUsers)
        {
            if (!i.equals(userId))
            {
                friendsTemp.add(i);
            }
        }
        //在离线列表添加离线好友
        int j = friendsButton.size();
        int k = 0;
        while (j < friendsTemp.size())  //添加好友按钮
        {
            while (k < friendsTemp.size())  //用索引k遍历未在线但已注册的用户
            {
                if (onlineFriendsIdList.contains(friendsTemp.get(k)))
                {
                    k++;
                }
                else
                {
                    break;
                }
            }
            JButton tempFriend = new JButton(friendsTemp.get(k));
            tempFriend.setBounds(0,j * 30,110,30);
            tempFriend.addActionListener(qqView);
            //tempFriend.setBorder(BorderFactory.createLoweredBevelBorder());
            friendsButton.add(tempFriend);
            panelFriends.add(tempFriend);

            JLabel tempStatus = new JLabel("离线");
            JPanel tempSBack = new JPanel();
            tempSBack.setBounds(110,j * 30 + 1, 40,28);
            tempSBack.setBackground(new Color(207, 215, 203, 137));
            tempSBack.add(tempStatus);
            //tempSBack.repaint();
            friendsStatus.add(tempStatus);
            friendsSBack.add(tempSBack);
            panelFriends.add(tempSBack);
            k++;
            j++;
        }
        panelFriends.setLayout(null);
        panelFriends.updateUI();

        //更新聊天记录框
        ArrayList<String> temptRecordId = new ArrayList<>(); //存储当前拥有的聊天框
        for (ChatFrame cf : recordsOfChat)
        {
            temptRecordId.add(cf.getChatId());
        }
        for (String id : friendsTemp)
        {
            //添加属性聊天框列表中没有的新聊天框
            if (!temptRecordId.contains(id))
            {
                recordsOfChat.add(new ChatFrame(id));
            }
        }
    }

    //更新显示的私聊聊天框
    public void updateChatFrame(String chatId)
    {
        panelBack.remove(scrollChat);
        //System.out.println("删处了原记录");
        JTextArea tempTextArea = new JTextArea();
        for (ChatFrame cf : recordsOfChat)
        {
            if (cf.getChatId().equals(chatId))
            {
                tempTextArea = cf.getChatRecord();
                scrollChat = cf.getScroll();
                //更新当前显示的聊天框
                displayingRecord = cf.getChatRecord();
                break;
            }
        }
        //System.out.println(tempTextArea.getText());
        panelBack.add(scrollChat);
        scrollChat.updateUI();
        //panelBack.updateUI();
    }

    //获取目标用户聊天框
    public JTextArea getTargetChatTextArea(String targetId)
    {
        for (ChatFrame cf : recordsOfChat)
        {
            if (cf.getChatId().equals(targetId))
            {
                return cf.getChatRecord();
            }
        }
        return new JTextArea("聊天框获取错误");
    }

    //更新全部的群聊列表
    public void updateGroupsList(ArrayList<String> groupsList)
    {
        //清空好友面板
        panelGroups.removeAll();

        groupsButton.clear();
        //groupsStatus.clear();
        //groupsSBack.clear();
        myGroupsIdList.clear();

        //ArrayList<String> friendsTemp = new ArrayList<>();
        myGroupsIdList.addAll(groupsList);

        for (int i = 0; i < myGroupsIdList.size(); i++)  //添加好友按钮
        {
            JButton tempGroup = new JButton(myGroupsIdList.get(i));
            tempGroup.setBounds(0,i * 30,153,30);
            groupsButton.add(tempGroup);
            panelGroups.add(tempGroup);

//            JLabel tempStatus = new JLabel("未加入");
//            JPanel tempSBack = new JPanel();
//            tempSBack.setBounds(110,i * 30 + 1, 40,28);
//            tempSBack.setBackground(new Color(104, 184, 61));
//            tempSBack.add(tempStatus);
            //tempSBack.repaint();

//            groupsStatus.add(tempStatus);
//            groupsSBack.add(tempSBack);
//            panelFriends.add(tempSBack);
        }

        panelGroups.setLayout(null);
        for (JButton tempButton : groupsButton)
        {
            tempButton.addActionListener(qqView);
        }
        panelGroups.updateUI();

        //更新聊天记录框
        ArrayList<String> temptRecordId = new ArrayList<>(); //存储当前拥有的聊天框
        for (GroupFrame cf : recordsOfGroup)
        {
            temptRecordId.add(cf.getGroupName());
        }
        for (String id : myGroupsIdList)
        {
            //添加属性聊天框列表中没有的新聊天框
            if (!temptRecordId.contains(id))
            {
                recordsOfGroup.add(new GroupFrame(id));
            }
        }
    }

    //更新显示的群聊聊天框
    public void updateGroupFrame(String groupName)
    {
        panelBack.remove(scrollChat);
        //System.out.println("删处了原记录");
        JTextArea tempTextArea = new JTextArea();
        for (GroupFrame cf : recordsOfGroup)
        {
            if (cf.getGroupName().equals(groupName))
            {
                tempTextArea = cf.getGroupRecord();
                scrollChat = cf.getScroll();
                //更新当前显示的聊天框
                displayingRecord = cf.getGroupRecord();
                break;
            }
        }
        //System.out.println(tempTextArea.getText());
        panelBack.add(scrollChat);
        scrollChat.updateUI();
        //panelBack.updateUI();
    }

    //获取目标群聊聊天框
    public JTextArea getTargetGroupTextArea(String targetId)
    {
        for (GroupFrame cf : recordsOfGroup)
        {
            if (cf.getGroupName().equals(targetId))
            {
                return cf.getGroupRecord();
            }
        }
        return new JTextArea("聊天框获取错误");
    }


}
