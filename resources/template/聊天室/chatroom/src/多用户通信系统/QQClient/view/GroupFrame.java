package 多用户通信系统.QQClient.view;

import javax.swing.*;
import java.awt.*;

public class GroupFrame
{
    private String groupName;
    private JTextArea groupRecord = new JTextArea(16,39);
    private JScrollPane scroll;

    public GroupFrame(String groupName)
    {
        this.groupName = groupName;
        groupRecord.setText("**************** 群聊 " + groupName + " ****************");
        groupRecord.setLineWrap(true);  //自动换行，进制水平滚动
        groupRecord.setEditable(false);
        groupRecord.setFont(new Font("楷体",Font.PLAIN,16));
        scroll = new JScrollPane(groupRecord);
        scroll.setBounds(250,50, 400,310);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String chatId) {
        this.groupName = chatId;
    }

    public JTextArea getGroupRecord() {
        return groupRecord;
    }

    public void setGroupRecord(JTextArea chatRecord) {
        this.groupRecord = chatRecord;
    }

    public JScrollPane getScroll() {
        return scroll;
    }
}
