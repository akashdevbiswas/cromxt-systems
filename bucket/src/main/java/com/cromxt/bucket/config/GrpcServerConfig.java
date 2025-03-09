package com.cromxt.bucket.config;


import com.cromxt.bucket.interceptors.FileHandlerInterceptor;
import com.cromxt.bucket.interceptors.FileManagerInterceptor;
import com.cromxt.bucket.service.impl.BucketInformationService;
import com.cromxt.bucket.service.impl.FileHandlerGRPCServiceImpl;
import com.cromxt.bucket.service.impl.FileManagementGRPCService;
import io.grpc.Server;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.netty.NettyServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
@Configuration(enforceUniqueMethods = false)
@RequiredArgsConstructor
public class GrpcServerConfig {

    private final BucketInformationService bucketInformationService;
    private final FileHandlerGRPCServiceImpl fileHandlerGRPCService;
    private final FileHandlerInterceptor fileHandlerInterceptor;

    @Bean(name = "grpcServer")
    @Profile("local")
    public Server configureServer() {
        Integer grpcPort = bucketInformationService.getRpcPort();
        String grpcHost = bucketInformationService.getApplicationHostname();

        log.info("Create gRPC server at {} on port {}", grpcHost, grpcPort);
        return NettyServerBuilder
                .forAddress(new InetSocketAddress(grpcHost, grpcPort))
                .addService(fileHandlerGRPCService)
                .intercept(fileHandlerInterceptor)
                .build();
    }

    @Bean(name = "grpcServer")
    @Profile({"dev", "prod"})
    public Server configureServer(
            FileManagementGRPCService fileManagementGRPCService,
            FileManagerInterceptor fileManagerInterceptor
    ) {
        Integer grpcPort = bucketInformationService.getRpcPort();
        String grpcHost = bucketInformationService.getApplicationHostname();

        log.info("Create gRPC server at {} on port {}", grpcHost, grpcPort);
        return NettyServerBuilder
                .forAddress(new InetSocketAddress(grpcHost, grpcPort))
                .addService(ServerInterceptors.intercept(fileHandlerGRPCService, fileHandlerInterceptor))
                .addService(ServerInterceptors.intercept(fileManagementGRPCService, fileManagerInterceptor))
                .build();
    }
}
