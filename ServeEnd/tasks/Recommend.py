import numpy as np

from user.models import *


def calc_euclidean_distance(vector1, vector2):
    # 计算两向量的欧几里得距离
    vector1 = np.array(vector1)
    vector2 = np.array(vector2)
    res = np.linalg.norm(vector1 - vector2)
    return res


def calc_cosine_distance(vector1, vector2):
    # 计算两向量的余弦距离
    vector1 = np.array(vector1)
    vector2 = np.array(vector2)
    res = np.dot(vector1, vector2) / (np.linalg.norm(vector1) * (np.linalg.norm(vector2)))
    return res


def load_books_data():
    print("正在加载书籍数据...")
    books = Book.objects.all()
    book_details = {}
    for book in books:
        book_details[book.bookname] = {"bookname": book.bookname, "owner": book.nickname.nickname, "tag": book.tag}
    return book_details


if __name__ == '__main__':
    load_books_data()