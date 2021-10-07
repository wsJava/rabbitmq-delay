package top.lvjp.rabbitmq.delay.sender;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import top.lvjp.rabbitmq.delay.level.DelayLevel;

import static top.lvjp.rabbitmq.delay.DelayRabbitmqConfig.DELAY_EXCHANGE;
import static top.lvjp.rabbitmq.delay.DelayRabbitmqConfig.getDelayQueueName;

/**
 * @author lvjp
 * @date 2021/8/7
 */
public class RabbitDelayMsgSender {

    private RabbitTemplate rabbitTemplate;

    public RabbitDelayMsgSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * @param routingKey 被死信exchange转发时的routingKey
     */
    public void sendDelayMessage(String routingKey, Object message, DelayLevel delayLevel) {
        String delayRoutingKey = getDelayQueueName(delayLevel) + "." + routingKey;
        rabbitTemplate.convertAndSend(DELAY_EXCHANGE, delayRoutingKey, message);
    }


    public void send(String routingKey, Message message, DelayLevel delayLevel) {
        String delayRoutingKey = getDelayQueueName(delayLevel) + "." + routingKey;
        rabbitTemplate.send(DELAY_EXCHANGE, delayRoutingKey, message);
    }
}
