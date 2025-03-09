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
@Configuration
@RequiredArgsConstructor
public class GrpcServerConfig {
    private final FileHandlerInterceptor fileHandlerInterceptor;
    private final FileHandlerGRPCServiceImpl fileHandlerGRPCService;


    @Bean(name = "bucketGrpcServerBuilder")
    @Order(1)
    public NettyServerBuilder configureServer(BucketInformationService bucketInformationService) throws IOException {
        Integer grpcPort = bucketInformationService.getRpcPort();
        String grpcHost = bucketInformationService.getApplicationHostname();
        assert grpcPort != null;
        assert grpcHost != null;
        log.info("Create gRPC server at {} on port {}", grpcHost, grpcPort);
        return NettyServerBuilder
                .forAddress(new InetSocketAddress(grpcHost, grpcPort))
                .addService(ServerInterceptors.intercept(fileHandlerGRPCService,fileHandlerInterceptor));
    }


    @Bean
    @Profile({"dev","prod"})
    @Primary
    public NettyServerBuilder addFileManagementService(FileManagementGRPCService fileManagementGRPCService,
                                                       FileManagerInterceptor fileManagerInterceptor,
                                                       @Qualifier("bucketGrpcServerBuilder") NettyServerBuilder server) {
        log.info("Add file management service");
        return server.addService(ServerInterceptors.intercept(fileManagementGRPCService,fileManagerInterceptor));
    }

    @Bean(name = "bucketGrpcServer")
    public Server createServer(NettyServerBuilder server) {
        log.info("Build server successfully");
        return server.build();
    }



}
