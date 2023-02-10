package 多用户通信系统.QQClient.view;

import javax.swing.*;
import java.awt.*;

public class ChatFrame
{
    private String chatId;
    private JTextArea record = new JTextArea(16,39);
    private JScrollPane scroll;

    public ChatFrame(String chatId)
    {
        this.chatId = chatId;
        record.setText("**************** 好友 " + chatId + " ****************");
        record.setLineWrap(true);  //自动换行，进制水平滚动
        record.setEditable(false);
        record.setFont(new Font("楷体",Font.PLAIN,16));
        scroll = new JScrollPane(record);
        scroll.setBounds(250,50, 400,310);
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public JTextArea getChatRecord() {
        return record;
    }

    public void setChatRecord(JTextArea chatRecord) {
        this.record = chatRecord;
    }

    public JScrollPane getScroll() {
        return scroll;
    }
}
