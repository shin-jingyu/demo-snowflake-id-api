package nettee.presistence.id.snowflake;

import nettee.presistence.id.snowflake.exception.ClockBackwardException;

import static nettee.presistence.id.snowflake.SnowflakeConstants.SnowflakeDefault.DATACENTER_ID_SHIFT;
import static nettee.presistence.id.snowflake.SnowflakeConstants.SnowflakeDefault.SEQUENCE_MASK;
import static nettee.presistence.id.snowflake.SnowflakeConstants.SnowflakeDefault.TIMESTAMP_LEFT_SHIFT;
import static nettee.presistence.id.snowflake.SnowflakeConstants.SnowflakeDefault.WORKER_ID_SHIFT;
import static nettee.presistence.id.snowflake.validator.SnowflakeConstructionValidator.validateDatacenterId;
import static nettee.presistence.id.snowflake.validator.SnowflakeConstructionValidator.validateWorkerId;

public final class Snowflake {
    private final long datacenterId;
    private final long workerId;
    private final long epoch;

    private long sequence;
    private long lastTimestamp = -1L;

    public Snowflake(SnowflakeProperties properties) {
        this(
                properties.datacenterId(),
                properties.workerId(),
                properties.epoch()
        );
    }

    public Snowflake(long datacenterId, long workerId, long epoch) {
        validateDatacenterId(datacenterId);
        validateWorkerId(workerId);

        this.datacenterId = datacenterId;
        this.workerId = workerId;
        this.epoch = epoch;
    }

    // synchronized: 동시성 제어를 엄격하게 할 때 사용
    public synchronized long nextId() {
        var timestamp = timeGen();

        if (timestamp < lastTimestamp) { // clock backward
            throw new ClockBackwardException(timestamp, lastTimestamp);
        }

        if (timestamp == lastTimestamp) {
            // 원래 값(오버플로): 0000 ... 0001 0000 0000 0000
            // 마스크:          0000 ... 0000 1111 1111 1111
            // 결괏값:          0000 ... 0000 0000 0000 0000
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) { // overflow
                // 다음 밀리초 기다리기
                timestamp = tilNextMillis(lastTimestamp);
            }
        }else{ // timestamp > lastTimestamp
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        long id;

        id = (timestamp - epoch) << TIMESTAMP_LEFT_SHIFT;
        id |= datacenterId << DATACENTER_ID_SHIFT;
        id |= workerId << WORKER_ID_SHIFT;
        id |= sequence;
//        return ((timestamp - epoch) << TIMESTAMP_LEFT_SHIFT) |
//                (datacenterId << DATACENTER_ID_SHIFT) |
//                (workerId << WORKER_ID_SHIFT) |
//                sequence;
        return id;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp){
            timestamp = timeGen();
        }
        return timestamp;
    }

    public long timeGen() {
        return System.currentTimeMillis();
    }
}
