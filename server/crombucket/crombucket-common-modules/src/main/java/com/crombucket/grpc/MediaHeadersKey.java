package com.cromxt.common.crombucket.grpc;


import com.cromxt.proto.files.AuthKey;
import com.cromxt.proto.files.FileMetadata;
import io.grpc.Context;
import io.grpc.Metadata;
import lombok.Getter;

public class MediaHeadersKey {

    public static final HeaderKeyValue<FileMetadata>  FILE_METADATA = new HeaderKeyValue<>("media-metadata", HeaderType.BINARY);
    public static final HeaderKeyValue<AuthKey> AUTH_KEY = new HeaderKeyValue<>("auth-key", HeaderType.BINARY);

    /*
        public static final Metadata.Key<byte[]> MEDIA_META_DATA_KEY = Metadata.Key.of(String.format("%s-bin", "media-metadata"), Metadata.BINARY_BYTE_MARSHALLER);
        public static final Metadata.Key<byte[]> MEDIA_DETAILS_KEY = Metadata.Key.of(String.format("%s-bin", "media-details"), Metadata.BINARY_BYTE_MARSHALLER);
     */

    @Getter
    public static class HeaderKeyValue<T> {
        private final String keyIdentifier;
        private final Metadata.Key<?> metaDataKey;
        private final Context.Key<T> contextKey;

        public HeaderKeyValue(
                String keyIdentifier,
                HeaderType headerType
        ) {
            this.keyIdentifier = keyIdentifier;
            if (headerType == HeaderType.STRING) {
                this.metaDataKey = Metadata.Key.of(keyIdentifier, Metadata.ASCII_STRING_MARSHALLER);
            } else {
                this.metaDataKey = Metadata.Key.of(String.format("%s-bin", keyIdentifier), Metadata.BINARY_BYTE_MARSHALLER);
            }
            this.contextKey = Context.key(keyIdentifier);
        }

    }

    public enum HeaderType {
        STRING,
        BINARY
    }
}