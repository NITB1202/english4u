package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.Comment.request.PostCommentRequestDto;
import com.nitb.apigateway.dto.Test.Comment.request.ReplyCommentRequestDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentResponseDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentsResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CommentService {
    Mono<CommentResponseDto> postComment(UUID userId, PostCommentRequestDto request);
    Mono<CommentResponseDto> replyComment(UUID userId, ReplyCommentRequestDto request);
    Mono<CommentsResponseDto> getComments(UUID testId, int page, int size);
}
