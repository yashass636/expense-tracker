package com.singhdevhub.userService.repository;

import com.singhdevhub.userService.entities.UserInfo;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories //enables JPARepository interface for the below class
public interface UserRepository extends CrudRepository<UserInfo, String>
{
    Optional<UserInfo> findByUserId(String userId);

}