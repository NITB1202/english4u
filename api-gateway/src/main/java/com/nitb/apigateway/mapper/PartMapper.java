package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Test.request.Part.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.response.Part.PartDetailResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartSummaryResponseDto;
import com.nitb.apigateway.dto.Test.response.Question.QuestionSummaryResponseDto;
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
