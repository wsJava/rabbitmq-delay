package top.lvjp.rabbitmq.delay;

import org.springframework.amqp.core.Queue;

import java.util.Map;

/**
 * 延时后被投递的业务队列, 提供给业务方消费
 * @author lvjp
 * @date 2021/8/7
 */
public class DelayConsumQueue extends Queue {

    /**
     * 与死信exchange 绑定的 routing key.  默认为 queueName
     */
    private String bindRoutingKey;

    public DelayConsumQueue(String name) {
        super(name);
    }

    public DelayConsumQueue(String name, boolean durable) {
        super(name, durable);
    }

    public DelayConsumQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        super(name, durable, exclusive, autoDelete);
    }

    public DelayConsumQueue(String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
        super(name, durable, exclusive, autoDelete, arguments);
    }

    public String getBindRoutingKey() {
        return bindRoutingKey;
    }

    public void setBindRoutingKey(String bindRoutingKey) {
        this.bindRoutingKey = bindRoutingKey;
    }
}
