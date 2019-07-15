package com.riseming.mq.config;

import com.rabbitmq.client.*;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * <p> 类描述：
 * <p> 创建人: mingjianyong
 */
@Service("rabbitMqService")
public class RabbitMqServiceImpl implements RabbitMqService{
    public static final Logger logger = LoggerFactory.getLogger(RabbitMqServiceImpl.class);
    private RabbitChannelPool rabbitChannelPool;
    @Autowired
    private RabbitMqChannelFactory rabbitMqChannelFactory;
    @Autowired
    private RabbitMqConfig mqConfig;
    public static final String UTF_8 = "utf-8";
    @PostConstruct
    public void initPool(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(mqConfig.getMinIdle());
        config.setMaxTotal(mqConfig.getMaxTotal());
        config.setMaxIdle(mqConfig.getMaxIdle());
        config.setMaxWaitMillis(2000);
        rabbitChannelPool = new RabbitChannelPool(rabbitMqChannelFactory, config);
    }

    @Override
    public void sendMessage() {

    }

    @Override
    public boolean sendMessage(String queueName, String exchange, String routingKey, String extype, String message) {
        Channel channel = null;
        try {
            channel = rabbitChannelPool.borrowObject();
            logger.info("active:{},Idle:{}",rabbitChannelPool.getNumActive(),rabbitChannelPool.getNumIdle());
            //声明队列(durable -> true,持久化，)
            channel.queueDeclare(queueName, false, false, false, null);
            //声明交换机(durable -> true,持久化，)
            channel.exchangeDeclare(exchange, extype,false);
            //队列绑定
            channel.queueBind(queueName, exchange, routingKey);
            channel.basicPublish(exchange, routingKey, MessageProperties.BASIC, message.getBytes(UTF_8) );
            return channel.waitForConfirms();
        } catch (Exception e) {
            logger.error("error", e);
            return false;
        }finally {
            if (channel != null) {
                rabbitChannelPool.returnObject(channel);
            }
        }
    }

    @Override
    public void startConsumer(MqCallService callback, String queueName, String routingKey, String exchange, String extype, boolean durable) {
        Channel channel = null;
        try {
            channel = rabbitChannelPool.borrowObject();
            channel.queueDeclare(queueName, durable, false, false, null);
            channel.queueBind(queueName, exchange, routingKey);
            final Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        String message = new String(body, UTF_8);
                        callback.call(message, null, 0);
                    } catch (Exception ex) {
                        logger.error("消费消息错误", ex);
                    }
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
