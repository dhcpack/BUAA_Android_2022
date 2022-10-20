package 多用户通信系统.QQClient.view;

//import 多用户通信系统.GUI.SWING1.ImageIconDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ClientLoginGui extends JFrame
{
    private JPanel panel = new JPanel();
    private JLabel tit = new JLabel();
    private JLabel user = new JLabel("账号：");
    private JLabel pass = new JLabel("密码：");
    private JTextField userID = new JTextField(20);
    private JTextField pwd = new JTextField(20);
    private JButton login = new JButton("登录");
    private JButton regist = new JButton("注册");

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JLabel getTit() {
        return tit;
    }

    public void setTit(JLabel title) {
        this.tit = title;
    }

    public JLabel getUser() {
        return user;
    }

    public void setUser(JLabel user) {
        this.user = user;
    }

    public JLabel getPass() {
        return pass;
    }

    public void setPass(JLabel pass) {
        this.pass = pass;
    }

    public JTextField getUserID() {
        return userID;
    }

    public void setUserID(JTextField userID) {
        this.userID = userID;
    }

    public JTextField getPwd() {
        return pwd;
    }

    public void setPwd(JTextField pwd) {
        this.pwd = pwd;
    }

    public JButton getLogin() {
        return login;
    }

    public void setLogin(JButton login) {
        this.login = login;
    }

    public JButton getRegist() {
        return regist;
    }

    public void setRegist(JButton regist) {
        this.regist = regist;
    }

    public ClientLoginGui(){};

    public void init()
    {
        this.setTitle("登录聊天室");
        Container container = getContentPane();
        container.setLayout(null);   //取消布局

        //初始化画板，大小400x300
        panel.setLayout(null);
        panel.setBounds(0,0,400,260);
        panel.setBackground(new Color(148, 210, 231, 255));
        container.add(panel);

        //添加标题图片
        URL headerurl = ClientLoginGui.class.getResource("/statics/logo_2.png");
        ImageIcon header = new ImageIcon(headerurl);
        tit.setIcon(header);
        tit.setBounds(50,20,300,60);
        panel.add(tit);

        //添加账号密码信息
        user.setBounds(70,100, 50, 30);
        user.setFont(new Font("宋体",Font.BOLD,16));
        userID.setBounds(120,100,180,30);
        userID.setFont(new Font("宋体",Font.PLAIN,16));
        panel.add(user);
        panel.add(userID);
        pass.setBounds(70,150, 50, 30);
        pass.setFont(new Font("宋体",Font.BOLD,16));
        pwd.setBounds(120,150,180,30);
        pwd.setFont(new Font("宋体",Font.PLAIN,16));
        panel.add(pass);
        panel.add(pwd);

        //添加登录、注册按钮
        login.setBounds(90,200,100,30);
        login.setFont(new Font("楷体",Font.BOLD,16));
        login.setBorder(BorderFactory.createRaisedBevelBorder());
        regist.setBounds(210,200,100,30);
        regist.setFont(new Font("楷体",Font.BOLD,16));
        regist.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.add(login);
        panel.add(regist);

        //初始化登录界面框
        this.setBounds(500,300,410,295);
        this.setResizable(false);  //窗口大小不可变
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
