#!/bin/sh

# base info
base_dir=/home/sany/tmp/del_file
log_file="$base_dir/file_opt.log"

touch $log_file

# delete ok files
cur_time=`date "+%Y-%m-%d %H:%M:%S"`
for ok_file in `ls $base_dir | grep txt.ok`
do 
    echo "[$cur_time] del $ok_file" >> $log_file
done