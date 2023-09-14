# -*- coding: utf-8 -*-
'''
@Time   :  2023-9月-14 16:44
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        数据域模型
'''


class TableDef:
    def __init__(self, table_name,table_title_zh, table_description):
        self.table_name = table_name
        self.table_title_zh = table_title_zh
        self.table_description = table_description
        self.columns = []

    def add_column(self, column):
        self.columns.append(column)
        
    def get_table_name(self):
        return self.table_name
    
    def get_table_def(self):
        pk=[]
        ddl = f"CREATE TABLE {self.get_table_name()} (\n"
        for column in self.columns:
            ddl += f"{column.get_column_def()},\n"
            if column.is_pk:
                pk.append(column.get_column_name())
        if len(pk)>0:
            ddl += f"PRIMARY KEY ({','.join(pk)})\n"
        ddl += ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci "
        ddl += f"COMMENT '{self.table_title_zh} {self.table_description}'"


class ColumnDef:
    def __init__(
        self,
        column_name,
        column_title_zh,
        is_pk,
        column_type,
        column_length,
        column_not_null,
        column_default,
        column_description,
        column_range,
        column_enum,
        under_supervision,
        in_general_ledger,
    ):
        self.column_name = column_name
        self.column_title_zh = column_title_zh
        self.is_pk = is_pk
        self.column_type = column_type
        self.column_length = column_length
        self.column_not_null = column_not_null
        self.column_default = column_default
        self.column_description = column_description
        self.column_range = column_range
        self.column_enum = column_enum
        self.under_supervision = under_supervision
        self.in_general_ledger = in_general_ledger

    def __str__(self):
        return f"name:{self.column_name}\ntype:{self.column_type}\nlength:{self.column_length}\nnull:{self.column_null}\ndefault:{self.column_default}\ncomment:{self.column_comment}\nrange:{self.column_range}\nenum:{self.column_enum}\nunder_supervision:{self.under_supervision}\nin_general_ledger:{self.in_general_ledger}\n"

    def __repr__(self):
        return self.__str__()

    def get_column_type(self):
        if not self.column_type:
            self.column_type = "varchar"
            self.column_length = 255

        if self.column_length:
            return f"{self.column_type}({self.column_length})"
        else:
            return self.column_type

    def get_column_null(self):
        if self.column_not_null:
            return "NOT NULL"
        else:
            return ""

    def get_column_default(self):
        if self.column_default:
            return f"DEFAULT {self.column_default}"
        else:
            return ""

    def get_column_comment(self):
        cmt = "COMMENT '"
        if self.column_title_zh:
            cmt += f"中文名: {self.column_title_zh},\n"
        if self.column_description:
            cmt += f"说明: {self.column_description},\n"
        if self.column_range:
            cmt += f"取值范围: {self.column_range},\n"
        if self.column_enum:
            cmt += f"码值: {self.column_enum},\n"
        if self.under_supervision:
            cmt += f"是否监管: {self.under_supervision},\n"
        if self.in_general_ledger:
            cmt += f"是否涉及总账: {self.in_general_ledger},\n"
        cmt += "'"
        return cmt
    
    def get_column_name(self):
        return self.column_name
    
    def get_column_def(self):
        return f"{self.get_column_name()} {self.get_column_type()} {self.get_column_null()} {self.get_column_default()} {self.get_column_comment()}"
    
