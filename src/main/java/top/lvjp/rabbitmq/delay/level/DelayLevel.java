package top.lvjp.rabbitmq.delay.level;

/**
 * 固定延时等级
 *
 * @author lvjp
 * @date 2021/10/5
 */
public interface DelayLevel {

    /**
     * @return 延时时间, 单位 ms
     */
    long getDelayTimeInMills();

    /**
     * @return 延时时间的简要描述, 用来创建队列名, 如 2m, 2h 这种
     */
    String getDesc();

}
