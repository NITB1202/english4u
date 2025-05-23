package com.nitb.testservice.mapper;

import com.nitb.testservice.entity.Part;
import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.PartResponse;
import com.nitb.testservice.grpc.PartsResponse;
import com.nitb.testservice.grpc.QuestionResponse;

import java.util.List;

public class PartMapper {
    private PartMapper() {}

    public static PartResponse toPartResponse(Part part, List<Question> questions) {
        List<QuestionResponse> questionResponses = questions.stream().map(QuestionMapper::toQuestionResponse).toList();

        return PartResponse.newBuilder()
                .setId(part.getId().toString())
                .setPosition(part.getPosition())
                .setContent(part.getContent())
                .setQuestionCount(part.getQuestionCount())
                .addAllQuestions(questionResponses)
                .build();
    }

    public static PartsResponse toPartsResponse(List<PartResponse> parts) {
        return PartsResponse.newBuilder()
                .addAllParts(parts)
                .build();
    }
}
