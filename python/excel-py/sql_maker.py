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


class TableDef:
    def __init__(self, table_name):
        self.table_name = table_name
        self.columns = []

    def add_column(self, column):
        self.columns.append(column)


class ColumnDef:
    def __init__(
        self,
        column_name,
        column_type,
        column_length,
        column_null,
        column_default,
        column_description,
        column_range,
        column_enum,
        under_supervision,
        in_general_ledger,
    ):
        self.column_name = column_name
        self.column_type = column_type
        self.column_length = column_length
        self.column_null = column_null
        self.column_default = column_default
        self.column_comment = column_description
        self.column_range = column_range
        self.column_enum = column_enum
        self.under_supervision = under_supervision
        self.in_general_ledger = in_general_ledger

    def __str__(self):
        return f"name:{self.column_name}\ntype:{self.column_type}\nlength:{self.column_length}\nnull:{self.column_null}\ndefault:{self.column_default}\ncomment:{self.column_comment}\nrange:{self.column_range}\nenum:{self.column_enum}\nunder_supervision:{self.under_supervision}\nin_general_ledger:{self.in_general_ledger}\n"
