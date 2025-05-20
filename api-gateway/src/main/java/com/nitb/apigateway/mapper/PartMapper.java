package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Test.response.Part.PartDetailResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartSummaryResponseDto;
import com.nitb.apigateway.dto.Test.response.Question.QuestionSummaryResponseDto;
import com.nitb.testservice.grpc.PartResponse;
import com.nitb.testservice.grpc.PartSummaryResponse;
import com.nitb.testservice.grpc.QuestionSummaryResponse;

import java.util.List;
import java.util.UUID;

public class PartMapper {
    private PartMapper() {}

    public static PartResponseDto toPartResponseDto(PartResponse part) {
        return PartResponseDto.builder()
                .id(UUID.fromString(part.getId()))
                .position(part.getPosition())
                .content(part.getContent())
                .questionCount(part.getQuestionCount())
                .build();
    }

    public static PartDetailResponseDto toPartDetailResponseDto(UUID id, String content, List<QuestionSummaryResponse> questions) {
        List<QuestionSummaryResponseDto> dto = questions.stream()
                .map(QuestionMapper::toQuestionSummaryResponseDto)
                .toList();

        return PartDetailResponseDto.builder()
                .id(id)
                .content(content)
                .questions(dto)
                .build();
    }

    public static PartSummaryResponseDto toPartSummaryResponseDto(PartSummaryResponse part) {
        return PartSummaryResponseDto.builder()
                .id(UUID.fromString(part.getId()))
                .position(part.getPosition())
                .questionCount(part.getQuestionCount())
                .build();
    }
}
