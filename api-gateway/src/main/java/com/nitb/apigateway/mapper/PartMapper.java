package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Test.Part.request.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.Part.response.PartDetailResponseDto;
import com.nitb.apigateway.dto.Test.Question.response.QuestionSummaryResponseDto;
import com.nitb.testservice.grpc.CreatePartRequest;
import com.nitb.testservice.grpc.CreateQuestionRequest;
import com.nitb.testservice.grpc.PartResponse;

import java.util.List;
import java.util.UUID;

public class PartMapper {
    private PartMapper() {}

    public static CreatePartRequest toCreatePartRequest(CreatePartRequestDto dto) {
        String content = dto.getContent() != null ? dto.getContent().trim() : "";
        List<CreateQuestionRequest> questions = dto.getQuestions().stream().map(QuestionMapper::toCreateQuestionRequest).toList();

        return CreatePartRequest.newBuilder()
                .setContent(content)
                .addAllQuestions(questions)
                .build();
    }

    public static PartDetailResponseDto toPartDetailResponseDto(PartResponse part) {
        List<QuestionSummaryResponseDto> questions = part.getQuestionsList().stream()
                .map(QuestionMapper::toQuestionSummaryResponseDto)
                .toList();

        return PartDetailResponseDto.builder()
                .id(UUID.fromString(part.getId()))
                .position(part.getPosition())
                .content(part.getContent())
                .questionCount(part.getQuestionCount())
                .questions(questions)
                .build();
    }
}
