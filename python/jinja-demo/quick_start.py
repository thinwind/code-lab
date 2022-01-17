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

def cups_json_render():
    template_name = "cups8583.json.jinja"
    template = env.get_template(template_name)
    ctx={
        "length_size":4,
        "length_size_included":"true",
        "header_enabled":"true",
        "header_key":"header",
        "header_length":46,
        "header_components":[
            ("Header Length","头长度",1,"b"),
            ("Header Flag and Version","头标识和版本号",1,"b"),
            ("Total Message Length","报文总长度",4,"n"),
        ]
    }
    result = template.render(ctx)
    print(result)
    
    
if __name__ == "__main__":
    cups_json_render()