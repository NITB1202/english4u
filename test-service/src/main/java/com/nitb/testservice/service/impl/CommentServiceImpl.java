package com.nitb.testservice.service.impl;

import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Comment;
import com.nitb.testservice.grpc.GetCommentsRequest;
import com.nitb.testservice.grpc.PostCommentRequest;
import com.nitb.testservice.grpc.ReplyCommentRequest;
import com.nitb.testservice.repository.CommentRepository;
import com.nitb.testservice.service.CommentService;
import com.nitb.testservice.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TestService testService;

    @Override
    public Comment postComment(PostCommentRequest request) {
        testService.validateTestId(request.getTestId());

        Comment comment = Comment.builder()
                .rootId(null)
                .userId(UUID.fromString(request.getUserId()))
                .testId(UUID.fromString(request.getTestId()))
                .createAt(LocalDateTime.now())
                .content(request.getContent())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public Comment replyComment(ReplyCommentRequest request) {
        UUID rootId = UUID.fromString(request.getRootId());
        UUID userId = UUID.fromString(request.getUserId());

        Comment rootComment = commentRepository.findById(rootId).orElseThrow(
                () -> new NotFoundException("Comment with id " + rootId + " not found.")
        );

        Comment comment = Comment.builder()
                .rootId(rootId)
                .userId(userId)
                .testId(rootComment.getTestId())
                .createAt(LocalDateTime.now())
                .content(request.getContent())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getComments(GetCommentsRequest request) {
        return null;
    }
}
