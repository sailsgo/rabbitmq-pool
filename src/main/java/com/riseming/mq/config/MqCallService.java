package com.riseming.mq.config;

import com.rabbitmq.client.Channel;

public interface MqCallService {
    /**
     * 回掉接口
     * @param message
     * @param channel
     * @param deliveryTag
     */
    void call(String message, Channel channel, long deliveryTag);
}
