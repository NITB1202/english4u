package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Part.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.request.Part.UpdatePartRequestDto;
import com.nitb.apigateway.dto.Test.response.Part.PartDetailResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartSummaryResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface PartService {
    Mono<PartResponseDto> addPartToTest(UUID userId, UUID testId, CreatePartRequestDto request);
    Mono<PartDetailResponseDto> getPartDetailById(UUID id);
    Mono<List<PartSummaryResponseDto>> getAllPartsForTest(UUID testId);
    Mono<ActionResponseDto> updatePart(UUID id, UUID userId, UpdatePartRequestDto request);
    Mono<ActionResponseDto> swapPartsPosition(UUID userId, UUID part1Id, UUID part2Id);
}
