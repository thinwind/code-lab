/*
 * Copyright 2021 Shang Yehua
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.rocketmq.example.batch;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.rocketmq.common.message.Message;

/**
 *
 * TODO MySplitter说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-02  16:20
 *
 */
public class MySplitter implements Iterator<List<Message>> {

    private long limit = 1024 * 1024 * 2;

    private List<Message> originList;

    private int curIdx;

    @Override
    public boolean hasNext() {
        return curIdx < originList.size();
    }

    @Override
    public List<Message> next() {
        int accSize = 0;
        int nxtIdx = curIdx;
        for (; nxtIdx < originList.size(); nxtIdx++) {
            int msgSize = getMsgSize(nxtIdx);
            if (msgSize > limit) {
                //如果单条消息大于限制，那么单条发送
                break;
            }
            accSize += msgSize;
            if (accSize > limit) {
                //找到了临界点
                break;
            }
        }
        if (nxtIdx == curIdx) {
            //单条超出限制
            nxtIdx++;
        }
        List<Message> result = originList.subList(curIdx, nxtIdx);
        curIdx = nxtIdx;
        return result;
    }

    private int getMsgSize(int nxtIdx) {
        Message message = originList.get(nxtIdx);
        int tmpSize = message.getTopic().length() + message.getBody().length;
        Map<String, String> properties = message.getProperties();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            tmpSize += entry.getKey().length() + entry.getValue().length();
        }
        tmpSize = tmpSize + 20; //for log overhead
        return tmpSize;
    }

    public MySplitter(List<Message> originList) {
        assert originList != null;
        this.originList = originList;
        curIdx = 0;
    }

    public MySplitter(long limit, List<Message> originList) {
        this(originList);
        this.limit = limit;
    }

}
