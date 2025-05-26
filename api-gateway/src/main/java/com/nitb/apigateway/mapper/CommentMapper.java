package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Test.Comment.response.CommentResponseDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentThreadResponseDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentsResponseDto;
import com.nitb.testservice.grpc.CommentResponse;
import com.nitb.testservice.grpc.CommentThreadResponse;
import com.nitb.testservice.grpc.CommentsResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CommentMapper {
    private CommentMapper() {}

    public static CommentResponseDto toCommentResponseDto(CommentResponse comment) {
        return CommentResponseDto.builder()
                .id(UUID.fromString(comment.getId()))
                .createAt(LocalDateTime.parse(comment.getCreateAt()))
                .content(comment.getContent())
                .build();
    }

    public static CommentThreadResponseDto toCommentThreadResponseDto(CommentThreadResponse comment) {
        List<CommentThreadResponseDto> replies = comment.getRepliesList().stream()
                .map(CommentMapper::toCommentThreadResponseDto)
                .toList();

        return CommentThreadResponseDto.builder()
                .id(UUID.fromString(comment.getId()))
                .username(comment.getUserId())
                .userAvatar("None")
                .createAt(LocalDateTime.parse(comment.getCreateAt()))
                .content(comment.getContent())
                .replies(replies)
                .build();
    }

    public static CommentsResponseDto toCommentsResponseDto(CommentsResponse comments) {
        List<CommentThreadResponseDto> threads = comments.getCommentsList().stream()
                .map(CommentMapper::toCommentThreadResponseDto)
                .toList();

        return CommentsResponseDto.builder()
                .comments(threads)
                .totalItems(comments.getTotalItems())
                .totalPages(comments.getTotalPages())
                .build();
    }
}
