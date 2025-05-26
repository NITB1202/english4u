package com.nitb.apigateway.dto.UserTest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveResultRequestDto {
    @NotNull(message = "Test id is required.")
    private UUID testId;

    @NotNull(message = "Seconds spent are required.")
    private Long secondsSpent;

    @NotNull(message = "User answers are required.")
    @Size(min = 1, message = "The map must contain at least one entry.")
    private Map<UUID, String> userAnswers;
}
