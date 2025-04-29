package com.nitb.testservice.service;

import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Part;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.repository.PartRepository;
import com.nitb.testservice.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartService {
    private final PartRepository partRepository;
    private final TestRepository testRepository;

    public Part createPart(CreatePartRequest request) {
        UUID testId = UUID.fromString(request.getTestId());

        if(!testRepository.existsById(testId)) {
            throw new NotFoundException("Test not found.");
        }

        int order = partRepository.countByTestId(testId) + 1;

        Part part = Part.builder()
                .testId(testId)
                .order(order)
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

    public void updatePart(UpdatePartRequest request) {
        UUID id = UUID.fromString(request.getId());
        Part part = partRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Part not found.")
        );

        part.setContent(request.getContent());
        partRepository.save(part);
    }

    public void swapPartsPosition(SwapPartsPositionRequest request){
        Part firstPart = partRepository.findById(UUID.fromString(request.getPart1Id())).orElseThrow(
                () -> new NotFoundException("First part not found.")
        );

        Part secondPart = partRepository.findById(UUID.fromString(request.getPart2Id())).orElseThrow(
                () -> new NotFoundException("Second part not found.")
        );

        int temp = firstPart.getOrder();
        firstPart.setOrder(secondPart.getOrder());
        secondPart.setOrder(temp);

        partRepository.save(firstPart);
        partRepository.save(secondPart);
    }

    public int getTotalQuestion(UUID testId) {
        int count = 0;

        List<Part> parts = partRepository.getAllByTestId(testId);

        for(Part part : parts) {
            count += part.getQuestionCount();
        }

        return count;
    }
}
