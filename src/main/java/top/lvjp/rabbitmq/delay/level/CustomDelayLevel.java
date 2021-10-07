package top.lvjp.rabbitmq.delay.level;


/**
 * 自定义延时时间的队列
 *
 * @author lvjp
 * @date 2021/10/4
 */
public class CustomDelayLevel implements DelayLevel {

    private final long delayTimeInMills;

    private final String desc;

    public CustomDelayLevel(long delayTimeInMills, String desc) {
        this.delayTimeInMills = delayTimeInMills;
        this.desc = desc;
    }

    @Override
    public long getDelayTimeInMills() {
        return delayTimeInMills;
    }

    @Override
    public String getDesc() {
        return desc;
    }

}
