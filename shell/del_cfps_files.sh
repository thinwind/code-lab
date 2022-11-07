#!/bin/sh
# file_name: del_cfps_files.sh
# 清理cfps生成的跟帐文件
# 实现思路：
# 1. 每天夜间11点执行，将当天的文件压缩
# 2. 删除除了当天压缩文件之外的所有文件

# base info
base_dir=/home/sany/tmp/del_file
log_file="$base_dir/file_op.log"

touch $log_file

cd $base_dir

# delete ok files
cur_time=`date "+%Y-%m-%d %H:%M:%S"`
for ok_file in `find . -type f -name "*.ok"`
do 
    rm -f $ok_file
    echo "[$cur_time] $ok_file removed" >> $log_file
done

cur_date_file=`date "+%Y_%m_%d"`.txt

# package the files created today
for file in `find . -type f -name "*$cur_date_file"`
do
    echo "[`date "+%Y-%m-%d %H:%M:%S"`] $file packaging..." >> $log_file
    tar -zcf $file.tar.gz $file
    echo "[`date "+%Y-%m-%d %H:%M:%S"`] $file packaged" >> $log_file
done

# remove all the files but tar files created today
for file in `find . -type f !  \( -name "*.gz" -o -name "*.log" \)`
do
    rm -f $file
    echo "[`date "+%Y-%m-%d %H:%M:%S"`] $file removed" >> $log_file
done