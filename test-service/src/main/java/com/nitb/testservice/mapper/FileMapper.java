package com.nitb.testservice.mapper;

import com.google.protobuf.ByteString;
import com.nitb.testservice.grpc.TestTemplateResponse;

public class FileMapper {
    private FileMapper() {}

    public static TestTemplateResponse toTestTemplateResponse(byte[] data) {
        return TestTemplateResponse.newBuilder()
                .setFileName("testTemplate.xlsx")
                .setFileContent(ByteString.copyFrom(data))
                .build();
    }
}
