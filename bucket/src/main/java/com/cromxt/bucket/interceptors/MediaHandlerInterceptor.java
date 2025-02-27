package com.cromxt.bucket.interceptors;

import com.cromxt.proto.files.MediaHeaders;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import static com.cromxt.grpc.MediaHeadersKey.MEDIA_META_DATA;

@Slf4j
public class MediaHandlerInterceptor implements ServerInterceptor {
    private final String secretKey;

    public MediaHandlerInterceptor(String secretKey){
        this.secretKey = secretKey;
    }
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
                                                                 Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        MediaHeaders metaData = null;
        Metadata.Key<?> mediaMetaDatakey = MEDIA_META_DATA.getMetaDataKey();

//        TODO: Add the request validation to verify the request authenticity using JWT token.

        if (headers.containsKey(mediaMetaDatakey)) {
            {
                try {
                    byte[] metaDataBytes = headers.get((Metadata.Key<byte[]>) mediaMetaDatakey);
                    metaData = MediaHeaders.parseFrom(metaDataBytes);
                    Context currentContext = Context.current().withValue(MEDIA_META_DATA.getContextKey(), metaData);
                    return Contexts.interceptCall(currentContext, call, headers, next);
                } catch (Exception e) {
                    log.error("Unable to parse media meta data", e);
                    call.close(Status.INTERNAL.withDescription("Unable to parse media meta data"), headers);
                }
            }
        }
        return new ServerCall.Listener<ReqT>() {
        };
    }
}