package com.craig.pe.demosecuritywebflux.handler;

import com.craig.pe.demosecuritywebflux.documents.User;
import com.craig.pe.demosecuritywebflux.dto.ApiResponse;
import com.craig.pe.demosecuritywebflux.dto.LoginRequest;
import com.craig.pe.demosecuritywebflux.dto.LoginResponse;
import com.craig.pe.demosecuritywebflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
@Component
public class AuthHandler {

    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TokenProvider tokenProvider;

    private final UserService userService;

    @Autowired
    public AuthHandler(TokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(loginRequest -> userService.findByUsername(loginRequest.getUsername())
                        .flatMap(user -> {
                            if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                                return ServerResponse.ok()
                                        .contentType(APPLICATION_JSON)
                                        .bodyValue(new LoginResponse(tokenProvider.generateToken(user)));
                            } else {
                                return ServerResponse.badRequest()
                                        .bodyValue(new ApiResponse(400, "Invalid credentials", null));
                            }
                        }).switchIfEmpty(ServerResponse.badRequest()
                                .bodyValue(new ApiResponse(400, "User does not exist", null))));
    }

    public Mono<ServerResponse> signup(ServerRequest request) {
        return request.bodyToMono(User.class)
                .map(user -> {
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                    return user;
                }).flatMap(o -> userService.findByUsername(o.getUsername())
                        .flatMap(user -> ServerResponse.badRequest().bodyValue(new ApiResponse(400, "User already exist", null)))
                        .switchIfEmpty(userService.create(o).flatMap(user -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(user))));
    }
}
