package nettee.jpa.id.snowflake;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends CrudRepository<Sample, Long> {
}
