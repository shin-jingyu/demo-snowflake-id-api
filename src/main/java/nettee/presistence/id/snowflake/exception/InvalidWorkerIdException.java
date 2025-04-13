package nettee.presistence.id.snowflake.exception;

import static nettee.presistence.id.snowflake.SnowflakeConstants.SnowflakeDefault.MAX_WORKER_ID;

public class InvalidWorkerIdException extends RuntimeException {

    private final long workerId;

    public InvalidWorkerIdException(long workerId) {
        super("datacenter Id can't be greater than %d or less than 0".formatted(MAX_WORKER_ID
        , workerId));
        this.workerId = workerId;
    }

    public long getWorkerId() {
        return workerId;
    }
}
