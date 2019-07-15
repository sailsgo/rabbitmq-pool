package com.riseming.mq.config;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * <p> 类描述：构建RabbitChannelPool
 * <p> 创建人: mingjianyong
 */
public class RabbitChannelPool extends GenericObjectPool<Channel> {

    public RabbitChannelPool(PooledObjectFactory<Channel> factory, GenericObjectPoolConfig<Channel> config) {
        super(factory, config);
    }
}

