# -*- coding: utf-8 -*-
"""
@Time   :  2023-4月-05 15:14
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        将webp转为jpg
"""

import os
from PIL import Image


# 图像预处理包括修改图像格式、编号
class WebpTransfer:
    def __init__(self, path):
        self.path = path

    # 读取文件夹下图像
    def read(self):
        filelist = os.listdir(self.path)
        self.webps = [f for f in filelist if f.endswith(".webp")]

    def transfer(self):
        # 查找图像方式不同，该函数只查找所有.webp格式的文件
        for item in self.webps:
            src = os.path.join(os.path.abspath(self.path), item)
            print("src=", src)
            im = Image.open(src)
            im.load()
            save_name = src.replace("webp", "jpg")
            im.save("{}".format(save_name), "JPEG")


if __name__ == "__main__":
    transfer = WebpTransfer("C:\\Users\\nices\\Downloads\\wallpaper")
    transfer.read()
    transfer.transfer()
    
