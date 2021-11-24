package co.edu.eafit.mongodb;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface MongoDBRepository extends ReactiveMongoRepository<ProcessData, String>, ReactiveQueryByExampleExecutor<ProcessData> {
}
