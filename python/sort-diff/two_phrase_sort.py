# -*- coding: utf-8 -*-
'''
@Time   :  2021-8月-25 17:18
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        两段式文件排序，适合特别大的文件排序
'''

from pathlib import Path
import shutil

def init_tmp_dir(base_dir):
    '''
        初始化临时目录
        sort: 排序临时文件目录
            将分割后的临时文件进行
            排序生成的临时存放在此目录
        split: 分割临时文件目录
            将文件进行分割时，生成的
            临时文件存放在此目录
        merge: 合并临时文件目录
            排序后生成的排序文件存放在
            此目录
    '''
    tmp = Path(base_dir) / "tmp"
    if not tmp.exists():
        tmp.mkdir()
    tmp_sort = tmp / "sort"
    if not tmp_sort.exists():
        tmp_sort.mkdir()
    tmp_split = tmp / "split"
    if not tmp_split.exists():
        tmp_split.mkdir()
    merge_dir = Path(base_dir) / "merge"
    if not merge_dir.exists():
        merge_dir.mkdir()

def clean_tmp_files(base_dir):
    '''
        清理切割和排序生成的临时文件
    '''
    shutil.rmtree(Path(base_dir) / "tmp" / "sort")
    shutil.rmtree(Path(base_dir) / "tmp" / "split")
    

def split_file(file, block_size):
    cnt = 1  # 文件数
    files = []  # 小文件目录
    cache_size = 100 *1024 # 100kb
    readLines = 0
    out_file_path = Path(".") / "tmp"/"split" / f"{file}.split.{cnt}"
    out_file = open(out_file_path, 'wt')
    files.append(out_file_path)

    with open(file, 'rt', encoding='utf-8') as f:
        while True:
            if readLines >= block_size:
                out_file.close()
                cnt += 1
                readLines = 0
                new_file = Path(".") / "tmp"/"split" / f"{file}.split.{cnt}"
                out_file = open(new_file, 'wt')
                files.append(new_file)
            lines = f.readlines(cache_size)
            readLines += len(lines)
            if not lines:
                break
            out_file.writelines(lines)

        out_file.close()

        return files


def split_and_sort_file(file_path, block_size):
    cnt = 1  # 文件数
    files = []  # 小文件目录
    lines = []
    cache_size = 50
    cache_lines = None
    with open(file_path, 'rt', encoding='utf-8') as f:
        while True:
            cache_lines = f.readlines(cache_size)
            if not cache_lines:
                if lines:
                    lines.sort()
                    files.append(save_file(file_path.name,lines, cnt))
                break

            lines += cache_lines

            if len(lines) >= block_size:
                files.append(save_file(file_path.name,lines, cnt))
                cnt += 1
                lines = []
        return files

def save_file(file_name,lines,cnt):
    lines.sort()  # 排序
    part_file = Path(".") / "tmp"/"sort" /f"{file_name}.part.{cnt}"
    with open(part_file, "wt", encoding="utf-8") as f:
        f.writelines(lines)
    return part_file
    

def merge_file(files, file_name):
    lines = {}  # 用来记录每一路当前最小值
    fs = [open(_f) for _f in files]
    out = open(file_name, "wt")
    for n, f in zip(range(0, len(fs)), fs):
        line = f.readline()
        if line:
            lines[n] = line

    while lines:
        n, l = min(lines.items(), key=lambda x: x[1])
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
    file_path = Path(".") / "test.txt"
    tmp_sort = Path('.') / "tmp" / "sort"
    if not tmp_sort.exists():
        tmp_sort.mkdir()
    
    tmp_split = Path('.') / "tmp" / "split"

    if not tmp_split.exists():
        tmp_split.mkdir()

    tmp_merge = Path('.') / "tmp" / "merge"
    if not tmp_merge.exists():
        tmp_merge.mkdir()

    sub_files = split_file(file_path, 20000)

    sorted_subfiles = []
    for sub_file in sub_files:
        tmpfiles = split_and_sort_file(sub_file, 1000)
        sorted_subfile = Path(".") / "tmp" / "merge" / f"{sub_file.name}.merge"
        merge_file(tmpfiles, sorted_subfile)
        sorted_subfiles.append(sorted_subfile)
    sorted_file = Path(".") / "out" / f"{file_path}.sorted"
    merge_file(sorted_subfiles, sorted_file)
