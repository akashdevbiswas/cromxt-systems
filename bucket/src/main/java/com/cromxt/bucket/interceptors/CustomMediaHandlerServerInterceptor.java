package com.cromxt.bucket.interceptors;

import com.cromxt.bucket.auth.BucketAuthorization;
import com.cromxt.proto.files.MediaDetails;
import com.cromxt.proto.files.MediaHeaders;
import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.cromxt.common.crombucket.grpc.MediaHeadersKey.MEDIA_DETAILS;
import static com.cromxt.common.crombucket.grpc.MediaHeadersKey.MEDIA_META_DATA;


@Slf4j
@Service(value = "customServerInterceptor")
@RequiredArgsConstructor
public class CustomMediaHandlerServerInterceptor implements ServerInterceptor {

    private final BucketAuthorization bucketAuthorization;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        MediaHeaders metaData = null;
        Metadata.Key<?> mediaMetaDatakey = MEDIA_META_DATA.getMetaDataKey();

        if (headers.containsKey(mediaMetaDatakey)) {
            {
                try {
                    byte[] metaDataBytes = headers.get((Metadata.Key<byte[]>) mediaMetaDatakey);
                    metaData = MediaHeaders.parseFrom(metaDataBytes);
                    String secret = metaData.getClientSecret();

                    boolean authorized = bucketAuthorization.isRequestAuthorized(secret);

                    if(!authorized){
                        call.close(Status.UNAUTHENTICATED.withDescription("Request is not authorized"), headers);
                        return next.startCall(call, headers);
                    }

                    String clientId = bucketAuthorization.extractClientId(secret);
                    MediaDetails mediaDetails = MediaDetails.newBuilder()
                            .setContentType(metaData.getContentType())
                            .setClientId(clientId)
                            .build();

//                    Create a context for current call.
                    Context currentContext = Context.current().withValue(MEDIA_DETAILS.getContextKey(), mediaDetails);
//                    Add the created Context to current request.
                    return Contexts.interceptCall(currentContext, call, headers, next);
                } catch (Exception e) {
                    log.error("Unable to parse media meta data", e);
                    call.close(Status.INTERNAL.withDescription("Unable to parse media meta data"), headers);
                }
            }
        }

        return next.startCall(call, headers);
    }
}
