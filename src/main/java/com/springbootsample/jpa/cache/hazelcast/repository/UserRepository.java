package com.springbootsample.jpa.cache.hazelcast.repository;

import com.springbootsample.jpa.cache.hazelcast.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_EMAIL_CACHE = "usersByEmail";


    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneByEmailIgnoreCase(String email);

/*
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneByFirstName(String login);
*/

}
