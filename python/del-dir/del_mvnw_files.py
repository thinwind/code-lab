# -*- coding: utf-8 -*-
'''
@Time   :  2020-Dec-28 11:24
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        删除mvn wrapper file
'''

import os
import shutil

target_dir = "/Users/shangyehua/Codeexamples/code-lab/java/spring-batch"
base_path = os.path.abspath(target_dir) 

def def_dir(dir):
    shutil.rmtree(dir)

def print_dir(dir):
    subs = os.listdir(dir)
    parent_path = os.path.abspath(dir)
    for sub in subs:
        if sub.startswith('.mvn') or sub.startswith("mvnw"):
            print(sub)
        sub = parent_path+"/"+sub
        if os.path.exists(sub) and os.path.isdir(sub):
            print_dir(sub)
            
def rm_mvnw_files(dir):
    subs = os.listdir(dir)
    parent_path = os.path.abspath(dir)
    for sub in subs:
        sub_path = parent_path+"/"+sub
        if sub.startswith('.mvn'):
            shutil.rmtree(sub_path)
        elif sub.startswith("mvnw"):
            os.remove(sub_path)
        else :
            if os.path.isdir(sub_path):
                rm_mvnw_files(sub_path)
            

if __name__ == "__main__":
    rm_mvnw_files(base_path)