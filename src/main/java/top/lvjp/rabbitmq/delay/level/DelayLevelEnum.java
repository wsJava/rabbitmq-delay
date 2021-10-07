package top.lvjp.rabbitmq.delay.level;

import top.lvjp.rabbitmq.delay.level.DelayLevel;

/**
 * @author lvjp
 * @date 2021/8/7
 */
public enum DelayLevelEnum implements DelayLevel {

    SECOND_1(1000, "1s"),
    SECOND_5(5 * 1000, "5s"),
    SECOND_10(10 * 1000, "10s"),
    SECOND_30(30 * 1000, "30s"),
    MINUTE_1(60 * 1000, "1m"),
    MINUTE_2(2 * 60 * 1000, "2m"),
    MINUTE_3(3 * 60 * 1000, "3m"),
    MINUTE_4(4 * 60 * 1000, "4m"),
    MINUTE_5(5 * 60 * 1000, "5m"),
    MINUTE_6(6 * 60 * 1000, "6m"),
    MINUTE_7(7 * 60 * 1000, "7m"),
    MINUTE_8(8 * 60 * 1000, "8m"),
    MINUTE_9(9 * 60 * 1000, "9m"),
    MINUTE_10(10 * 60 * 1000, "10m"),
    MINUTE_20(20 * 60 * 1000, "20m"),
    MINUTE_30(30 * 60 * 1000, "30m"),
    HOUR_1(60 * 60 * 1000, "1h"),
    HOUR_2(2 * 60 * 60 * 1000, "2h"),
    ;

    private static final String DELAY_QUEUE_PREFIX = "common_delay_";

    private final long delayTimeInMills;
    private final String desc;

    DelayLevelEnum(long delayTimeInMills, String desc) {
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
