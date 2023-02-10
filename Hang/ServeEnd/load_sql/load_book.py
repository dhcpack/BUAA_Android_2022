import random

import pandas as pd
from pymysql import Connection

#  建立链接
conn = Connection(host='43.143.166.142', user='android', password='F7MGfrpWSEjWCci2', port=3306, database='android')
# 获取游标
cur = conn.cursor()

df = pd.read_excel("四级乱序版.xls")
word_list = []
right_choice = {}
for index, row in df.iterrows():
    word_list.append([row["单词"] + "\n" + row["音标"], [row["释义"]]])
    right_choice[row["单词"] + "\n" + row["音标"]] = row["释义"]
for i in range(len(word_list) - 3):
    for j in range(i + 1, i + 4):
        word_list[i][1].append(word_list[j][1][0])
word_list[len(word_list) - 3][1].append(word_list[len(word_list) - 2][1][0])
word_list[len(word_list) - 3][1].append(word_list[len(word_list) - 1][1][0])
word_list[len(word_list) - 3][1].append(word_list[0][1][0])
word_list[len(word_list) - 2][1].append(word_list[len(word_list) - 1][1][0])
word_list[len(word_list) - 2][1].append(word_list[0][1][0])
word_list[len(word_list) - 2][1].append(word_list[1][1][0])
word_list[len(word_list) - 1][1].append(word_list[0][1][0])
word_list[len(word_list) - 1][1].append(word_list[1][1][0])
word_list[len(word_list) - 1][1].append(word_list[2][1][0])
for i in range(len(word_list)):
    random.shuffle(word_list[i][1])
for i in range(len(word_list)):
    word_list[i][1].append(chr(word_list[i][1].index(right_choice[word_list[i][0]]) + ord("A")))
for i in range(len(word_list)):
    word_list[i][1] = "%%%".join(word_list[i][1])
for i in range(len(word_list)):
    print(i, word_list[i])
    sql = "insert into user_ques (type, ques, ans2, book_id, nickname_id) values (%s,%s,%s,%s,%s)"
    cur.execute(sql, [2, word_list[i][0], word_list[i][1], 15, "admin"])

conn.commit()
cur.close()
conn.close()
