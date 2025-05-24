package com.nitb.testservice.service.impl;

import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Part;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.repository.PartRepository;
import com.nitb.testservice.service.PartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;

    @Override
    @Transactional
    public Part createPart(UUID testId, int position, String content) {
        Part part = Part.builder()
                .testId(testId)
                .position(position)
                .content(content)
                .questionCount(0)
                .build();

        return partRepository.save(part);
    }

    @Override
    public List<Part> getAllPartsForTest(GetAllPartsForTestRequest request) {
        return partRepository.getAllByTestId(UUID.fromString(request.getTestId()));
    }

    @Override
    public int getTotalQuestionCount(UUID testId) {
        int count = 0;

        List<Part> parts = partRepository.getAllByTestId(testId);

        for(Part part : parts) {
            count += part.getQuestionCount();
        }

        return count;
    }

    @Override
    public String getPartContent(UUID partId) {
        Part part = partRepository.findById(partId).orElseThrow(
                () -> new NotFoundException(String.format("Part with id %s not found", partId))
        );

        return part.getContent();
    }

    @Override
    public int getPartCount(UUID testId) {
        return partRepository.countByTestId(testId);
    }

    @Override
    public void updateQuestionCount(UUID partId, int count) {
        Part part = partRepository.findById(partId).orElseThrow(
                () -> new NotFoundException("Part not found.")
        );

        part.setQuestionCount(count);
        partRepository.save(part);
    }
}
