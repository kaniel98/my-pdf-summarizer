package com.personal.pdfsummarizer.user;

import com.personal.pdfsummarizer.user.models.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserEntity, String> {
    Mono<UserEntity> save(UserEntity userEntity);
}
