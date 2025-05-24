package com.nitb.testservice.service;

import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.UploadTestTemplateRequest;

public interface FileService {
    byte[] generateTestTemplate();
    Test uploadTestTemplate(UploadTestTemplateRequest request);
}
