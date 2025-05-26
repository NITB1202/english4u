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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TestService testService;
    private final int DEFAULT_SIZE = 10;

    @Override
    public Comment postComment(PostCommentRequest request) {
        testService.validateTestId(request.getTestId());

        Comment comment = Comment.builder()
                .userId(UUID.fromString(request.getUserId()))
                .testId(UUID.fromString(request.getTestId()))
                .createAt(LocalDateTime.now())
                .content(request.getContent())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public Comment replyComment(ReplyCommentRequest request) {
        UUID parentId = UUID.fromString(request.getParentId());
        UUID userId = UUID.fromString(request.getUserId());

        Comment parentComment = commentRepository.findById(parentId).orElseThrow(
                () -> new NotFoundException("Comment with id " + parentId + " not found.")
        );

        Comment comment = Comment.builder()
                .parent(parentComment)
                .userId(userId)
                .testId(parentComment.getTestId())
                .createAt(LocalDateTime.now())
                .content(request.getContent())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getRootComments(GetCommentsRequest request) {
        UUID testId = UUID.fromString(request.getTestId());
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return commentRepository.findByTestIdAndParentIdIsNullOrderByCreateAtDesc(testId, PageRequest.of(page, size));
    }
}
