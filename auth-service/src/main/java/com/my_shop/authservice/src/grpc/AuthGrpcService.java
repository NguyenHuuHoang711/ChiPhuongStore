package com.my_shop.authservice.src.grpc;

import com.myShop.grpc.AuthGrpcServiceGrpc;
import com.myShop.grpc.RegisterRequest;
import com.myShop.grpc.AuthResponse;
import com.my_shop.authservice.src.dto.MetaData;
import com.my_shop.authservice.src.service.AuthService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AuthGrpcService extends AuthGrpcServiceGrpc.AuthGrpcServiceImplBase {

    private final AuthService authService;

    @Override
    public void register(RegisterRequest request, StreamObserver<AuthResponse> responseObserver) {
        RegisterRequest req = RegisterRequest.newBuilder()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword())
                .build();
        com.my_shop.authservice.src.dto.RegisterRequest dtoReq = new com.my_shop.authservice.src.dto.RegisterRequest(
                req.getEmail(), req.getPassword()
        );

        MetaData meta = new MetaData();
        com.my_shop.authservice.src.dto.AuthResponse dtoRes = authService.register(dtoReq, meta);
        AuthResponse res = AuthResponse.newBuilder()
                .setAccessToken(dtoRes.getAccessToken())
                .setRefreshToken(dtoRes.getRefreshToken())
                .build();

        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }
}
