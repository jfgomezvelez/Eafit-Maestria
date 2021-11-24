package co.edu.eafit.mongodb;

import org.springframework.stereotype.Repository;

@Repository
public class StatisticRepository extends AdapterOperations<ProcessData, String, MongoDBRepository>
{

    public StatisticRepository(MongoDBRepository repository) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository);
    }
}
