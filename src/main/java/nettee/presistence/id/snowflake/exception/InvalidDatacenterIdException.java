package nettee.presistence.id.snowflake.exception;

import static nettee.presistence.id.snowflake.SnowflakeConstants.SnowflakeDefault.MAX_DATACENTER_ID;

public class InvalidDatacenterIdException extends RuntimeException {

    private final long datacenterId;

    public InvalidDatacenterIdException(long datacenterId) {
        super("datacenter Id can't be greater than %d or less than 0".formatted(MAX_DATACENTER_ID
        , datacenterId));
        this.datacenterId = datacenterId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }
}
