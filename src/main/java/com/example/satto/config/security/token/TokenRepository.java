package com.example.satto.config.security.token;

import com.example.satto.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from Token t inner join Users u on t.users.email = u.email
            where u.email = :email and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByUser(@Param("email") String email);

    Optional<Token> findByToken(String token);

    Optional<Token> findByUsersAndTokenType(Users user, TokenType tokenType);
}
