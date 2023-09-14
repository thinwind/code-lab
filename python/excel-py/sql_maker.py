# -*- coding: utf-8 -*-
"""
@Time   :  2023-9月-12 19:23
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        读取表结构文档，生成sql语句
"""


from openpyxl import load_workbook


# 读取sheet
def read_sheet(file_name, sheet_name):
    wb = load_workbook(file_name)
    sheet = wb[sheet_name]
    return sheet

