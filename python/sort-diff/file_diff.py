# -*- coding: utf-8 -*-
'''
@Time   :  2021-8月-24 11:38
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        找到文件不同
'''

from pathlib import Path

def find_diff(left,right):
    with open(left,'rt') as left_file,open(right,'rt') as right_file,\
        open(Path(".") / f"out/{left}.only",'wt') as left_only,open(Path(".") / f"out/{right}.only",'wt') as right_only:
        left_line = left_file.readline()
        right_line = right_file.readline()
        while True:
            #判断是否结束
            if not left_line:
                # 将右侧写出
                while right_line:
                    right_only.write(right_line)
                    right_line = right_file.readline()
                return
            if not right_line:
                while left_line:
                    left_only.write(left_line)
                    left_line = left_file.readline()
                return
            
            if left_line == right_line or left_line.strip() == right_line.strip():
                left_line = left_file.readline()
                right_line = right_file.readline()
            elif left_line < right_line:
                left_only.write(left_line)
                left_line = left_file.readline()
            else:
                right_only.write(right_line)
                right_line = right_file.readline()
                

            
if __name__ == "__main__":
    left = Path(".") / "left.txt"
    right = Path(".") / "right.txt"
    find_diff(left,right)