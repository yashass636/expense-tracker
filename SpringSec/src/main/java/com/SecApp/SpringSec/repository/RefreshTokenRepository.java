package com.SecApp.SpringSec.repository;

import com.SecApp.SpringSec.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    public Optional<RefreshToken> findByToken (String token);
}
