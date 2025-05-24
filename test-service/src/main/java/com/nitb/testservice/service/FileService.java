package com.nitb.testservice.service;

import com.nitb.testservice.grpc.UploadTestTemplateRequest;

public interface FileService {
    byte[] generateTestTemplate();
    void uploadTestTemplate(UploadTestTemplateRequest request);
}
