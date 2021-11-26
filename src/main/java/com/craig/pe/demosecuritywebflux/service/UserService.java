package com.craig.pe.demosecuritywebflux.service;

import com.craig.pe.demosecuritywebflux.documents.User;
import com.craig.pe.demosecuritywebflux.exception.UserNotFoundException;
import com.craig.pe.demosecuritywebflux.repository.IRepository;
import com.craig.pe.demosecuritywebflux.repository.IUserRepository;
import com.craig.pe.demosecuritywebflux.utils.UserMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService extends BaseService<User, String> implements IUserService {

    private final IUserRepository iUserRepository;
    private final UserMessage userMessage;

    public UserService(IUserRepository iUserRepository, UserMessage userMessage) {
        this.iUserRepository = iUserRepository;
        this.userMessage = userMessage;
    }


    @Override
    protected IRepository<User, String> getRepository() {
        return iUserRepository;
    }

    @Override
    public Mono<User> findByUsername(String username) {
        Mono<User> user = iUserRepository.findByUsername(username);
        return user.hasElement().flatMap(aBoolean -> {
            if (Boolean.TRUE.equals(aBoolean)) {
                return user;
            } else {
                return Mono.error(() -> new UserNotFoundException(userMessage.getLocalMessage("entity.client.notNamePresent")));
            }
        });
    }
}
