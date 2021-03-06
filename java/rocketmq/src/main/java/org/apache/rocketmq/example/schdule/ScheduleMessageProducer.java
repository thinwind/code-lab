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
package org.apache.rocketmq.example.schdule;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 *
 * TODO ScheduleMessageProducer说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-02  11:12
 *
 */
public class ScheduleMessageProducer {
    public static void main(String[] args)
            throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("ExampleProducerGroup");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        int totalMessageToSend = 100;
        for (int i = 0; i < totalMessageToSend; i++) {
            Message msg = new Message("TestTopic", ("Hello schduled message " + i).getBytes());
            msg.setDelayTimeLevel(2);
            producer.send(msg);
        }
        producer.shutdown();
    }
}
