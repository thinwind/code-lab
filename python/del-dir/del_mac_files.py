# -*- coding: utf-8 -*-
'''
@Time   :  2020-Dec-28 11:24
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        删除mac系统自动生成的文件
'''

import os
import shutil

target_dir = "C:\\Users\\Shangyh\\BookLib"
base_path = os.path.abspath(target_dir) 

def def_dir(dir):
    shutil.rmtree(dir)

def print_dir(dir):
    subs = os.listdir(dir)
    parent_path = os.path.abspath(dir)
    for sub in subs:
        if sub.startswith('.'):
            print(sub)
        sub = parent_path+"/"+sub
        if os.path.exists(sub) and os.path.isdir(sub):
            print_dir(sub)
            
def rm_mac_files(dir):
    subs = os.listdir(dir)
    parent_path = os.path.abspath(dir)
    for sub in subs:
        sub_path = parent_path+"/"+sub
        if sub.startswith('.'):
            if sub.startswith(".git"):
                # shutil.rmtree(sub_path)
                print("git://"+sub_path)
                # .git dir
                pass
            else:
                if os.path.isdir(sub_path):
                    shutil.rmtree(sub_path)
                else :
                    os.remove(sub_path)
                # print(sub_path)
        else:
            if os.path.isdir(sub_path):
                rm_mac_files(sub_path)

if __name__ == "__main__":
    print_dir(base_path)