package com.riseming.mq.config;

/**
 * <p> 类描述：
 * <p> 创建人: mingjianyong
 */
public interface RabbitMqService {
    void sendMessage();

    /**
     * 发送消息到MQ
     * @param queueName 队列名称
     * @param exchange 交换机
     * @param routingKey routingKey
     * @param extype 交换机类型
     * @param message 消息
     * @return true/false 成功与否
     */
    boolean sendMessage(String queueName, String exchange, String routingKey, String extype, String message);

    /**
     *  消费MQ消息
     * @param callback 回调处理方法
     * @param queueName 队列名称
     * @param routingKey routingKey
     * @param exchange 交换机
     * @param extype  交换机类型
     * @param durable 是否持久化
     */
    void startConsumer(MqCallService callback, String queueName, String routingKey, String exchange, String extype, boolean durable);
}
