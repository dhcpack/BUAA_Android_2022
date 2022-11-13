from selenium import webdriver
import time
import csv
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By


def drop_down():
    """执行页面滚动的操作"""  # javascript
    for x in range(1, 12, 2):  # 1 3 5 7 9 11 在你不断的下拉过程中, 页面高度也会变的
        time.sleep(1)
        j = x / 9  # 1/9  3/9  5/9  9/9
        js = 'document.documentElement.scrollTop = document.documentElement.scrollHeight * %f' % j
        driver.execute_script(js)  # 执行我们JS代码


key_world = input('请输入你想要获取商品数据: ')
f = open(f'京东{key_world}商品数据.csv', mode='a', encoding='utf-8', newline='')
csv_writer = csv.DictWriter(f, fieldnames=[
    '商品标题',
    '商品价格',
    '评论量',
    '店铺名字',
    '商品图片',
    '标签',
    '商品详情页',
])
csv_writer.writeheader()

# selenium 模拟人的行为 获取数据内容
# selenium 操控 谷歌驱动 然后 操控浏览器
service = Service(executable_path=r"C:\Users\hw\AppData\Local\Google\Chrome\chromedriver.exe")

driver = webdriver.Chrome(service=service)
# 此次路径为chrome.exe路径
driver.get('https://www.jd.com/')  # 访问一个网址 打开浏览器 打开网址

driver.find_element(By.CSS_SELECTOR, '#key').send_keys(key_world)  # 找到输入框标签
driver.find_element(By.CSS_SELECTOR, '.button').click()  # 找到搜索按钮 进行点击
time.sleep(1)
driver.find_element(By.CSS_SELECTOR, '#J_filter > div.f-line.top > div.f-sort > a:nth-child(2)').click()
time.sleep(3)


def get_shop_info():
    # 第一步 获取所有的li标签内容
    driver.implicitly_wait(10)
    lis = driver.find_elements(By.CSS_SELECTOR, '#J_goodsList ul li')  # 获取多个标签
    # 返回数据 列表 [] 列表里面的元素 <> 对象
    # print(len(lis))
    for li in lis:
        title = li.find_element(By.CSS_SELECTOR, '.p-name em').text.replace('\n', '')  # 商品标题 获取标签文本数据
        price = li.find_element(By.CSS_SELECTOR, '.p-price strong i').text  # 价格
        commit = li.find_element(By.CSS_SELECTOR, '.p-commit strong a').text  # 评论量
        shop_name = li.find_element(By.CSS_SELECTOR, '.p-shop a').text  # 店铺名字
        if not shop_name:
            shop_name = str(None)
        pic = li.find_element(By.CSS_SELECTOR, '.p-img img').get_attribute('src')  # '商品图片'
        if not pic:
            pic = str(li.find_element(By.CSS_SELECTOR, ".p-img img").get_attribute('data - lazy - img'))
            pic = 'https:' + pic
        if pic.count('None') != 0:
            continue
        href = li.find_element(By.CSS_SELECTOR, '.p-img a').get_attribute('href')  # 商品详情页
        icons = li.find_elements(By.CSS_SELECTOR, '.p-icons i')
        icon = ','.join([i.text for i in icons])  # 列表推导式  ','.join 以逗号把列表中的元素拼接成一个字符串数据
        dit = {
            '商品标题': title,
            '商品价格': price,
            '评论量': commit,
            '店铺名字': shop_name,
            '商品图片': pic,
            '标签': icon,
            '商品详情页': href,
        }

        csv_writer.writerow(dit)
        print(title, price, commit, href, icon, sep=' | ')


for page in range(1, 5):
    print(f'正在爬取第{page}页的数据内容')
    time.sleep(1)
    drop_down()
    get_shop_info()  # 下载数据
    driver.find_element(By.CSS_SELECTOR, '.pn-next').click()  # 点击下一页

driver.quit()  # 关闭浏览器
