package 多用户通信系统.myJDBC;

import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class linkMySQL
{

    public static boolean addAccount(String id,String pwd) throws SQLException
    {
        if(checkTable("account",id))
        {
            System.out.println("用户"+id+"已存在");
            return false;
        }
        //在项目下创建1个文件夹 比如lib 将mysql.jar加入并添加到项目


        //1.注册驱动
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");    //"hzx1887415157"
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="insert into account values(?,?);";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.setString(1,id);
        preparedStatement.setString(2,pwd);
        //用于执行静态的sql语句，并返回其生成的结果的对象
        preparedStatement.executeUpdate();
       //返回值表示受影响的行数 返回零说明操作失败


        //5.关闭连接
        preparedStatement.close();
        connect.close();
        return true;
    }

    public static boolean checkTable(String tablename,String targetID) throws SQLException
    {
        //在项目下创建1个文件夹 比如lib 将mysql.jar加入并添加到项目


        //1.注册驱动
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="select * from "+tablename+";";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);

        //返回结果集
        ResultSet set=preparedStatement.executeQuery();
        //查找结果集
        while(set.next())
        {
            String id=set.getString("id");
            if(id.equals(targetID))
            {
                return true;
            }
        }
        preparedStatement.close();
        connect.close();
        return false;


    }

    public static String  checkPwd(String tablename,String targetID) throws SQLException
    {
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="select * from "+tablename+";";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);

        //返回结果集
        ResultSet set=preparedStatement.executeQuery();
        //查找结果集
        while(set.next())
        {
            String id=set.getString("id");
            if(id.equals(targetID))
            {
                return set.getString("password");
            }
        }
        preparedStatement.close();
        connect.close();
        return null;
    }

    public static void deleteMsg(String tablename,String colum,String value) throws SQLException
    {
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="delete from "+tablename+" where "+colum+"=?;";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.setString(1,value);

        //返回结果集
        preparedStatement.executeUpdate();
        //查找结果集
        preparedStatement.close();
        connect.close();

    }

    public static void addOffLineMsg(String id,String senderId,String msg) throws SQLException
    {
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="insert into offlinemsg values(?,?,?);";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.setString(1,id);
        preparedStatement.setString(2,senderId);
        preparedStatement.setString(3,msg);
        //用于执行静态的sql语句，并返回其生成的结果的对象
        preparedStatement.executeUpdate();
        //返回值表示受影响的行数 返回零说明操作失败


        //5.关闭连接
        preparedStatement.close();
        connect.close();

    }

    public static ArrayList<String>  getOffLineMsg(String id) throws SQLException
    {
        //1.注册驱动
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="select * from offlinemsg where id=?;";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.setString(1,id);
        //返回结果集

        ResultSet set=preparedStatement.executeQuery();
        ArrayList<String>offLineMsg=new ArrayList<String>();
        while(set.next())
        {
            String getter=set.getString("id");
            String senderId=set.getString("senderId");
            String message=set.getString("message");
            String s="离线时"+senderId+"对你说: "+message;
            offLineMsg.add(s);

        }
        preparedStatement.close();
        connect.close();
        return offLineMsg;
    }

    //群聊的相关操作
    //创建群聊->建表
    public static void createGroup(String tableName) throws SQLException {
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/group?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="create table "+tableName+"(id varchar(50));";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.executeUpdate();

        preparedStatement.close();
        connect.close();
    }
    //检查id是否在群聊中
    public static boolean checkMember(String tableName,String id) throws SQLException
    {
        boolean b=false;
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/group?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="select * from "+tableName+" where id=?;";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.setString(1,id);
        ResultSet set=preparedStatement.executeQuery();
        while(set.next())
        {
            String id1=set.getString("id");
            if(id1.equals(id))
            {
                b=true;
                break;
            }
        }


        preparedStatement.close();
        connect.close();
        return b;
    }
    //加入群聊操作
    public static boolean addMember(String tableName,String id) throws SQLException
    {
        //已在群聊中，不能添加
        if(checkMember(tableName,id))
        {
            return false;
        }
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/group?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="insert into "+tableName+" values(?);";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.setString(1,id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connect.close();

        String url1="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";
        //2.将用户名和密码放入到Properties对象
        Properties properties1=new Properties();
        properties1.setProperty("user","root");
        properties1.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect1=driver.connect(url1,properties1);
        //4.执行sql
        String sql1="insert into groupmember values(?,?);";
        PreparedStatement preparedStatement1=connect1.prepareStatement(sql1);
        preparedStatement1.setString(1,id);
        preparedStatement1.setString(2,tableName);
        preparedStatement1.executeUpdate();
        preparedStatement1.close();
        connect1.close();
        return true;
    }
    //将所有群聊名称以数组形式返回
    public static ArrayList<String> showTables() throws SQLException
    {
        ArrayList<String>tables=new ArrayList<String>();
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/group?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="show tables;";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        ResultSet set=preparedStatement.executeQuery();

        while(set.next())
        {
            String tableName=set.getString("Tables_in_group");
            tables.add(tableName);
        }

        preparedStatement.close();
        connect.close();
        return tables;
    }

    //获取全部注册用户列表
    public static ArrayList<String> showAccounts() throws SQLException
    {
        ArrayList<String>accounts=new ArrayList<String>();
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="select id from account";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        ResultSet set=preparedStatement.executeQuery();

        while(set.next())
        {
            String id=set.getString("id");
            accounts.add(id);
        }

        preparedStatement.close();
        connect.close();
        return accounts;
    }
    //获取用户所处的全部群聊
    public static ArrayList<String> showMyGroup(String id) throws SQLException
    {
        ArrayList<String>mygroup=new ArrayList<String>();
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="select groupName from groupmember where id="+"?";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.setString(1,id);
        ResultSet set=preparedStatement.executeQuery();

        while(set.next())
        {
            String groupName=set.getString("groupName");
            mygroup.add(groupName);
        }

        preparedStatement.close();
        connect.close();
        return mygroup;
    }

    public static boolean delMember(String tableName,String id) throws SQLException
    {
        //不在群聊中，不能退出
        if(!checkMember(tableName,id))
        {
            return false;
        }
        Driver driver=new com.mysql.cj.jdbc.Driver();
        //url代表需要连接的数据库 java到mysql底层走的是网络连接 本质就是网络通讯
        //解读 jdbc:mysql://是固定的 表示协议 即通过jdbc方式连接mysql
        //localhost 主机 也可以是ip地址
        //3306表示mysql监听端口
        //test表示连接到mysql的哪个数据库
        String url="jdbc:mysql://localhost:3306/group?useSSL=false&serverTimezone=UTC";

        //2.将用户名和密码放入到Properties对象
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect=driver.connect(url,properties);
        //4.执行sql
        String sql="delete from "+tableName+" where id= ?;";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.setString(1,id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connect.close();

        String url1="jdbc:mysql://localhost:3306/chatroom?useSSL=false&serverTimezone=UTC";
        //2.将用户名和密码放入到Properties对象
        Properties properties1=new Properties();
        properties1.setProperty("user","root");
        properties1.setProperty("password","Dsy20010907");
        //3.获取连接
        Connection connect1=driver.connect(url1,properties1);
        //4.执行sql
        String sql1="delete from groupmember where id=? and groupName=?";
        PreparedStatement preparedStatement1=connect1.prepareStatement(sql1);
        preparedStatement1.setString(1,id);
        preparedStatement1.setString(2,tableName);
        preparedStatement1.executeUpdate();
        preparedStatement1.close();
        connect1.close();
        return true;
    }




    public static void main(String[] args) throws SQLException
    {

        System.out.println(showTables());

    }


}
