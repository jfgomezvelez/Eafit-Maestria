package co.edu.eafit.mongodb;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

import static org.springframework.data.domain.Example.of;

public abstract class AdapterOperations<D, I, R extends ReactiveCrudRepository<D, I> & ReactiveQueryByExampleExecutor<D>> {

    protected R repository;
    protected ObjectMapper mapper;
    private final Class<D> dataClass;

    @SuppressWarnings("unchecked")
    public AdapterOperations(R repository) {
        this.repository = repository;
        this.mapper = mapper;
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.dataClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
    }

    public Mono<D> save(D entity) {
        return Mono.just(entity)
                .flatMap(this::saveData);
    }

    protected Mono<D> saveData(D data) {
        return repository.save(data);
    }

}
