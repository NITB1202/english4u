package com.nitb.testservice.service;

import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Comment;
import com.nitb.testservice.grpc.GetCommentsRequest;
import com.nitb.testservice.grpc.PostCommentRequest;
import com.nitb.testservice.grpc.ReplyCommentRequest;
import com.nitb.testservice.repository.CommentRepository;
import com.nitb.testservice.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TestService testService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void postComment_shouldSaveAndReturnComment() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        String content = "This is a test comment.";

        PostCommentRequest request = PostCommentRequest.newBuilder()
                .setUserId(userId.toString())
                .setTestId(testId.toString())
                .setContent(content)
                .build();

        // Mock testService validation
        doNothing().when(testService).validateTestId(request.getTestId());

        // Stub comment to be returned from repository
        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Comment savedComment = commentService.postComment(request);

        // Then
        assertNotNull(savedComment);
        assertEquals(userId, savedComment.getUserId());
        assertEquals(testId, savedComment.getTestId());
        assertEquals(content, savedComment.getContent());
        assertNotNull(savedComment.getCreateAt());

        verify(testService, times(1)).validateTestId(request.getTestId());
        verify(commentRepository, times(1)).save(commentCaptor.capture());

        // Verify values passed to repository
        Comment captured = commentCaptor.getValue();
        assertEquals(userId, captured.getUserId());
        assertEquals(testId, captured.getTestId());
        assertEquals(content, captured.getContent());
    }

    @Test
    void replyComment_shouldSaveReplySuccessfully() {
        // Given
        UUID parentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        String replyContent = "This is a reply.";

        ReplyCommentRequest request = ReplyCommentRequest.newBuilder()
                .setParentId(parentId.toString())
                .setUserId(userId.toString())
                .setContent(replyContent)
                .build();

        Comment parentComment = Comment.builder()
                .id(parentId)
                .userId(UUID.randomUUID())
                .testId(testId)
                .content("Original comment")
                .createAt(LocalDateTime.now().minusDays(1))
                .build();

        Comment replyComment = Comment.builder()
                .id(UUID.randomUUID())
                .parent(parentComment)
                .userId(userId)
                .testId(testId)
                .content(replyContent)
                .createAt(LocalDateTime.now())
                .build();

        // Mock behavior
        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parentComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(replyComment);

        // When
        Comment result = commentService.replyComment(request);

        // Then
        assertNotNull(result);
        assertEquals(replyContent, result.getContent());
        assertEquals(parentComment, result.getParent());
        assertEquals(testId, result.getTestId());
        assertEquals(userId, result.getUserId());
        verify(commentRepository).findById(parentId);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void replyComment_shouldThrowNotFoundException_whenParentNotFound() {
        // Given
        UUID parentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ReplyCommentRequest request = ReplyCommentRequest.newBuilder()
                .setParentId(parentId.toString())
                .setUserId(userId.toString())
                .setContent("Reply")
                .build();

        when(commentRepository.findById(parentId)).thenReturn(Optional.empty());

        // Then
        assertThrows(NotFoundException.class, () -> commentService.replyComment(request));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void getRootComments_shouldReturnPage_whenValidRequest() {
        UUID testId = UUID.randomUUID();

        GetCommentsRequest request = GetCommentsRequest.newBuilder()
                .setTestId(testId.toString())
                .setPage(1)
                .setSize(10)
                .build();

        // Giả lập trả về trang rỗng
        Page<Comment> emptyPage = Page.empty(PageRequest.of(0, 10));

        // Stub: chú ý dùng matcher any(Pageable.class) để không strict check argument
        when(commentRepository.findByTestIdAndParentIdIsNullOrderByCreateAtDesc(eq(testId), any(Pageable.class)))
                .thenReturn(emptyPage);

        // Call service
        Page<Comment> result = commentService.getRootComments(request);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());

        verify(commentRepository, times(1)).findByTestIdAndParentIdIsNullOrderByCreateAtDesc(eq(testId), any(Pageable.class));
    }

    @Test
    void getRootComments_shouldUseDefaultPageAndSize_whenPageOrSizeInvalid() {
        UUID testId = UUID.randomUUID();

        GetCommentsRequest request = GetCommentsRequest.newBuilder()
                .setTestId(testId.toString())
                .setPage(0)
                .setSize(0)
                .build();

        int defaultSize = 10;
        Pageable pageable = PageRequest.of(0, defaultSize, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Comment> emptyPage = Page.empty(pageable);

        when(commentRepository.findByTestIdAndParentIdIsNullOrderByCreateAtDesc(eq(testId), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<Comment> result = commentService.getRootComments(request);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(commentRepository).findByTestIdAndParentIdIsNullOrderByCreateAtDesc(eq(testId), any(Pageable.class));
    }
}