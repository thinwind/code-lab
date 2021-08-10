# -*- coding: utf-8 -*-
'''
@Time   :  2021-7月-22 08:47
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        删除eclipse生成的文件，清理java工作区
'''
import os
import shutil

target_files=['.classpath','.project','.settings','.vscode','.factorypath','.apt_generated_tests']

def clean_eclipse_file(wp):
    subs = os.listdir(wp)
    parent_path = os.path.abspath(wp)
    for sub in subs:
        sub_path = parent_path+"/"+sub
        if sub in target_files:
            if os.path.isdir(sub_path):
                shutil.rmtree(sub_path)
            else:
                os.remove(sub_path)
        else:
            if os.path.isdir(sub_path):
                clean_eclipse_file(sub_path)

if __name__ == '__main__':
    wp = '/Users/shangyehua/opensource-projects/rocketmq'
    clean_eclipse_file(wp)