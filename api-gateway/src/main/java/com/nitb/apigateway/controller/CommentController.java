package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Test.Comment.request.PostCommentRequestDto;
import com.nitb.apigateway.dto.Test.Comment.request.ReplyCommentRequestDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentResponseDto;
import com.nitb.apigateway.dto.Test.Comment.response.CommentsResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Test.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Post a comment.")
    @ApiResponse(responseCode = "200", description = "Post successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<CommentResponseDto>> postComment(@AuthenticationPrincipal UUID userId,
                                                                @Valid @RequestBody PostCommentRequestDto request) {
        return commentService.postComment(userId, request).map(ResponseEntity::ok);
    }

    @PostMapping("/reply")
    @Operation(summary = "Reply a comment.")
    @ApiResponse(responseCode = "200", description = "Reply successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<CommentResponseDto>> replyComment(@AuthenticationPrincipal UUID userId,
                                                                 @Valid @RequestBody ReplyCommentRequestDto request) {
        return commentService.replyComment(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get a paginated list of comments.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<CommentsResponseDto>> getComments(@RequestParam UUID testId,
                                                                 @Positive(message = "Page must be positive") @RequestParam int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return commentService.getComments(testId, page, size).map(ResponseEntity::ok);
    }
}
