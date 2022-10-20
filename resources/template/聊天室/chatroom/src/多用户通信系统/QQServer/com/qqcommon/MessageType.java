package 多用户通信系统.QQServer.com.qqcommon;

public interface MessageType
{
    //不同常量的值表示不同消息类型
    String MESSAGE_LOGIN_SUCCEED="1";//表示登录成功
    String MESSAGE_LOGIN_FAIL="2";//表示登录失败
    String MESSAGE_COMM_MES="3";//普通信息包
    String MESSAGE_GET_ONLINEFRIEND="4";//要求返回在线用户列表
    String MESSAGE_RET_ONLINEFRIEND="5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT="6";//客户端请求退出
    String MESSAGE_CLIENT_NOTFOUND="7";//用户不合法
    String MESSAGE_CLIENT_SENDSUCCEED="8";//成功送信
    String MESSAGE_TOALL_MES="9";//群发申请
    String MESSAGE_FILE_MES="10";//文件消息
    String MESSAGE_OFFLINE_MES="11";//离线消息的获取申请

    String MESSAGE_REG_APPLY="12";
    String MESSAGE_REG_SUCCEED="13";
    String MESSAGE_REG_FAILED="14";

    String MESSAGE_OFFLINE_GET="15";//已经写入离线消息包的message
    String MESSAGE_GROUP_CREATE="16";
    String MESSAGE_GROUP_LIST="17";//展示聊天室列表申请

    String MESSAGE_GROUP_ENTER="18";//加入群聊的申请信息
    String MESSAGE_GROUP_ENTER_SUCCEED="19";//加入成功
    String MESSAGE_GROUP_ENTER_FAILED="20";//加入失败->目前是因为已经在群聊中
    String MESSAGE_GROUP_MES="21";     //群聊信息
    String MESSAGE_GROUP_MEMBERNOTFOUND="22";  //不在群聊中
    String MESSAGE_OTHER_LOGIN="23";   //其他用户登录信息
    String MESSAGE_GET_ACCOUNTS="24";  //获取全部注册用户列表
    String MESSAGE_GET_MYGROUPS="25";  //获取自己所在的全部群聊
    String MESSAGE_QUIT_GROUP="26";    //退出群聊
    String MESSAGE_QUITGROUP_FAIL="27";//退出群聊失败





}
