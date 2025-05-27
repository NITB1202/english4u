package com.nitb.fileservice.mapper;

import com.nitb.fileservice.grpc.FileResponse;

public class FileMapper {
    private FileMapper() {}

    public static FileResponse toFileResponse(String url) {
        return FileResponse.newBuilder()
                .setUrl(url)
                .build();
    }
}
