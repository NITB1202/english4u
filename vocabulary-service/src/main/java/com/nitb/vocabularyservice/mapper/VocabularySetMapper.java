package com.nitb.vocabularyservice.mapper;

import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

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
                .setVersion(set.getVersion())
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
                .setVersion(set.getVersion())
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
                .setVersion(set.getVersion())
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

    public static VocabularySetStatisticResponse toVocabularySetStatisticResponse(String time, long count) {
        return VocabularySetStatisticResponse.newBuilder()
                .setTime(time)
                .setCount(count)
                .build();
    }

    public static CountPublishedVocabularySetsResponse toCountPublishedVocabularySetsResponse(List<VocabularySetStatisticResponse> statistics) {
        return CountPublishedVocabularySetsResponse.newBuilder()
                .addAllStatistics(statistics)
                .build();
    }

    public static AdminSetStatisticResponse toAdminSetStatisticResponse(UUID userId, long totalPublishedSets) {
        return AdminSetStatisticResponse.newBuilder()
                .setUserId(userId.toString())
                .setTotalPublishedSets(totalPublishedSets)
                .build();
    }

    public static AdminSetStatisticsResponse toAdminSetStatisticsResponse(List<AdminSetStatisticResponse> statistics) {
        return AdminSetStatisticsResponse.newBuilder()
                .addAllStatistics(statistics)
                .build();
    }
}
