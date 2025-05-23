package com.crombucket.storageservice.service;

import com.crombucket.storageservice.constants.FileVisibility;
import com.crombucket.proto.storagenode.Visibility;
import lombok.NonNull;

public interface GRPCService {
    @NonNull
    default Visibility getFileVisibility(FileVisibility visibility) {
        return switch (visibility) {
            case PRIVATE -> Visibility.PRIVATE;
            case PROTECTED -> Visibility.PROTECTED;
            case PUBLIC -> Visibility.PUBLIC;
        };
    }
    default FileVisibility getFileVisibility(Visibility visibility) {
        return switch (visibility) {
            case PRIVATE -> FileVisibility.PRIVATE;
            case PROTECTED -> FileVisibility.PROTECTED;
            case PUBLIC -> FileVisibility.PUBLIC;
            case UNRECOGNIZED -> throw new IllegalArgumentException("Invalid visibility");
        };
    }

}
