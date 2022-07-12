# -*- coding: utf-8 -*-
'''
@Time   :  2022-1月-28 13:53
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        Java包重构
'''

from os import listdir
import os
from pathlib import Path
import shutil
import re

def repackage(base_dir,origin_package,target_package):
    origin_dirs = origin_package.split('.')
    source_dir = base_dir
    for d in origin_dirs:
        source_dir = source_dir / d
    target_dirs = target_package.split('.')
    target_dir = base_dir
    for d in target_dirs:
        target_dir = target_dir / d
    shutil.move(source_dir,target_dir)
    replace_dir_package(target_dir,origin_package,target_package)
    
def replace_dir_package(base_dir,origin_package,target_package):
    root_dir = str(base_dir.absolute())
    for f in listdir(base_dir):
        target = root_dir+"/"+f
        if os.path.isfile(target):
            replace_file_package(target,origin_package,target_package)
        else:
            replace_dir_package(base_dir / f,origin_package,target_package)
            
def replace_file_package(src_file,origin_package,target_package):
    shutil.move(src_file,src_file+".tmp")
    with open(src_file+".tmp",'rb') as fin,open(src_file,'wb') as fout:
        text = fin.read()
        new_line = text.replace(bytes(origin_package,encoding='ascii'),bytes(target_package,encoding='ascii'))
        fout.write(new_line)
    os.remove(src_file+".tmp")
        
if __name__ == "__main__":
    base_dir = Path("/Users/shangyehua/tmp/bluedream2/src/main/java")
    repackage(base_dir,"io.github.thinwind.bluedream","com.zdsyh.bluetears")