package com.cromxt.bucket.config;


import com.cromxt.bucket.service.GRPCMediaService;
import com.cromxt.bucket.service.impl.BucketInformationService;
import io.grpc.Server;
import io.grpc.ServerInterceptor;
import io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
@Slf4j
@Configuration
public class GrpcServerConfig {

    @Bean
    public Server mediaHandlerGrpcServer(GRPCMediaService mediaHandlerGRPCService,
                                         BucketInformationService bucketInformationService,
                                         @Qualifier("customServerInterceptor")
                                         ServerInterceptor serverInterceptor) throws IOException {
        Integer grpcPort = bucketInformationService.getRpcPort();
        String grpcHost = bucketInformationService.getApplicationHostname();
        assert grpcPort != null;
        assert grpcHost != null;
        log.info("Create gRPC server at {} on port {}", grpcHost, grpcPort);
        return  NettyServerBuilder
                .forAddress(new InetSocketAddress(grpcHost,grpcPort))
                .addService(mediaHandlerGRPCService)
                .intercept(serverInterceptor)
                .build();
    }

}
