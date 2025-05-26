package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.Comment.request.PostCommentRequestDto;
import com.nitb.apigateway.dto.Test.Comment.request.ReplyCommentRequestDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentResponseDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentsResponseDto;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final TestServiceGrpcClient testGrpc;

    @Override
    public Mono<CommentResponseDto> postComment(UUID userId, PostCommentRequestDto request) {
        return null;
    }

    @Override
    public Mono<CommentResponseDto> replyComment(UUID userId, ReplyCommentRequestDto request) {
        return null;
    }

    @Override
    public Mono<CommentsResponseDto> getComments(UUID testId, int page, int size) {
        return null;
    }
}
