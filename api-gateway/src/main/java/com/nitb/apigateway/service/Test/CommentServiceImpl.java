package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.Comment.request.PostCommentRequestDto;
import com.nitb.apigateway.dto.Test.Comment.request.ReplyCommentRequestDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentResponseDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentsResponseDto;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import com.nitb.apigateway.grpc.UserServiceGrpcClient;
import com.nitb.apigateway.mapper.CommentMapper;
import com.nitb.testservice.grpc.CommentResponse;
import com.nitb.testservice.grpc.CommentsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final TestServiceGrpcClient testGrpc;
    private final UserServiceGrpcClient userGrpc;

    @Override
    public Mono<CommentResponseDto> postComment(UUID userId, PostCommentRequestDto request) {
        return Mono.fromCallable(()-> {
            //Check permission
            userGrpc.checkCanPerformAction(userId);

            //Post comment
            CommentResponse response = testGrpc.postComment(userId, request);
            return CommentMapper.toCommentResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<CommentResponseDto> replyComment(UUID userId, ReplyCommentRequestDto request) {
        return Mono.fromCallable(()->{
            //Check permission
            userGrpc.checkCanPerformAction(userId);

            //Reply comment
            CommentResponse response = testGrpc.replyComment(userId, request);
            return CommentMapper.toCommentResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<CommentsResponseDto> getComments(UUID testId, int page, int size) {
        return Mono.fromCallable(()->{
            CommentsResponse response = testGrpc.getComments(testId, page, size);
            return CommentMapper.toCommentsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
