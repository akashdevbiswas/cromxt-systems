package com.crombucket.storageservice.config;


import com.crombucket.storageservice.interceptors.FileHandlerInterceptor;
import com.crombucket.storageservice.interceptors.FileManagerInterceptor;
import com.crombucket.storageservice.service.impl.StorageServerDetails;
import com.crombucket.storageservice.service.impl.FileHandlerGRPCServiceImpl;
import com.crombucket.storageservice.service.impl.FileManagementGRPCService;
import io.grpc.Server;
import io.grpc.ServerInterceptors;
import io.grpc.netty.NettyServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.InetSocketAddress;

@Slf4j
@Configuration(enforceUniqueMethods = false)
@RequiredArgsConstructor
public class GrpcServerConfig {

    private final StorageServerDetails storageServerDetails;
    private final FileHandlerGRPCServiceImpl fileHandlerGRPCService;
    private final FileHandlerInterceptor fileHandlerInterceptor;

    @Bean(name = "grpcServer")
    @Profile({"local","local-docker","local-docker-dev"})
    public Server configureServer() {
        Integer grpcPort = storageServerDetails.getRpcPort();
        String grpcHost = storageServerDetails.getApplicationHostname();

        log.info("Create gRPC server at {} on port {}", grpcHost, grpcPort);
        return NettyServerBuilder
                .forAddress(new InetSocketAddress(grpcHost, grpcPort))
                .addService(fileHandlerGRPCService)
                .intercept(fileHandlerInterceptor)
                .build();
    }

    @Bean(name = "grpcServer")
    @Profile({"crombucket","crombucket-docker","crombucket-docker-dev"})
    public Server configureServer(
            FileManagementGRPCService fileManagementGRPCService,
            FileManagerInterceptor fileManagerInterceptor
    ) {
        Integer grpcPort = storageServerDetails.getRpcPort();
        String grpcHost = storageServerDetails.getApplicationHostname();

        log.info("Create gRPC server at {} on port {}", grpcHost, grpcPort);
        return NettyServerBuilder
                .forAddress(new InetSocketAddress(grpcHost, grpcPort))
                .addService(ServerInterceptors.intercept(fileHandlerGRPCService, fileHandlerInterceptor))
                .addService(ServerInterceptors.intercept(fileManagementGRPCService, fileManagerInterceptor))
                .build();
    }
}
