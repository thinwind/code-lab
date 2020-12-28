# -*- coding: utf-8 -*-
'''
@Time   :  2020-Dec-28 11:24
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        删除$开头的目录
'''

import os
import shutil

target_dir = "/Users/shangyehua/.m2/"
base_path = os.path.abspath(target_dir) 

def def_dir(dir):
    shutil.rmtree(dir)

def print_dir(dir):
    subs = os.listdir(dir)
    parent_path = os.path.abspath(dir)
    for sub in subs:
        if sub.startswith('$'):
            print(sub)
        sub = parent_path+"/"+sub
        if os.path.exists(sub) and os.path.isdir(sub):
            print_dir(sub)
            
def rm_doller_dir(dir):
    subs = os.listdir(dir)
    parent_path = os.path.abspath(dir)
    for sub in subs:
        sub_path = parent_path+"/"+sub
        if sub.startswith('$'):
            if os.path.isdir(sub_path):
                shutil.rmtree(sub_path)
                # print(sub_path)
            else:
                os.remove(sub_path)
                # print(sub_path)
        else:
            if os.path.isdir(sub_path):
                rm_doller_dir(sub_path)

if __name__ == "__main__":
    print_dir(base_path)