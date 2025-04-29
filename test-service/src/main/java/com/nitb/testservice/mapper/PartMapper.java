package com.nitb.testservice.mapper;

import com.nitb.testservice.entity.Part;
import com.nitb.testservice.grpc.GetAllPartsForTestResponse;
import com.nitb.testservice.grpc.PartResponse;
import com.nitb.testservice.grpc.PartSummaryResponse;

import java.util.List;

public class PartMapper {
    private PartMapper() {}

    public static PartResponse toPartResponse(Part part) {
        return PartResponse.newBuilder()
                .setId(part.getId().toString())
                .setPosition(part.getPosition())
                .setContent(part.getContent())
                .setQuestionCount(part.getQuestionCount())
                .build();
    }

    public static PartSummaryResponse toPartSummaryResponse(Part part) {
        return PartSummaryResponse.newBuilder()
                .setId(part.getId().toString())
                .setPosition(part.getPosition())
                .setQuestionCount(part.getQuestionCount())
                .build();
    }

    public static GetAllPartsForTestResponse toGetAllPartsForTestResponse(List<Part> parts) {
        List<PartSummaryResponse> responses = parts.stream()
                .map(PartMapper::toPartSummaryResponse)
                .toList();

        return GetAllPartsForTestResponse.newBuilder()
                .addAllParts(responses)
                .build();
    }

}
