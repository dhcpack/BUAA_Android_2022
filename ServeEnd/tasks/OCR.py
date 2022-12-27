# 导入easyocr
import easyocr

# 创建reader对象
reader = easyocr.Reader(['ch_sim', 'en'])


def OCR(path):
    # 读取图像
    path = "./static/" + path
    result = reader.readtext(path)
    res = ""
    for r in result:
        res += r[-2]
    return res


if __name__ == '__main__':
    print(OCR("hand(1).jpg"))
