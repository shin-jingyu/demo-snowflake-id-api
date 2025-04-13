package nettee.presistence.id.snowflake.validator;

import nettee.presistence.id.snowflake.exception.InvalidDatacenterIdException;
import nettee.presistence.id.snowflake.exception.InvalidWorkerIdException;

import static nettee.presistence.id.snowflake.SnowflakeConstants.SnowflakeDefault.MAX_DATACENTER_ID;
import static nettee.presistence.id.snowflake.SnowflakeConstants.SnowflakeDefault.MAX_WORKER_ID;

public final class SnowflakeConstructionValidator {

    private SnowflakeConstructionValidator() {}

    public static void validateDatacenterId(long datacenterId) {
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new InvalidDatacenterIdException(datacenterId);
        }
    }

    public static void validateWorkerId(long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new InvalidWorkerIdException(workerId);
        }
    }
}
