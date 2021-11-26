package com.craig.pe.demosecuritywebflux.service;

import com.craig.pe.demosecuritywebflux.documents.User;
import reactor.core.publisher.Mono;

public interface IUserService extends IBaseService<User, String> {
    Mono<User> findByUsername(String username);
}
