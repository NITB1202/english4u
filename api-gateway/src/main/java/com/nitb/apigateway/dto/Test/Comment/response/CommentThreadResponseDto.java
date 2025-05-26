package com.nitb.apigateway.dto.Test.Comment.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentThreadResponseDto {
    private UUID id;

    private String username;

    private String userAvatar;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createAt;

    private String content;

    private List<CommentThreadResponseDto> replies;
}
