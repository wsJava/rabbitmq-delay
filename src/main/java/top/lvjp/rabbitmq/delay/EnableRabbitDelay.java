package top.lvjp.rabbitmq.delay;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lvjp
 * @date 2021/10/3
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({DelayRabbitmqConfig.class})
public @interface EnableRabbitDelay {
}
