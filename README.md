# BUAA_Android_2022
BUAA_Android_2022

|                | 必做任务  | 选做任务 | 其他任务                       |
| -------------- | --------- | -------- | ------------------------------ |
| 登录和个人页   | 1-3 17-20 | 6        |                                |
| 知识点记忆模块 | 4-12      | 1-3      |                                |
| 智能推荐模块   |           | 45       | 爬取网上记忆本（航概和四六级） |
| 朋友交流模块   | 13-17     | 7-12     |                                |



数据库设计

用户表user_table

| 属性名    | 类型        |                              |
| --------- | ----------- | ---------------------------- |
| id        | int         | 主键，数据库自动生成         |
| nick_name | varchar(64) | 不重复，通过nickname添加好友 |
| password  | varchar(64) |                              |
| pic       | varchar(64) | 头像路径                     |
| days      | int         | 打卡天数                     |



打卡表 check_table

| 属性名 | 类型     |                               |
| ------ | -------- | ----------------------------- |
| userId | int      | user_table(id) 外键           |
| time   | datetime | 打卡时间 判断当日是否打卡完成 |



记忆本表 book_table

| 属性名 | 类型        |                      |
| ------ | ----------- | -------------------- |
| id     | int         | 主键，数据库自动生成 |
| userId | int         | user_table(id) 外键  |
| pic    | varchar(64) | 封面路径             |
| tag    | varchar(64) | 记忆本标签           |
| public | bool        | 是否公开             |



题目表 ques_table

| 属性名 | 类型          |                                                   |
| ------ | ------------- | ------------------------------------------------- |
| id     | int           | 主键，数据库自动生成                              |
| bookId | int           | book_table(id) 外键                               |
| type   | int           | （答案类型）文字or图片or音频or视频（0，1， 2，3） |
| ques   | varchar(1024) | 问题 or 知识点描述                                |
| ans1   | varchar(1024) | 文字答案                                          |
| ans2   | varchar(64)   | 图片答案路径                                      |
| ans3   | varchar(64)   | 音频答案路径                                      |
| ans4   | varchar(64)   | 视频答案路径                                      |



用户学习记录表 learn_record_table

打开记忆本时生成一条记录

| 属性名 | 类型      |                      |
| ------ | --------- | -------------------- |
| id     | int       | 主键，数据库自动生成 |
| userId | int       | user_table(id) 外键  |
| time   | timestamp | 时间戳               |
| bookId | int       | 记忆本id             |



朋友表 friends_table

(双向的)

| 属性名  | 类型      |                      |
| ------- | --------- | -------------------- |
| id      | int       | 主键，数据库自动生成 |
| userId1 | int       | user_table(id) 外键  |
| userId2 | int       | user_table(id) 外键  |
| time    | timestamp | 成为好友时间         |



申请表 request_table

（单向的，同意后移动到friends_table中）

| 属性名  | 类型          |                             |
| ------- | ------------- | --------------------------- |
| id      | int           | 主键，数据库自动生成        |
| userId1 | int           | user_table(id) 外键  申请者 |
| userId2 | int           | user_table(id) 外键  被申请 |
| time    | timestamp     | 请求发出时间                |
| text    | varchar(1024) | 申请理由                    |



聊天记录表 chat_record_table（不知道有没有用）

（加为好友才能聊天）

| 属性名   | 类型          |                      |
| -------- | ------------- | -------------------- |
| id       | int           | 主键，数据库自动生成 |
| sender   | int           | user_table(id) 外键  |
| receiver | int           | user_table(id) 外键  |
| time     | timestamp     | 时间                 |
| text     | varchar(1024) | 内容                 |



帖子表 post_table

| 属性名  | 类型          |                      |
| ------- | ------------- | -------------------- |
| id      | int           | 主键，数据库自动生成 |
| userId  | int           | 作者                 |
| time    | time          | 发帖时间             |
| favour  | int           | 点赞数               |
| comment | int           | 评论数               |
| title   | varchar(128)  | 题目                 |
| content | varchar(4096) | 内容                 |



评论表 comment_table

| 属性名 | 类型          |                             |
| ------ | ------------- | --------------------------- |
| id     | int           | 主键，数据库自动生成        |
| postId | int           | post_table(id) 外键         |
| userId | int           | user_table(id) 外键  评论者 |
| time   | timestamp     | 时间                        |
| text   | varchar(1024) | 内容                        |





ORM框架：Room [使用 Room 将数据保存到本地数据库  | Android 开发者  | Android Developers](https://developer.android.com/training/data-storage/room#groovy)

可视化数据库：[(195条消息) Android Studio 打开 Room 可视化查看器（Database Inspector）_Eyeswap的博客-CSDN博客_android room 查看](https://blog.csdn.net/h20101988/article/details/124169208?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-1-124169208-blog-108011311.pc_relevant_layerdownloadsortv1&spm=1001.2101.3001.4242.2&utm_relevant_index=4)

服务器mysql数据库：

[Android数据库框架该如何选？ - 掘金 (juejin.cn)](https://juejin.cn/post/7020223144082276383)



图标库：[iconmonstr - Free simple icons for your next project](https://iconmonstr.com/)

模板：https://github.com/getActivity/AndroidProject
