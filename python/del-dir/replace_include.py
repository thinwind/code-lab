# -*- coding: utf-8 -*-
'''
@Time   :  2021-May-01 20:29
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        把gradle的include层级改为project
        比如
        include "chapter-03/news-service"
        替换为
        include ":chapter-03-news-service"
        project(':chapter-03-news-service').projectDir = "chapter-03/news-service" as File
'''

import re
pattern = r'include\s+"(\S+)/(\S+)"'

def replaceSubDir(file):
    result = []
    with open(file,'rt') as src:
        for line in src:
            if line.strip():
                m = re.search(pattern,line)
                if m:
                    result.append('include ":{0}-{1}"'.format(m.group(1),m.group(2)))
                    result.append('project(\':{0}-{1}\').projectDir = "{2}/{3}" as File'.format(m.group(1),m.group(2),m.group(1),m.group(2)))
                else:
                    result.append(line)
    return "\n".join(result)
    
if __name__ == "__main__":
    file = "build.gradle"
    new_file=replaceSubDir(file)
    print(new_file)