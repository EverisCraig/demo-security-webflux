package com.craig.pe.demosecuritywebflux.handler;

import com.craig.pe.demosecuritywebflux.documents.User;
import com.craig.pe.demosecuritywebflux.exception.UserNotFoundException;
import com.craig.pe.demosecuritywebflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class UserHandler {
    private final UserService userService;

    @Autowired
    public UserHandler(UserService userService) {
        this.userService = userService;
    }


    public Mono<ServerResponse> createUser(ServerRequest request) {
        Mono<User> user = request.bodyToMono(User.class)
                .flatMap(userService::create);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON).body(user, User.class);
    }

    public Mono<ServerResponse> listUser(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(userService.findAll(), User.class);
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        String userId = request.pathVariable("userId");
        String MSJ_ERROR_FIND_CLIENT = "CLIENT DOES NOT EXIST";
        return userService.findById(userId).flatMap(user -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(Mono.error(new UserNotFoundException(MSJ_ERROR_FIND_CLIENT)))
                .onErrorResume(throwable -> Mono.error(new UserNotFoundException(throwable.getMessage())));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String userId = request.pathVariable("userId");
        userService.delete(userId);
        return ServerResponse.ok().build();
    }
}
