package 多用户通信系统.QQClient.view;

import javax.swing.*;
import java.awt.*;

public class PopService extends JDialog
{
    public PopService(String hint)
    {
        //this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);   弹窗默认关闭时退出，此步多余
        this.setTitle("系统提示");
        Container container = this.getContentPane();
        //container.setBackground(new Color(232, 129, 218, 144));
        //container.setLayout(null);
        JTextArea content = new JTextArea(hint);
        content.setFont(new Font("宋体",Font.BOLD,16));
        //JLabel msg = new JLabel(hint);
        //msg.setFont(new Font("宋体",Font.BOLD,16));
        //msg.setHorizontalAlignment(JLabel.CENTER);
        container.add(content);

        this.setVisible(true);
        this.setBounds(550,350,300,200);
    }
}
