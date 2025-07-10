package com.my_shop.apigateway.src.config;

import auth.AuthServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub authStub;

    @Bean
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub() {
        return this.authStub;
    }
}
