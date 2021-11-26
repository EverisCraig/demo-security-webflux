package com.craig.pe.demosecuritywebflux.configuration;

import com.craig.pe.demosecuritywebflux.handler.AuthHandler;
import com.craig.pe.demosecuritywebflux.handler.UserHandler;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterConfiguration {

    @Autowired
    private  AuthHandler authHandler;

    @Bean
    public RouterFunction<ServerResponse> routes(UserHandler userHandler) {
        return route(GET("/user"), userHandler::listUser)
                .andRoute(GET("/user/{userId}"), userHandler::getUserById)
                .andRoute(POST("/user"), userHandler::createUser)
                .andRoute(PUT("/user"), userHandler::createUser)
                .andRoute(DELETE("/user/{userId}"), userHandler::deleteUser);


    }

    @Bean
    public  RouterFunction<ServerResponse> authRoute(){
        return
                route(POST("/auth/login").and(accept(APPLICATION_JSON)), authHandler::login)
                .andRoute(POST("/auth/signup").and(accept(APPLICATION_JSON)), authHandler::signup);
    }
}
