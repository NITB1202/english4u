package com.nitb.apigateway.dto.Test.Comment.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyCommentRequestDto {
    @NotNull(message = "Parent id is required.")
    private UUID parentId;

    @NotEmpty(message = "Content is required.")
    private String content;
}
