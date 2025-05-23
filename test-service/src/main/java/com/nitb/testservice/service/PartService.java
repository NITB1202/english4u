package com.nitb.testservice.service;

import com.nitb.testservice.entity.Part;
import com.nitb.testservice.grpc.CreatePartRequest;
import com.nitb.testservice.grpc.GetAllPartsForTestRequest;

import java.util.List;
import java.util.UUID;

public interface PartService {
    Part createPart(UUID testId, int position, CreatePartRequest request);
    List<Part> getAllPartsForTest(GetAllPartsForTestRequest request);
    void updateQuestionCount(UUID partId, int count);
    int getTotalQuestionCount(UUID testId);
    String getPartContent(UUID partId);
    int getPartCount(UUID testId);
}
