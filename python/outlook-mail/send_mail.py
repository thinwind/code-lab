# -*- coding: utf-8 -*-
'''
@Time   :  2022-9月-21 09:47
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        发送邮件
'''

from datetime import time
from accessOutlookEmail import create_account, send_email
import os
import time,random

# send_email(account, 'TestSubject', 'TestBody', ['wshangyehua@cebvendor.com'])

def send_file(att_name, attachement, acc, to):
    """
    发送附件
    """
    with open(attachement, 'rb') as att:
        bdata = att.read()
        send_email(acc, att_name, att_name, [to], [(att_name, bdata)])


def send_all_files_in_directory(acc, to, dir):
    subs = os.listdir(dir)
    parent_path = os.path.abspath(dir)
    for sub in subs:
        if sub.startswith("."):
            continue
        sub_file = parent_path+"/"+sub
        if os.path.exists(sub_file) and os.path.isdir(sub_file):
            send_all_files_in_directory(acc, to, sub_file)
        else:
            # print(type(sub))   =====> <class 'str'>
            print("==========> sending " + sub + "...")
            send_file(sub, sub_file, acc, to)
            print("==========> " + sub + " sent.")
            time.sleep(random.randint(50,120))
    print("==========> All sent.")


def print_dir(dir):
    subs = os.listdir(dir)
    parent_path = os.path.abspath(dir)
    for sub in subs:
        print(sub)
        sub = parent_path+"/"+sub
        if os.path.exists(sub) and os.path.isdir(sub):
            print_dir(sub)
        


if __name__ == '__main__':
    base_dir = '/Users/shangyehua/Playground/Codeexamples/send'
    account = create_account('rubbish_tin@outlook.com', 'oo00001111')
    to = 'wshangyehua@cebvendor.com'
    send_all_files_in_directory(account, to, base_dir)
    # print_dir(base_dir)
    # send_file('test3','/Users/shangyehua/Downloads/vscode-920/test/test.zip', account, to)
