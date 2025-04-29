package com.nitb.testservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Part;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.repository.PartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartService {
    private final PartRepository partRepository;
    private final TestService testService;

    @Transactional
    public Part createPart(CreatePartRequest request) {
        UUID testId = UUID.fromString(request.getTestId());
        UUID userId = UUID.fromString(request.getUserId());

        //Update test
        testService.increasePartCount(testId, userId);

        //Create part
        int position = partRepository.countByTestId(testId) + 1;

        Part part = Part.builder()
                .testId(testId)
                .position(position)
                .content(request.getContent())
                .questionCount(0)
                .build();

        return partRepository.save(part);
    }

    public String getPartContentById(GetPartContentByIdRequest request) {
        UUID id = UUID.fromString(request.getId());
        Part part = partRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Part not found.")
        );

        return part.getContent();
    }

    public List<Part> getAllPartsForTest(GetAllPartsForTestRequest request) {
        return partRepository.getAllByTestId(UUID.fromString(request.getTestId()));
    }

    @Transactional
    public void updatePart(UpdatePartRequest request) {
        UUID id = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());

        Part part = partRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Part not found.")
        );

        part.setContent(request.getContent());
        partRepository.save(part);

        testService.updateLastModified(part.getTestId(), userId);
    }

    public void swapPartsPosition(SwapPartsPositionRequest request){
        Part firstPart = partRepository.findById(UUID.fromString(request.getPart1Id())).orElseThrow(
                () -> new NotFoundException("First part not found.")
        );

        Part secondPart = partRepository.findById(UUID.fromString(request.getPart2Id())).orElseThrow(
                () -> new NotFoundException("Second part not found.")
        );

        if(!firstPart.getTestId().equals(secondPart.getTestId())){
            throw new BusinessException("First and second parts are not in the same test.");
        }

        int temp = firstPart.getPosition();
        firstPart.setPosition(secondPart.getPosition());
        secondPart.setPosition(temp);

        partRepository.save(firstPart);
        partRepository.save(secondPart);

        testService.updateLastModified(firstPart.getTestId(), UUID.fromString(request.getUserId()));
    }

    public int getTotalQuestion(UUID testId) {
        int count = 0;

        List<Part> parts = partRepository.getAllByTestId(testId);

        for(Part part : parts) {
            count += part.getQuestionCount();
        }

        return count;
    }

    public void updateQuestionCount(UUID partId, int count, UUID userId) {
        Part part = partRepository.findById(partId).orElseThrow(
                () -> new NotFoundException("Part not found.")
        );

        part.setQuestionCount(count);
        partRepository.save(part);

        testService.updateLastModified(part.getTestId(), userId);
    }

    public void updateLastModified(UUID partId, UUID userId) {
        Part part = partRepository.findById(partId).orElseThrow(
                () -> new NotFoundException("Part not found.")
        );

        testService.updateLastModified(part.getTestId(), userId);
    }
}
