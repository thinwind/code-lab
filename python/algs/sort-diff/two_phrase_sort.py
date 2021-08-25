# -*- coding: utf-8 -*-
'''
@Time   :  2021-8月-25 17:18
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        两段式文件排序，适合特别大的文件排序
'''

from pathlib import Path


def split_file(file, block_size):
    cnt = 1  # 文件数
    files = []  # 小文件目录
    cache_size = 200
    
    with open(file, 'rt', encoding='utf-8') as f:
        while True:
            lines = f.readlines(block_size)
            if not lines:
                break
            if sort:
                lines.sort()  # 排序
            files.append(save_file(lines, cnt))
            cnt += 1
        return files


def save_file(lines, cnt):
    path = Path(".") / "tmp"
    filepath = path / f"part.{cnt}"
    info = ''.join(lines)
    with open(filepath, "a+t", encoding="utf-8") as f:
        f.write(f"{info}")

    return filepath

def merge_file(files,file_name):
    lines = {}  # 用来记录每一路当前最小值
    fs = [open(_f) for _f in files]
    out = open(Path(".") / f"out/{file_name}.sorted", "a+")
    for n,f in zip(range(0,len(fs)),fs):
        line = f.readline()
        if line:
            lines[n] = line
    
    while lines:
        n,l = min(lines.items(), key=lambda x:x[1])
        out.write(l)
        next_line = fs[n].readline()
        if next_line:
            lines[n] = next_line
        else:
            del lines[n]
            
    out.close()
    for f in fs:
        f.close()
            
if __name__ == "__main__":
    # 每行单个数字
    file_path = Path(".") / "test.txt"
    block_size = 20000
    tmp_files = split_file(file_path,block_size)
    merge_file(tmp_files,"test.txt")