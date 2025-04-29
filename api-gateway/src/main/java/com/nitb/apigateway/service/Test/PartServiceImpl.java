package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Part.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.request.Part.UpdatePartRequestDto;
import com.nitb.apigateway.dto.Test.response.Part.PartDetailResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartSummaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {
    @Override
    public PartResponseDto createPart(UUID userId, CreatePartRequestDto request) {
        return null;
    }

    @Override
    public PartDetailResponseDto getPartDetailById(UUID id) {
        return null;
    }

    @Override
    public List<PartSummaryResponseDto> getAllPartsForTest(UUID testId) {
        return List.of();
    }

    @Override
    public ActionResponseDto updatePart(UUID id, UUID userId, UpdatePartRequestDto request) {
        return null;
    }

    @Override
    public ActionResponseDto swapPartsPosition(UUID userId, UUID part1Id, UUID part2Id) {
        return null;
    }
}
