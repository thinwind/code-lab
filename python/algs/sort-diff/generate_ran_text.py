# -*- coding: utf-8 -*-
'''
@Time   :  2021-8月-24 11:08
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        生成随机字符串
'''

import random
import string
from pathlib import Path

samples = string.ascii_letters + string.digits

def create_ranfile(file_name,col_num,line_size):
    with open(Path(".")/f"{file_name}",'wt',encoding="ASCII") as file:
        for _ in range(line_size):
            file.write(''.join(random.sample(samples,col_num)))
            file.write("\n")


if __name__ == "__main__":
    # 每行单个数字
    create_ranfile("test.txt",16,100000)
    