package com.gymcrm.gymcrm.Repositories;

import com.gymcrm.gymcrm.Entities.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository  extends CrudRepository<Token, Long> {
    @Query("SELECT t FROM Token t INNER JOIN t.user u WHERE u.username = :username AND t.isLoggedOut = false")
    List<Token> findAllTokensByUsername(@Param("username") String username);

    Optional<Token> findByToken(String Token);
}
