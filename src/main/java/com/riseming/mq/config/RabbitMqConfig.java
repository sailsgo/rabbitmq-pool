package com.riseming.mq.config;

import com.rabbitmq.client.Address;
import com.riseming.mq.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.UnknownFormatConversionException;


/**
 * <p> 类描述：
 * <p> 创建人: mingjianyong
 */
@Component
public class RabbitMqConfig {
    /**
     * @desc 用户名
     */
    @Value("${rabbitmq.username:guest}")
    private String username;
    /**
     * @desc 密码
     */
    @Value("${rabbitmq.password:guest}")
    private String password;
    /**
     * @desc 连接串
     */
    private Address[] address;
    /**
     * @desc vhost
     */
    @Value("${rabbitmq.vhost:/}")
    private String vhost;
    /**
     * @desc 对象总数
     */
    @Value("${rabbitmq.maxTotal:8}")
    private Integer maxTotal;
    /**
     * @desc 最大空闲对象数
     */
    @Value("${rabbitmq.maxIdle:8}")
    private Integer maxIdle;
    /**
     * @desc 最小空闲对象书
     */
    @Value("${rabbitmq.minIdle:0}")
    private Integer minIdle;
    /**
     * @desc 获取超时时间
     */
    @Value("${rabbitmq.maxWaitMillis:1000}")
    private Integer maxWaitMillis;

    @Autowired
    private Environment environment;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address[] getAddress() {
        return address;
    }

    public void setAddress(Address[] address) {
        this.address = address;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(Integer maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    /**
     * 解析配置参数
     */
    @PostConstruct
    public void initAddress() {
        String connetionString = environment.getProperty("rabbitmq.connectionString", "127.0.0.1:5672");
        if (StringUtils.isEmpty(connetionString)) {
            throw new UnknownFormatConversionException("非法的配置参数");
        }
        String[] arrays = connetionString.split(Constant.COMMA);
        this.address = new Address[arrays.length];
        for (int i = 0; i < arrays.length; i++) {
            String[] addressArray = arrays[i].split(":");
            address[i] = new Address(addressArray[0], Integer.valueOf(addressArray[1]));
        }
    }
}
