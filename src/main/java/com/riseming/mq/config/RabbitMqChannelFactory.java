package com.riseming.mq.config;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <p> 类描述：
 * <p> 创建人: mingjianyong
 */
@Component
public class RabbitMqChannelFactory extends BasePooledObjectFactory<Channel> {
    public static final Logger loggr = LoggerFactory.getLogger(RabbitMqChannelFactory.class);
    @Autowired
    private RabbitMqConfig config;
    private ConnectionFactory factory;
    private Connection conn;
    private int i = 0;
    @Override
    public PooledObject<Channel> makeObject() throws Exception {
        return super.makeObject();
    }

    @Override
    public void destroyObject(PooledObject<Channel> p) throws Exception {
        super.destroyObject(p);
    }

    @Override
    public boolean validateObject(PooledObject<Channel> p) {
        return p.getObject().isOpen();
    }


    @Override
    public void activateObject(PooledObject<Channel> p) throws Exception {
        super.activateObject(p);
    }

    /**
     * 使用完返还对象时
     * @param p
     * @throws Exception
     */
    @Override
    public void passivateObject(PooledObject<Channel> p) throws Exception {
        super.passivateObject(p);
    }

    /**
     * 创建一个新对象
     * @return
     * @throws Exception
     */
    @Override
    public Channel create() throws Exception {
        loggr.info("create channel:{}",i);
        Channel channel = conn.createChannel();
        channel.confirmSelect();
        i++;
        return channel;
    }
    /**
     * 封装为池化对象
     * @param channel
     * @throws Exception
     */
    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }
    @PostConstruct
    private void init(){
        factory = new ConnectionFactory();
        factory.setConnectionTimeout(5000);
        factory.setVirtualHost(config.getVhost());
        //自动重连
        factory.setAutomaticRecoveryEnabled(true);
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        try {
            conn = factory.newConnection(config.getAddress());
        } catch (Exception e) {
            loggr.error("rabbitmq create connection error", e);
            throw new RuntimeException("rabbitmq create connection error");
        }
    }
}
