package com.nitb.testservice.service;

import com.nitb.testservice.entity.Comment;
import com.nitb.testservice.grpc.GetCommentsRequest;
import com.nitb.testservice.grpc.PostCommentRequest;
import com.nitb.testservice.grpc.ReplyCommentRequest;
import org.springframework.data.domain.Page;

public interface CommentService {
    Comment postComment(PostCommentRequest request);
    Comment replyComment(ReplyCommentRequest request);
    Page<Comment> getComments(GetCommentsRequest request);
}
