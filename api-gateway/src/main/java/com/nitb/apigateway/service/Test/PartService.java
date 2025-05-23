package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.Part.response.PartDetailResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface PartService {
    Mono<List<PartDetailResponseDto>> getAllPartsForTest(UUID testId);
}
