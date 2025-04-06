package com.cromxt.storageserver.interceptors;


import com.cromxt.storageserver.auth.BucketAuthorizationBase;
import com.cromxt.proto.files.AuthKey;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.cromxt.common.crombucket.grpc.MediaHeadersKey.AUTH_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"crombucket","crombucket-docker","crombucket-docker-dev"})
public class FileManagerInterceptor implements ServerInterceptor {

    private final BucketAuthorizationBase bucketAuthorizationBase;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        Metadata.Key<?> metaDataKey = AUTH_KEY.getMetaDataKey();

        if (headers.containsKey(metaDataKey)) {
            try {
                byte[] metaDataBytes = headers.get((Metadata.Key<byte[]>) metaDataKey);
                AuthKey authKey = AuthKey.parseFrom(metaDataBytes);

                String key = authKey.getAuthKey();

                if (!key.equals(bucketAuthorizationBase.getApiKey())) {
                    call.close(Status.UNAUTHENTICATED.withDescription("Request is not authorized"), headers);
                }

            } catch (InvalidProtocolBufferException e) {
                call.close(Status.UNAUTHENTICATED.withDescription("Request is not authorized"), headers);
            }
        } else {
            call.close(Status.UNAUTHENTICATED.withDescription("Request is not authorized"), headers);
        }
        return next.startCall(call, headers);
    }
}
