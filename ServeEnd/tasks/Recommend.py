import difflib
import logging
import math

import numpy as np
from sklearn.cluster import MiniBatchKMeans

from user.models import *

logging.basicConfig(level=logging.INFO, datefmt='%Y/%m/%d %H:%M:%S',
                    format='%(levelname)s - %(filename)s:%(lineno)d:%(funcName)s - %(message)s')
logger = logging.getLogger(__name__)


def string_similar(s1, s2):
    return difflib.SequenceMatcher(None, s1, s2).quick_ratio()


def calc_euclidean_distance(vector1, vector2):
    # 计算两向量的欧几里得距离
    print(vector1, vector2)
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


TOP_TAGS = list()
TOP_TAGS_INDEXS = {}
BOOK_DETAILS = {}
USER_DETAILS = {}
USER_CLUSTERS = {}


def process_book_data():
    '''
    对图书数据进行处理
    原格式： [bookid]::[bookname]::[tags]
    处理后： {
        bookid: {
            "bookname": bookname,
            "tags": tags
        }
        ...
    }
    '''
    print("正在加载书籍数据...")
    books = Book.objects.all()
    global TOP_TAGS, TOP_TAGS_INDEXS
    for book in books:
        TOP_TAGS.append(book.tag)
    TOP_TAGS = list(set(TOP_TAGS))
    TOP_TAGS_INDEXS = {i: TOP_TAGS.index(i) for i in TOP_TAGS}
    book_details = {}
    for book in books:
        book_details[book.id] = {"bookname": book.bookname, "tags": [book.tag]}
    global BOOK_DETAILS
    BOOK_DETAILS = book_details
    return book_details


def load_user_data():
    print("正在加载用户数据...")
    users = User.objects.all()
    user_rating = []
    for user in users:
        user_books = Book.objects.filter(nickname=user)
        for book in user_books:  # 对拥有的记忆本评分10分
            user_rating.append([user.nickname, book.id, 10])
        user_institute = user.institute
        all_books = Book.objects.all()
        for book in all_books:
            user_rating.append([user.nickname, book.id, 8 * string_similar(user_institute, book.bookname)])
        user_target = user.target
        for book in all_books:
            user_rating.append([user.nickname, book.id, 8 * string_similar(user_target, book.tag)])
    return user_rating


def process_user_data():
    '''
    对用户评分数据进行处理，feature 为用户特征向量，由读过的书籍计算得出
    原格式： [userid]::[bookid]::[score]
    处理后： {
        userid: {
            "review": [(bookid1, score1), (bookid2, score2), ... ],
            "feature": {feature1:weight1, feature2:weight2, ...}
        }
        ...
    }
    '''
    print("正在处理用户数据...")
    data = load_user_data()
    bookcount = {}
    users_dict = {}

    for userid, bookid, score in data:
        try:
            bookcount[userid] += 1
        except:
            bookcount[userid] = 0

    for userid, bookid, score in data:
        try:
            users_dict[userid]['review'].append((bookid, score))
        except:
            users_dict[userid] = {'review': [(bookid, score)]}

    top_tags = set(TOP_TAGS)
    books_dict = BOOK_DETAILS
    # users_dict = load_user_data()
    usercnt = 0
    for userid in users_dict:
        dic = {}
        bookcnt = 0
        for bookid, score in users_dict[userid]['review']:
            # if bookid not in books_dict:
            #     continue
            book_tags = set(books_dict[bookid]['tags'])

            bookcnt += 1
            tags = book_tags & top_tags

            for t in tags:
                try:
                    dic[t] += 1
                except:
                    dic[t] = 1

        dic = {i: math.sqrt(dic[i] / bookcnt) for i in dic}
        coefficient = math.sqrt(sum([dic[i] ** 2 for i in dic]))  # 归一化，使向量模长为1
        dic = {i: dic[i] / coefficient for i in dic}

        users_dict[userid]['feature'] = dic
        usercnt += 1
        if usercnt % 5 == 0:
            logger.info("processed {}/{}".format(usercnt, len(users_dict)))
    global USER_DETAILS
    USER_DETAILS = users_dict
    return users_dict


def dic2vec(features):
    res = [0] * len(TOP_TAGS_INDEXS)
    for i in features:
        if i in TOP_TAGS_INDEXS:
            res[TOP_TAGS_INDEXS[i]] = features[i]
    return res


def tags2vec(dic):
    coefficient = math.sqrt(sum([dic[i] ** 2 for i in dic]))  # 归一化，使向量模长为1
    dic = {i: dic[i] / coefficient for i in dic}

    v = np.array(dic2vec(dic))
    return v


def classify_user():
    '''
    运用 KMeans 算法对用户进行分组，每组取质心作为组特征向量，计算时先找到与用户特征向量近的用户组，降低计算量
    '''
    print("正在加载用户聚类数据...")
    users_dict = USER_DETAILS
    CLUSTER_NUMBER = 3
    u = list(users_dict.keys())
    X = np.array([dic2vec(users_dict[i]['feature']) for i in u])
    y = MiniBatchKMeans(n_clusters=CLUSTER_NUMBER, random_state=0, verbose=True, max_no_improvement=100,
                        init_size=3 * CLUSTER_NUMBER).fit_predict(X)

    clusters = [{} for _ in range(CLUSTER_NUMBER)]
    for i in range(CLUSTER_NUMBER):
        users = [u[j] for j in range(len(y)) if y[j] == i]
        vects = [X[j] for j in range(len(y)) if y[j] == i]
        if len(vects) == 0:
            centroid = 0
        else:
            centroid = sum(vects) / len(vects)
        clusters[i]['users'] = users
        clusters[i]['centroid'] = centroid
    global USER_CLUSTERS
    USER_CLUSTERS = clusters
    return clusters


def recommend_by_feature(vector):
    print(vector)
    # 根据用户特征向量进行匹配
    TOP_CLUSTER_NUM = 5  # 选取最近邻群组数量
    TOP_USER_NUM = 5  # 相似用户的数量上限
    TOP_BOOK_NUM = 6  # 推荐书籍的数量上限

    users_dict = USER_DETAILS
    clusters = USER_CLUSTERS
    clu_dist = [(calc_euclidean_distance(vector, clusters[index]['centroid']), index) for index, item in enumerate(clusters)]
    clu_dist.sort()
    clu_dist = clu_dist[:TOP_CLUSTER_NUM]

    similar_users = []
    for _, i in clu_dist:
        clu = clusters[i]
        similar_users.extend(clu['users'])

    similar_users = [(calc_euclidean_distance(vector, dic2vec(users_dict[item]['feature'])), item) for index, item in
                     enumerate(similar_users)]
    similar_users.sort()
    similar_users = similar_users[:TOP_USER_NUM]

    similar_users = [i for i in similar_users if i[0] > 0.5][:20]
    # print(similar_users[:100])
    # for s,n in similar_users[:100][::-1]:
    #     print((n, s))

    recommend_books = []
    for k, u in similar_users:
        books = [(int(score) * k, bookid) for (bookid, score) in users_dict[u]['review']]
        recommend_books.extend(books)

    d = {}
    for score, bookid in recommend_books:
        if bookid in d:
            d[bookid] += score
        else:
            d[bookid] = score

    recommend_books = list(d.items())
    recommend_books.sort(reverse=True, key=lambda x: x[1])
    recommend_books = recommend_books[:100]

    return recommend_books, similar_users


def recommend(profile):
    process_book_data()
    process_user_data()
    classify_user()
    top_tags = set(TOP_TAGS)
    books_dict = BOOK_DETAILS
    dic = {}
    bookcnt = 0
    for bookname in profile['books']:
        if bookname not in books_dict:
            continue
        book_tags = set(books_dict[bookname]['tags'])
        bookcnt += 1
        tags = book_tags & top_tags
        for t in tags:
            try:
                dic[t] += 1
            except:
                dic[t] = 1

    for t in profile['tags']:
        try:
            dic[t] += (bookcnt // 2) if bookcnt >= 2 else 1
        except:
            dic[t] = (bookcnt // 2) if bookcnt >= 2 else 1

    dic = {i: math.sqrt(dic[i] / (bookcnt if bookcnt > 0 else 1)) for i in dic}
    coefficient = math.sqrt(sum([dic[i] ** 2 for i in dic]))  # 归一化，使向量模长为1
    dic = {i: dic[i] / coefficient for i in dic}
    vector = tags2vec(dic)
    print("vector", end=":")
    print(vector)
    return recommend_by_feature(vector)


def recom(user: User):
    profile = {}
    profile['tags'] = [user.target, user.institute]
    profile['books'] = [book.bookname for book in Book.objects.filter(nickname=user)]
    recommend_books, similar_users = recommend(profile)
    books_dict = BOOK_DETAILS
    users_dict = USER_DETAILS
    res = []
    # 显示推荐内容，格式为 [推荐指数, bookname, booktags]
    for bookid, val in recommend_books:
        if bookid not in books_dict:
            continue
        item = books_dict[bookid]
        if item['bookname'] in profile['books']:
            continue
        res.append(item['bookname'])
    return res


def recommendTask():
    users = User.objects.all()
    for user in users:
        res = recom(user)
        user.recommends = "/".join(res)
        user.save()
        print(user.nickname, end=":")
        print(res)


if __name__ == '__main__':
    print(1)
