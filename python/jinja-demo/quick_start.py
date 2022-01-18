# -*- coding: utf-8 -*-
'''
@Time   :  2022-1月-17 15:54
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        jinja快速demo
'''

from jinja2 import Environment, FileSystemLoader
from pathlib import Path

env = Environment(
    loader=FileSystemLoader("templates")
)

output_path = Path(".") / "output"

def cups_json_render():
    template_name = "cups8583.json.jinja"
    template = env.get_template(template_name)
    ctx={
        "length_size":2,
        "length_size_included":"true",
        "header_enabled":"true",
        "header_key":"header",
        "header_length":46,
        "header_components":[
            ("Header Length","头长度",1,"b"),
            ("Header Flag and Version","头标识和版本号",1,"b"),
            ("Total Message Length","报文总长度",4,"n"),
            ("Destination Station ID","目的ID",11,"ans"),
            ("Source Station ID","源ID",11,"ans"),
            ("Reserved for Use","保留使用",3,"n"),
            ("Batch Number","批次号",1,"b"),
            ("Transaction Information","交易信息",8,"ans"),
            ("User Information","用户信息",1,"b"),
            ("Reject Code","拒绝码",5,"b")
        ]
    }
    total_length = 0
    for comp in ctx["header_components"]:
        total_length += comp[2]
    if not total_length == ctx["header_length"]:
        raise AssertionError
    result = template.generate(ctx)

    with open(output_path / "cups8583.json", "w",encoding="utf-8") as output_file:
        for str in result:
            output_file.write(str)
    
    
if __name__ == "__main__":
    cups_json_render()