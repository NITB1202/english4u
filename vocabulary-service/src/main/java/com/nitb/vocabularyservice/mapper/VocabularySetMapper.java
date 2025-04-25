package com.nitb.vocabularyservice.mapper;

import com.nitb.vocabularyservice.dto.VocabularySetStatisticDto;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;

public class VocabularySetMapper {
    private VocabularySetMapper() {}

    public static CreateVocabularySetResponse toCreateVocabularySetResponse(VocabularySet set) {
        return CreateVocabularySetResponse.newBuilder()
                .setId(set.getId().toString())
                .setName(set.getName())
                .setCreatedBy(set.getCreatedBy().toString())
                .setCreateAt(set.getCreatedAt().toString())
                .build();
    }

    public static VocabularySetDetailResponse toVocabularySetDetailResponse(VocabularySet set) {
        return VocabularySetDetailResponse.newBuilder()
                .setId(set.getId().toString())
                .setCreatedBy(set.getCreatedBy().toString())
                .setCreateAt(set.getCreatedAt().toString())
                .setName(set.getName())
                .setWordCount(set.getWordCount())
                .setUpdatedBy(set.getUpdatedBy().toString())
                .setUpdateAt(set.getUpdatedAt().toString())
                .setIsDeleted(set.getIsDeleted())
                .build();
    }

    public static VocabularySetSummaryResponse toVocabularySetSummaryResponse(VocabularySet set) {
        return VocabularySetSummaryResponse.newBuilder()
                .setId(set.getId().toString())
                .setName(set.getName())
                .setWordCount(set.getWordCount())
                .build();
    }

    public static VocabularySetsResponse toVocabularySetsResponse(Page<VocabularySet> sets) {
        List<VocabularySetSummaryResponse> summary = sets.stream()
                .map(VocabularySetMapper::toVocabularySetSummaryResponse)
                .toList();

        return VocabularySetsResponse.newBuilder()
                .addAllSets(summary)
                .setTotalItems(sets.getTotalElements())
                .setTotalPages(sets.getTotalPages())
                .build();
    }

    public static UpdateVocabularySetResponse toUpdateVocabularySetResponse(VocabularySet set) {
        return UpdateVocabularySetResponse.newBuilder()
                .setId(set.getId().toString())
                .setName(set.getName())
                .setUpdatedBy(set.getUpdatedBy().toString())
                .setUpdateAt(set.getUpdatedAt().toString())
                .build();
    }

    public static DeleteVocabularySetResponse toDeleteVocabularySetResponse(VocabularySet set) {
        return DeleteVocabularySetResponse.newBuilder()
                .setId(set.getId().toString())
                .setUpdatedBy(set.getUpdatedBy().toString())
                .setUpdateAt(set.getUpdatedAt().toString())
                .setIsDeleted(set.getIsDeleted())
                .build();
    }

    public static VocabularySetStatistic toVocabularySetStatistic(VocabularySetStatisticDto dto) {
        return VocabularySetStatistic.newBuilder()
                .setTime(dto.getTime())
                .setCount(dto.getCount())
                .build();
    }

    public static CountPublishedVocabularySetsResponse toCountPublishedVocabularySetsResponse(List<VocabularySetStatisticDto> dto) {
        List<VocabularySetStatistic> statistics = dto.stream()
                .map(VocabularySetMapper::toVocabularySetStatistic)
                .toList();

        return CountPublishedVocabularySetsResponse.newBuilder()
                .addAllStatistics(statistics)
                .build();
    }
}
