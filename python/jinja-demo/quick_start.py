# -*- coding: utf-8 -*-
'''
@Time   :  2022-1月-17 15:54
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        jinja快速demo
'''

from jinja2 import Environment, FileSystemLoader

env = Environment(
    loader=FileSystemLoader("templates")
)