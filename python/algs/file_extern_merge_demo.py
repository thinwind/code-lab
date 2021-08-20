# -*- coding: utf-8 -*-
'''
@Time   :  2021-8月-19 17:28
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        文件外归并排序
'''

import os
from pathlib import Path


def nw_merge(files):
    fs = [open(file_) for file_ in files]
    min_map = {}  # 用来记录每一路当前最小值
    out = open(Path(".") / "out/integration.txt", "a+")
    for f in fs:
        read = f.readline()
        if read:
            min_map[f] = int(read.strip())

    while min_map:  # 将最小值取出，　并将该最小值所在的那一路做对应的更新
        min_ = min(min_map.items(), key=lambda x: x[1])
        min_f, min_v = min_
        out.write(f"{min_v}\n")
        nextline = min_f.readline()
        if nextline:
            min_map[min_f] = int(nextline.strip())
        else:
            del min_map[min_f]
    for f in fs:
        f.close()
    out.close()


def save_file(l, fileno):
    path = Path(".") / "split"
    filepath = path / f"{fileno}"
    info = '\n'.join(map(str, l))
    with open(filepath, "a+") as f:
        f.write(f"{info}")

    return filepath


def split_file(file_path, block_size):
    fileno = 1  # 文件数
    files = []  # 小文件目录
    with open(file_path, 'r') as f:
        while True:
            lines = f.readlines(block_size)
            if not lines:
                break
            lines = [int(i.strip()) for i in lines]  # 生成一个列表
            lines.sort()  # 排序
            files.append(save_file(lines, fileno))
            fileno += 1
        return files


if __name__ == "__main__":
    # 每行单个数字
    file_path = Path(".") / "tests.txt"
    block_size = 500 * 1024 * 1024 # 500K
    num_blocks = os.stat(file_path).st_size / block_size
    files = split_file(file_path, block_size)
    nw_merge(files)
