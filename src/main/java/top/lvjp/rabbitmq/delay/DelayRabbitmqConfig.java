package top.lvjp.rabbitmq.delay;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.lvjp.rabbitmq.delay.level.DelayLevel;
import top.lvjp.rabbitmq.delay.level.DelayLevelEnum;
import top.lvjp.rabbitmq.delay.sender.RabbitDelayMsgSender;

import java.util.*;

/**
 * @author lvjp
 * @date 2021/8/7
 */
@Configuration
public class DelayRabbitmqConfig implements ApplicationContextAware, InitializingBean {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    private ApplicationContext applicationContext;

    private static final String DELAY_QUEUE_PREFIX = "common_delay_";

    public static final String DELAY_EXCHANGE = "common_delay_exchange";
    private static final String DELAY_EXCHANGE_DLX = "common_delay_exchange_dlx";



    @Bean
    @ConditionalOnMissingBean
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        return new RabbitAdmin(rabbitTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitDelayMsgSender rabbitDelayMsgSender(RabbitTemplate rabbitTemplate) {
        return new RabbitDelayMsgSender(rabbitTemplate);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        declareCommonDelayExchangeAndQueues();

        declareAndBindingBizQueues();
    }

    /**
     * ????????????exchange???queue, ?????????
     */
    private void declareCommonDelayExchangeAndQueues() {
        // ?????? topic exchange ????????????exchange, ????????????????????? exchange ???????????????????????? queue
        TopicExchange delayExchange = new TopicExchange(DELAY_EXCHANGE, true, false);
        rabbitAdmin.declareExchange(delayExchange);

        // ?????????????????? exchange, ????????????????????????????????????????????? queue
        TopicExchange dlxDelayExchange = new TopicExchange(DELAY_EXCHANGE_DLX, true, false);
        rabbitAdmin.declareExchange(dlxDelayExchange);

        // ?????????????????????????????????bean, ??? ?????????????????????
        Collection<DelayLevel> customDelayLevels = applicationContext.getBeansOfType(DelayLevel.class).values();
        List<DelayLevel> delayLevels = new ArrayList<DelayLevel>(Arrays.asList(DelayLevelEnum.values()));
        delayLevels.addAll(customDelayLevels);

        // ??????????????????????????? queue, ????????? delay_exchange
        for (DelayLevel level : delayLevels) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("x-message-ttl", level.getDelayTimeInMills());
            params.put("x-dead-letter-exchange", DELAY_EXCHANGE_DLX);       // ????????????exchange,  ???????????????routingKey
            params.put("x-queue-mode", "lazy");     // ?????????????????????, ????????????????????????????????????
            Queue queue = new Queue(getDelayQueueName(level), true, false, false, params);
            rabbitAdmin.declareQueue(queue);

            Binding binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE, DELAY_EXCHANGE, queue.getName() + ".#", Collections.<String, Object>emptyMap());
            rabbitAdmin.declareBinding(binding);
        }
    }

    /**
     * ??????????????????, ???????????????exchange
     */
    private void declareAndBindingBizQueues() {
        Collection<DelayConsumQueue> bizQueues = applicationContext.getBeansOfType(DelayConsumQueue.class).values();

        for (DelayConsumQueue queue : bizQueues) {
            rabbitAdmin.declareQueue(queue);

            String bindingRoutingKey = queue.getBindRoutingKey();
            if (bindingRoutingKey == null || bindingRoutingKey.isEmpty()) {
                bindingRoutingKey = queue.getName();
            }
            Binding binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE, DELAY_EXCHANGE_DLX, "*." + bindingRoutingKey, Collections.<String, Object>emptyMap());
            rabbitAdmin.declareBinding(binding);
        }
    }

    public static String getDelayQueueName(DelayLevel delayLevel) {
        return DELAY_QUEUE_PREFIX + delayLevel.getDesc();
    }

}
