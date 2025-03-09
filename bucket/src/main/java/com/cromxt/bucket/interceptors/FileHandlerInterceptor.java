package com.cromxt.bucket.interceptors;

import com.cromxt.bucket.auth.BucketAuthorization;
import com.cromxt.proto.files.FileMetadata;
import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.cromxt.common.crombucket.grpc.MediaHeadersKey.FILE_METADATA;


@Slf4j
@Component
@RequiredArgsConstructor
public class FileHandlerInterceptor implements ServerInterceptor {

    private final BucketAuthorization bucketAuthorization;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        FileMetadata metaData = null;
        Metadata.Key<?> mediaMetaDatakey = FILE_METADATA.getMetaDataKey();

        if (headers.containsKey(mediaMetaDatakey)) {

            try {
                byte[] metaDataBytes = headers.get((Metadata.Key<byte[]>) mediaMetaDatakey);
                metaData = FileMetadata.parseFrom(metaDataBytes);
                String secret = metaData.getClientSecret();

                boolean authorized = bucketAuthorization.isRequestAuthorized(secret);

                if (!authorized) {
                    call.close(Status.UNAUTHENTICATED.withDescription("Request is not authorized"), headers);
                    return next.startCall(call, headers);
                }

                String clientId = bucketAuthorization.extractClientId(secret);

                FileMetadata updatedMetaData = FileMetadata.newBuilder()
                        .setExtension(metaData.getExtension())
                        .setVisibility(metaData.getVisibility())
                        .setClientId(clientId)
                        .build();

//                    Create a context for current call.
                Context currentContext = Context.current().withValue(FILE_METADATA.getContextKey(), updatedMetaData);
//                    Add the created Context to current request.
                return Contexts.interceptCall(currentContext, call, headers, next);
            } catch (Exception e) {
                log.error("Unable to parse media meta data", e);
                call.close(Status.INTERNAL.withDescription("Unable to parse media meta data"), headers);
            }

        }

        return next.startCall(call, headers);
    }
}
