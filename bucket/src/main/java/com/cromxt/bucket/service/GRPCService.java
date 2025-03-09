package com.cromxt.bucket.service;

import com.cromxt.bucket.constants.FileVisibility;
import com.cromxt.proto.files.Visibility;
import lombok.NonNull;

public interface GRPCService {
    @NonNull
    default Visibility getFileVisibility(FileVisibility visibility) {
        return switch (visibility) {
            case PRIVATE_ACCESS -> Visibility.PRIVATE;
            case PROTECTED_ACCESS -> Visibility.PROTECTED;
            case PUBLIC_ACCESS -> Visibility.PUBLIC;
        };
    }
    default FileVisibility getFileVisibility(Visibility visibility) {
        return switch (visibility) {
            case PRIVATE -> FileVisibility.PRIVATE_ACCESS;
            case PROTECTED -> FileVisibility.PROTECTED_ACCESS;
            case PUBLIC -> FileVisibility.PUBLIC_ACCESS;
            case UNRECOGNIZED -> throw new IllegalArgumentException("Invalid visibility");
        };
    }

}
