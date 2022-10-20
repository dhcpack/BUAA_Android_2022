# BUAA_Android_202
### 任务分工

|                | 必做任务  | 选做任务 | 其他任务                                   |
| -------------- | --------- | -------- | ------------------------------------------ |
| 登录和个人页   | 1-3 17-20 | 6        |                                            |
| 知识点记忆模块 | 4-12      | 1-3      |                                            |
| 智能推荐模块   |           | 45       | 服务端接口，爬取网上记忆本（航概和四六级） |
| 朋友交流模块   | 13-17     | 7-12     |                                            |



### 数据库设计

用户表user_table

| 属性名    | 类型        |                                             |
| --------- | ----------- | ------------------------------------------- |
| nickname  | varchar(64) | 主键，不重复                                |
| password  | varchar(64) |                                             |
| sex       | bool        | 性别 true:男 false:女，默认值true           |
| pic       | varchar(64) | 头像路径，默认值userPic.png                 |
| days      | int         | 打卡天数，默认值0                           |
| target    | varchar(64) | 学习方向（也可以像level弄成int 选择）       |
| level     | int         | 小学生1 初中生2 高中生2 本科生3 研究生4 ... |
| stuId     | varchar(16) | 学号                                        |
| institute | varchar(64) | 学院                                        |
| major     | varchar(64) | 专业                                        |
| grade     | varchar(16) | 年级                                        |



打卡表 check_table

| 属性名   | 类型        |                                             |
| -------- | ----------- | ------------------------------------------- |
| id       | int         | 主键，自动生成                              |
| nickname | varchar(64) | user_table(nickname) 外键                   |
| time     | datetime    | 打卡时间 判断当日是否打卡完成，默认当前时刻 |



记忆本表 book_table

| 属性名   | 类型         |                            |
| -------- | ------------ | -------------------------- |
| id       | int          | 主键，自动生成             |
| bookname | varchar(512) | 记忆本名字，主键           |
| nickname | varchar(64)  | user_table(nickname) 外键  |
| pic      | varchar(64)  | 封面路径 默认值bookPic.png |
| tag      | varchar(64)  | 记忆本标签 默认值：未分类  |
| public   | bool         | 是否公开，默认不公开 false |



题目表 ques_table

| 属性名   | 类型          |                                                   |
| -------- | ------------- | ------------------------------------------------- |
| id       | int           | 主键，数据库自动生成                              |
| nickname | varchar(64)   | user_table(nickname) 外键                         |
| bookId   | int           | book_table(id) 外键                               |
| type     | int           | （答案类型）文字or图片or音频or视频（1， 2，3，4） |
| ques     | varchar(1024) | 问题 or 知识点描述                                |
| ans1     | varchar(1024) | 文字答案（type=1时不为空）                        |
| ans2     | varchar(64)   | 图片答案路径（type=2时不为空）                    |
| ans3     | varchar(64)   | 音频答案路径（type=3时不为空）                    |
| ans4     | varchar(64)   | 视频答案路径（type=4时不为空）                    |



用户学习记录表 learn_record_table

操作记忆本时生成记录

| 属性名   | 类型        |                           |
| -------- | ----------- | ------------------------- |
| id       | int         | 主键，数据库自动生成      |
| nickname | varchar(64) | user_table(nickname) 外键 |
| time     | timestamp   | 时间戳                    |
| bookId   | int         | 记忆本id                  |



朋友表 friends_table

(双向的)

| 属性名    | 类型        |                              |
| --------- | ----------- | ---------------------------- |
| id        | int         | 主键，数据库自动生成         |
| nickname1 | varchar(64) | user_table(nickname) 外键    |
| nickname2 | varchar(64) | user_table(nickname) 外键    |
| time      | timestamp   | 成为好友时间，默认为当前时刻 |



申请表 request_table

（单向的，同意后移动到friends_table中）(注意添加时receiver对应上表nickname1)

| 属性名   | 类型          |                                   |
| -------- | ------------- | --------------------------------- |
| id       | int           | 主键，数据库自动生成              |
| sender   | varchar(64)   | user_table(nickname) 外键  申请者 |
| receiver | varchar(64)   | user_table(nickname) 外键  被申请 |
| time     | timestamp     | 请求发出时间，默认为当前时刻      |
| msg      | varchar(1024) | 申请理由，默认为空                |



聊天记录表 chat_record_table（不知道有没有用）

（加为好友才能聊天，聊天前需要检查对方是否在自己的好友列表中）

| 属性名   | 类型          |                           |
| -------- | ------------- | ------------------------- |
| id       | int           | 主键，数据库自动生成      |
| sender   | varchar(64)   | user_table(nickname) 外键 |
| receiver | varchar(64)   | user_table(nickname) 外键 |
| time     | timestamp     | 时间，默认为当前时刻      |
| msg      | varchar(1024) | 内容                      |



帖子表 post_table

| 属性名   | 类型          |                          |
| -------- | ------------- | ------------------------ |
| id       | int           | 主键，数据库自动生成     |
| nickname | varchar(64)   | user_table(nickname)外键 |
| time     | time          | 发帖时间，默认为当前时刻 |
| favour   | int           | 点赞数，初始值为0        |
| title    | varchar(1024) | 题目，默认为Title        |
| content  | varchar(4096) | 内容，默认为content      |
| tag      | varchar(32)   | 标签，默认为未分类       |



评论表 comment_table

| 属性名   | 类型          |                                   |
| -------- | ------------- | --------------------------------- |
| id       | int           | 主键，数据库自动生成              |
| postId   | int           | post_table(id) 外键               |
| nickname | varchar(64)   | user_table(nickname) 外键  评论者 |
| time     | timestamp     | 时间，默认为当前时刻              |
| content  | varchar(1024) | 内容，默认为content               |



### 想到的新功能

可以展示根据学生注册选择的学院展示院徽



### 一些限制

用户名是主键 不能更改



### 可能用到的

图标库：[iconmonstr - Free simple icons for your next project](https://iconmonstr.com/)

图片网站：[Librestock Photos - Free Stock Photo Search Engine](https://librestock.com/)

模板：https://github.com/getActivity/AndroidProject
