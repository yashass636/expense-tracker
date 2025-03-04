package com.SecApp.SpringSec.repository;

import com.SecApp.SpringSec.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String> {

    public UserInfo findByUsername(String username);
}
