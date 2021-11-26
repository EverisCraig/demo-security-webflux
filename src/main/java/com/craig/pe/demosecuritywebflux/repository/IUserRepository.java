package com.craig.pe.demosecuritywebflux.repository;

import com.craig.pe.demosecuritywebflux.documents.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IUserRepository extends IRepository<User, String> {
    Mono<User>  findByUsername(String username);
}
