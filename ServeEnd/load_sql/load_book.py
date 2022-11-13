import pandas as pd
from pymysql import Connection

#  建立链接
conn = Connection(host='43.143.166.142', user='android', password='F7MGfrpWSEjWCci2', port=3306, database='android')
# 获取游标
cur = conn.cursor()

df = pd.read_excel("level6-1.xls")
for index, row in df.iterrows():
    print(row["序号"], row["单词"], row["音标"], row["释义"])
    sql = "insert into user_ques (type, ques, ans1, book_id, nickname_id) values (%s,%s,%s,%s,%s)"
    cur.execute(sql, [1, row["单词"] + "\n" + row["音标"], row["释义"], 9, "admin"])

conn.commit()
cur.close()
conn.close()
