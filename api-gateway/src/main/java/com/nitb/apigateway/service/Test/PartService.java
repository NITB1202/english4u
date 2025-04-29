package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Part.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.request.Part.UpdatePartRequestDto;
import com.nitb.apigateway.dto.Test.response.Part.PartDetailResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartSummaryResponseDto;

import java.util.List;
import java.util.UUID;

public interface PartService {
    PartResponseDto createPart(UUID userId, CreatePartRequestDto request);
    PartDetailResponseDto getPartDetailById(UUID id);
    List<PartSummaryResponseDto> getAllPartsForTest(UUID testId);
    ActionResponseDto updatePart(UUID id, UUID userId, UpdatePartRequestDto request);
    ActionResponseDto swapPartsPosition(UUID userId, UUID part1Id, UUID part2Id);
}
