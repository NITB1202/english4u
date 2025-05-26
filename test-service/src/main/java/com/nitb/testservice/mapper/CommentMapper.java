package com.nitb.testservice.mapper;

import com.nitb.testservice.entity.Comment;
import com.nitb.testservice.grpc.CommentResponse;
import com.nitb.testservice.grpc.CommentThreadResponse;
import com.nitb.testservice.grpc.CommentsResponse;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CommentMapper {
    private CommentMapper() {}

    public static CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.newBuilder()
                .setId(comment.getId().toString())
                .setCreateAt(comment.getCreateAt().toString())
                .setContent(comment.getContent())
                .build();
    }

    public static CommentThreadResponse toCommentThreadResponse(Comment comment) {
        List<CommentThreadResponse> replies = Optional.ofNullable(comment.getReplies())
                .orElse(Collections.emptyList())
                .stream()
                .map(CommentMapper::toCommentThreadResponse)
                .toList();

       return CommentThreadResponse.newBuilder()
               .setId(comment.getId().toString())
               .setUserId(comment.getUserId().toString())
               .setCreateAt(comment.getCreateAt().toString())
               .setContent(comment.getContent())
               .addAllReplies(replies)
               .build();
    }

    public static CommentsResponse toCommentsResponse(Page<Comment> comments) {
        List<CommentThreadResponse> threads = comments.stream()
                .map(CommentMapper::toCommentThreadResponse)
                .toList();

        return CommentsResponse.newBuilder()
                .addAllComments(threads)
                .setTotalItems(comments.getTotalElements())
                .setTotalPages(comments.getTotalPages())
                .build();
    }
}
