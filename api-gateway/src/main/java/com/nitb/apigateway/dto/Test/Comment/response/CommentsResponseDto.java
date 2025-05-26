package com.nitb.apigateway.dto.Test.Comment.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsResponseDto {
    private List<CommentThreadResponseDto> comments;

    private long totalItems;

    private int totalPages;
}
